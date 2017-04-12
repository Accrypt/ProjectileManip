package io.mzb.ProjectileManip.util.effects;

import org.bukkit.Location;
import org.bukkit.entity.EnderPearl;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.util.Vector;

/**
 * Created by Alex on 11/04/2017.
 */
public class EnderEffect extends BowEffect {

    private static final double damper = 0.2D;

    public EnderEffect(EntityShootBowEvent event, Entity singleTarget) {
        super(event, EntityType.ENDER_PEARL, 300, 1, singleTarget);

        Location loc = event.getProjectile().getLocation();
        Vector velocity = event.getProjectile().getVelocity();

        event.getProjectile().remove();
        EnderPearl pearl = (EnderPearl) event.getEntity().getWorld().spawnEntity(loc.add(0, 2, 0), EntityType.ENDER_PEARL);
        pearl.setVelocity(velocity);
        pearl.setGravity(false);
        pearl.setBounce(true);
        pearl.setShooter(null);
        event.setProjectile(pearl);
    }

    @Override
    public boolean cycleEffect() {
        Entity projectile = getEvent().getProjectile();
        if (projectile == null || projectile.isOnGround() || projectile.isDead()) {
            return true;
        }

        Entity target = getSingleTarget();

        Location targetLoc = target.getLocation();
        Location projectileLoc = projectile.getLocation();
        double dist = targetLoc.distanceSquared(projectileLoc);

        if (dist <= 2.5D) {
            projectile.getWorld().spawnEntity(projectileLoc, EntityType.ENDERMAN);
            projectile.remove();
            return true;
        }

        Vector from = projectileLoc.toVector();
        Vector to = targetLoc.toVector();

        Vector vector = to.subtract(from);
        Vector eggVector = new Vector(vector.getX() / (dist * damper), vector.getY() / (dist * damper), vector.getZ() / (dist * damper));
        projectile.setVelocity(eggVector);

        return false;
    }

}
