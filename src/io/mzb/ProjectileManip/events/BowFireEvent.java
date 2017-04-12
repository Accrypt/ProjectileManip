package io.mzb.ProjectileManip.events;

import io.mzb.ProjectileManip.util.Bows;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.inventory.ItemStack;

/**
 * Created by Alex on 11/04/2017.
 */
public class BowFireEvent implements Listener {

    @EventHandler
    public void onEntityFireBow(EntityShootBowEvent event) {
        LivingEntity shooter = event.getEntity();
        if(shooter.getType() == EntityType.PLAYER) {
            ItemStack itemBow = event.getBow();
            Bows bow = Bows.getBowForItem(itemBow);
            if(bow != null) { // TODO: Use optional?
                System.out.println("Found bow fire: " + bow.toString());
                bow.startEffect(event);
            }
        }
    }

}
