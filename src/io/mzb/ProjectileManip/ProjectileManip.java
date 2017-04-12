package io.mzb.ProjectileManip;

import io.mzb.ProjectileManip.events.BowFireEvent;
import io.mzb.ProjectileManip.events.JoinEvent;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * Created by Alex on 11/04/2017.
 */
public class ProjectileManip extends JavaPlugin {

    private static ProjectileManip instance;

    @Override
    public void onEnable() {
        this.instance = this;

        getServer().getPluginManager().registerEvents(new JoinEvent(), this);
        getServer().getPluginManager().registerEvents(new BowFireEvent(), this);
    }

    public static ProjectileManip getInstance() {
        return instance;
    }

}
