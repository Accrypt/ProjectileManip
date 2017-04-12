package io.mzb.ProjectileManip.events;

import io.mzb.ProjectileManip.util.Bows;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

/**
 * Created by Alex on 11/04/2017.
 */
public class JoinEvent implements Listener {

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        player.getInventory().clear();

        player.getInventory().addItem(Bows.ARROW.getItem());
        player.getInventory().addItem(Bows.EGG.getItem());
        player.getInventory().addItem(Bows.ENDER.getItem());
    }

}
