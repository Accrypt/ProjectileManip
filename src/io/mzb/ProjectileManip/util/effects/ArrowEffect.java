package io.mzb.ProjectileManip.util.effects;

import net.minecraft.server.v1_11_R1.EntityLiving;
import org.bukkit.Location;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Damageable;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Alex on 11/04/2017.
 */
public class ArrowEffect extends BowEffect {

    private static double damper = 0.2D;

    public ArrowEffect(EntityShootBowEvent event) {
        super(event, EntityType.ARROW, 3, 5, false);
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
            if (!(entity instanceof Damageable))
                removeFromNearby.add(entity);
        }
        for (Entity entity : removeFromNearby) {
            nearby.remove(entity);
        }

        if (nearby.size() > 0) {
            Entity target = nearby.get(0);
            double nearestDist = 100.0D;
            for(Entity allEntitys : nearby) {
                if(projectile.getLocation().distanceSquared(allEntitys.getLocation()) < nearestDist) {
                    nearestDist = projectile.getLocation().distanceSquared(allEntitys.getLocation());
                    target = allEntitys;
                }
            }
            Location targetLoc = target.getLocation();
            Location projectileLoc = projectile.getLocation();
            double dist = targetLoc.distanceSquared(projectileLoc);

            Vector from = projectileLoc.toVector();
            Vector to  = targetLoc.toVector();

            double angle = projectile.getLocation().getDirection().angle(to);
            double damperActual = damper;
            if(angle > Math.PI / 40) // 4.5deg = PI/40rad (45deg/s = stop // 4.5deg/10th sec = stop)
                damperActual += (angle / 20);

            Vector vector = to.subtract(from);
            Vector arrowVector = new Vector(vector.getX() / (dist * damperActual), vector.getY() / (dist * damperActual), vector.getZ() / (dist * damperActual));
            projectile.setVelocity(arrowVector);
        }
        return false;
    }

}
