package io.mzb.ProjectileManip.util.effects;

import io.mzb.ProjectileManip.ProjectileManip;
import org.bukkit.Bukkit;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Projectile;
import org.bukkit.event.entity.EntityShootBowEvent;

import java.util.UUID;

/**
 * Created by Alex on 11/04/2017.
 */
public abstract class BowEffect {

    // The event of the bow being shot, passed to allow event manipulation
    private EntityShootBowEvent event;
    // What is being fired?
    private EntityType entityType;
    // After X seconds, effect should cancel
    private int timeout;
    // How far should the effect search
    private int range;
    // Is the effect limited to hostiles?
    private boolean hostileLock;
    // Is the effect only targeting a single entity
    private Entity singleTarget;
    // ID of effect task
    private int taskId;
    // Total number of cycles ran
    private int cycles;

    public BowEffect(EntityShootBowEvent event, EntityType entityType, int timeout, int range, boolean hostileLock) {
        this.event = event;
        this.entityType = entityType;
        this.timeout = timeout;
        this.range = range;
        this.hostileLock = hostileLock;
        this.singleTarget = null;
    }

    public BowEffect(EntityShootBowEvent event, EntityType entityType, int timeout, int range, Entity singleTarget) {
        this.event = event;
        this.entityType = entityType;
        this.timeout = timeout;
        this.range = range;
        this.hostileLock = false;
        this.singleTarget = singleTarget;
    }

    /**
     * @return Return true if the effect should stop (e.g. no target or hit the target)
     */
    public abstract boolean cycleEffect();

    /**
     * Loops the effect every 2 ticks (10/sec), cancels if effect returns true or timeout is reached
     */
    public void startEffect() {
        taskId = Bukkit.getScheduler().scheduleSyncRepeatingTask(ProjectileManip.getInstance(), () -> {
            if(cycleEffect() || (cycles++ > timeout * 10)) {
               Bukkit.getScheduler().cancelTask(taskId);
            }
        }, 0, 2);
    }

    public EntityShootBowEvent getEvent() {
        return event;
    }

    public EntityType getEntityType() {
        return entityType;
    }

    public int getTimeout() {
        return timeout;
    }

    public int getRange() {
        return range;
    }

    public boolean isHostileLock() {
        return hostileLock;
    }

    public Entity getSingleTarget() {
        return singleTarget;
    }

}
