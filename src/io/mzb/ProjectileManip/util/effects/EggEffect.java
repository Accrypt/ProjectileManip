package io.mzb.ProjectileManip.util.effects;

import org.bukkit.Location;
import org.bukkit.entity.*;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Alex on 11/04/2017.
 */
public class EggEffect extends BowEffect {

    private static final double damper = 0.4D;
    private static final boolean explode = true;

    public EggEffect(EntityShootBowEvent event) {
        super(event, EntityType.EGG, 300, 20, true);

        Location loc = event.getProjectile().getLocation();
        Vector velocity = event.getProjectile().getVelocity();

        event.getProjectile().remove();
        Egg egg = (Egg) event.getEntity().getWorld().spawnEntity(loc.add(0, 2, 0), EntityType.EGG);
        egg.setVelocity(velocity);
        egg.setGravity(false);
        egg.setBounce(true);
        egg.setShooter(event.getEntity());
        event.setProjectile(egg);
        // Might want to remove chicken spawn from the egg
    }

    @Override
    public boolean cycleEffect() {
        Entity projectile = getEvent().getProjectile();
        if (projectile == null || projectile.isOnGround() || projectile.isDead()) {
            return true;
        }
        List<Entity> nearby = projectile.getNearbyEntities(getRange(), getRange(), getRange());

        // Don't want to fire at the person who shot the arrow
        if (nearby.contains(getEvent().getEntity()))
            nearby.remove(getEvent().getEntity());

        // Only want to hit living things
        List<Entity> removeFromNearby = new ArrayList<>();
        for (Entity entity : nearby) {
            if (!(entity instanceof Damageable)) {
                removeFromNearby.add(entity);
            } else if (isHostileLock()) {
                if (!(entity instanceof Monster)) {
                    removeFromNearby.add(entity);
                }
            }
        }
        for (Entity entity : removeFromNearby) {
            nearby.remove(entity);
        }

        if (nearby.size() > 0) {
            Entity target = nearby.get(0);
            double nearestDist = 100.0D;
            for (Entity allEntitys : nearby) {
                if (projectile.getLocation().distanceSquared(allEntitys.getLocation()) < nearestDist) {
                    nearestDist = projectile.getLocation().distanceSquared(allEntitys.getLocation());
                    target = allEntitys;
                }
            }
            Location targetLoc = target.getLocation();
            Location projectileLoc = projectile.getLocation();
            double dist = targetLoc.distanceSquared(projectileLoc);

            if(explode && dist <= 2.5D) {
                projectile.getWorld().createExplosion(projectile.getLocation(), 2, false);
                projectile.remove();
                return true;
            }

            Vector from = projectileLoc.toVector();
            Vector to = targetLoc.toVector();

            Vector vector = to.subtract(from);
            Vector eggVector = new Vector(vector.getX() / (dist * damper), vector.getY() / (dist * damper), vector.getZ() / (dist * damper));
            projectile.setVelocity(eggVector);
        }

        return false;
    }
}
