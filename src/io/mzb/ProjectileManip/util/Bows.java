package io.mzb.ProjectileManip.util;

import io.mzb.ProjectileManip.util.effects.ArrowEffect;
import io.mzb.ProjectileManip.util.effects.EggEffect;
import io.mzb.ProjectileManip.util.effects.EnderEffect;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Collection;
import java.util.List;
import java.util.Set;

/**
 * Created by Alex on 11/04/2017.
 */
public enum Bows {

    // Attempts to follow any entity within 5 blocks, for 3 seconds.
    ARROW("&7Arrow &fBow"),
    // Attempts to follow any nearby hostile creature within 20 blocks until it locks on.
    EGG("&eEgg &fBow"),
    // Grabs the player’s cursor location (and target) and attempts to slowly approach that entity until it reaches it.
    ENDER("&6Ender &fBow");

    private String itemName;

    Bows(String itemName) {
        this.itemName = itemName;
    }

    /**
     * @return Returns a bow item with the name of the special bow
     */
    public ItemStack getItem() {
        ItemStack itemStack = new ItemStack(Material.BOW);
        ItemMeta itemMeta = itemStack.getItemMeta();
        itemMeta.setDisplayName(ChatColor.translateAlternateColorCodes('&', "&f" + itemName));
        itemStack.setItemMeta(itemMeta);
        return itemStack;
    }

    /**
     * @param itemStack The item to compare against the bow
     * @return True if itemStack has the same name as the bow
     */
    public boolean isValid(ItemStack itemStack) {
        ItemMeta itemMeta = itemStack.getItemMeta();
        String displayName = ChatColor.stripColor(itemMeta.getDisplayName());
        String specialBowName = ChatColor.stripColor(ChatColor.translateAlternateColorCodes('&', this.itemName));
        return displayName.equals(specialBowName);
    }

    /**
     * Starts the effect of the projectile moving towards a player etc
     * @param event The event that starts the effect
     */
    public void startEffect(EntityShootBowEvent event) {
        switch (this) {
            case ARROW:
                new ArrowEffect(event).startEffect();
                break;
            case EGG:
                new EggEffect(event).startEffect();
                break;
            case ENDER:
                Location cursorTargetLoc = event.getEntity().getTargetBlock((Set<Material>) null, 16 * 6).getLocation();
                Collection<Entity> nearby = cursorTargetLoc.getWorld().getNearbyEntities(cursorTargetLoc, 25, 25, 25);
                if(nearby.size() > 0) {
                    Entity target = (Entity) nearby.toArray()[0];
                    double nearestDist = 100.0D;
                    for (Entity allEntitys : nearby) {
                        if (cursorTargetLoc.distanceSquared(allEntitys.getLocation()) < nearestDist) {
                            nearestDist = cursorTargetLoc.distanceSquared(allEntitys.getLocation());
                            target = allEntitys;
                        }
                    }
                    new EnderEffect(event, target).startEffect();
                } else {
                    event.setCancelled(true);
                    event.getEntity().sendMessage("Unable to start effect. No nearby target to cursor location.");
                }
                break;
        }
    }

    public static Bows getBowForItem(ItemStack itemStack) {
        for(Bows bow : Bows.values()) {
            if(bow.isValid(itemStack)) return bow;
        }
        return null;
    }

}
