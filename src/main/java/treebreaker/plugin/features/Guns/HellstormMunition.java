/*
 * Copyright (C) 2021 Daniel Allen
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package main.java.treebreaker.plugin.features.Guns;

import main.java.treebreaker.plugin.utils.Utils;
import org.bukkit.FluidCollisionMode;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.Damageable;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.util.RayTraceResult;
import org.bukkit.util.Vector;

import static main.java.treebreaker.plugin.features.Guns.Gun.addShot;

/**
 *
 * @author Daniel Allen
 */
public class HellstormMunition extends Projectile {

    private final int count;
    private final double detonationHeight;
    private final double spread;
    private final Rocket cluster;

    public HellstormMunition(Location newPos, Vector vel, double damage, Player shooter, Shot shot, int particleCount, Particle.DustOptions tracer, int munitionCount, double height, double spread, Rocket cluster) {
        super(newPos, vel, damage, shooter, shot, particleCount);
        this.count = munitionCount;
        this.detonationHeight = height;
        this.tracer = tracer;
        this.spread = spread;
        this.cluster = cluster;
    }

    /**
     * Called every tick on this projectile
     */
    @Override
    public void tick() {
        if (pos.getWorld() == null) {
            return;
        }
        lifetime++;
        before();

        Location origin = pos.clone();
        Location curPos = pos.clone();
        move();
        Location newPos = pos.clone();
        if (pos.clone().add(new Vector(vel.getX() / 20, vel.getY() / 20, vel.getZ() / 20)).getY() <= this.detonationHeight && this.vel.getY() <= 0) {
            explode();
            destroy();
        }
        RayTraceResult hits = pos.getWorld().rayTrace(origin, vel.clone().normalize(), vel.length() / 20, FluidCollisionMode.ALWAYS, true, 0.084, hitFilter);

        if (hits != null && (hits.getHitBlock() != null || hits.getHitEntity() != null)) {
            destroy();
        }

        drawLine(curPos, newPos);
        after();
    }

    public void explode() {
        double velocity = this.vel.length();
        Shot shot = new Shot(count);
        pos.getWorld().playSound(pos, Sound.ENTITY_FIREWORK_ROCKET_LAUNCH, 1, 0.3f);
        for (int i = 0; i < count; i++) {
            Vector dir = vel.clone();
            dir = Utils.randomizeDirection(dir, spread * velocity);
            
            pos.getWorld().spawnParticle(Particle.EXPLOSION_LARGE, pos, 5, 1, 1, 1);
            Rocket s = new Rocket(pos.clone(), dir, hitDamage, owner, shot, 1, tracer, cluster.getPower(), false);
            shot.add(s);
        }
        addShot(shot);
    }

    @Override
    public void hit(Entity e) {
        explode();
        if (e instanceof Damageable) {
            ((Damageable) e).damage(hitDamage, owner);
        }
        destroy();
    }
}
