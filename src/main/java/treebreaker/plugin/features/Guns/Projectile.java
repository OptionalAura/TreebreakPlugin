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
import org.bukkit.*;
import org.bukkit.entity.Damageable;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.util.RayTraceResult;
import org.bukkit.util.Vector;

import java.util.function.Predicate;

/**
 *
 * @author Daniel Allen
 */
public class Projectile {

    protected boolean alive = true;
    protected Location pos;
    protected Vector vel;
    protected final double hitDamage;
    protected final Entity owner;
    protected final Shot parent;

    protected long lifetime = 0;
    protected int particleDensity = 1;

    protected Particle.DustOptions tracer = new Particle.DustOptions(Color.fromRGB(255, 93, 93), 1);

    public Projectile(Location origin, double speed, double damage, Entity owner, Shot parent) {
        this.pos = origin;
        this.vel = pos.getDirection().multiply(speed);
        this.hitDamage = damage;
        this.owner = owner;
        this.parent = parent;

    }

    public Projectile(Location origin, Vector vel, double damage, Entity owner, Shot parent) {
        this.pos = origin;
        this.vel = vel;
        this.hitDamage = damage;
        this.owner = owner;
        this.parent = parent;

    }

    public Projectile(Location origin, double speed, double damage, Entity owner, Shot parent, int particles) {
        this.pos = origin;
        this.vel = pos.getDirection().multiply(speed);
        this.hitDamage = damage;
        this.owner = owner;
        this.parent = parent;
        this.particleDensity = particles;
    }

    public Projectile(Location origin, Vector vel, double damage, Entity owner, Shot parent, int particles) {
        this.pos = origin;
        this.vel = vel;
        this.hitDamage = damage;
        this.owner = owner;
        this.parent = parent;
        this.particleDensity = particles;
    }

    public Projectile(Location origin, double speed, double damage, Entity owner, Shot parent, int particles, Particle.DustOptions tracer) {
        this.pos = origin;
        this.vel = pos.getDirection().multiply(speed);
        this.hitDamage = damage;
        this.owner = owner;
        this.parent = parent;
        this.particleDensity = particles;
        this.tracer = tracer;
    }

    public Projectile(Location origin, Vector vel, double damage, Entity owner, Shot parent, int particles, Particle.DustOptions tracer) {
        this.pos = origin;
        this.vel = vel;
        this.hitDamage = damage;
        this.owner = owner;
        this.parent = parent;
        this.particleDensity = particles;
        this.tracer = tracer;
    }

    /**
     * Called every tick on this projectile
     */
    public void tick() {
        if (pos.getWorld() == null) {
            return;
        }
        lifetime++;
        before();

        RayTraceResult hits = pos.getWorld().rayTrace(pos, vel.clone().normalize(), vel.length() / 20, FluidCollisionMode.ALWAYS, true, 0, hitFilter);
        Location curPos = pos.clone();
        move();
        Location newPos = pos.clone();
        if (hits != null && (hits.getHitBlock() != null || hits.getHitEntity() != null)) {
            Vector p = hits.getHitPosition();
            newPos.setX(p.getX());
            newPos.setY(p.getY());
            newPos.setZ(p.getZ());
            Particle.DustOptions dust = new Particle.DustOptions(Color.fromRGB(55, 55, 55), 1);
            curPos.getWorld().spawnParticle(Particle.REDSTONE, newPos, 4, 0, 0, 0, dust);
            destroy();
            if (hits.getHitEntity() != null) {
                hit(hits.getHitEntity());
            }
        }
        drawLine(curPos, newPos);
        after();
    }

    /**
     * Moves this projectile to it's next location
     */
    public final void move() {
        pos.add(new Vector(vel.getX() / 20, vel.getY() / 20, vel.getZ() / 20));
        vel.add(new Vector(0, Utils.getProperty("world.physics.gravity", -9.81d) / 20, 0));
    }

    Predicate<Entity> hitFilter = new Predicate<Entity>() {
        @Override
        public boolean test(Entity t) {
            return !(t.equals(owner) && lifetime < 20);
        }
    };
    double drawDistSquared = (Bukkit.getViewDistance() * 16) * (Bukkit.getViewDistance() * 16);
    private static double xzDistSquared(Location a, Location b){
        return Math.pow(a.getX() - b.getX(), 2) + Math.pow(a.getZ() - b.getZ(), 2);
    }
    public static double weightedDistSquared(Location a, Location b, double xMod, double yMod, double zMod){
        return Math.pow((a.getX() - b.getX())*xMod, 2) + Math.pow((a.getY()-b.getY())*yMod, 2) + Math.pow((a.getZ() - b.getZ())*zMod, 2);
    }
    /**
     * Draws a line between curPos and newPos.
     *
     * @param curPos
     * @param newPos
     */
    public void drawLine(Location curPos, Location newPos) {
        if (curPos.getWorld() == null || curPos.getWorld() != newPos.getWorld()) {
            return;
        }
        boolean shouldDraw = false;
        double minDist = drawDistSquared;
        for (Player p : curPos.getWorld().getPlayers()) {
            if (weightedDistSquared(p.getLocation(), curPos, 1, 0.1, 1) < drawDistSquared || xzDistSquared(p.getLocation(), newPos) < drawDistSquared) {
                double min = Math.min(p.getLocation().distanceSquared(curPos), p.getLocation().distanceSquared(newPos));
                if (min < minDist) {
                    minDist = min;
                }
                shouldDraw = true;
                break;
            }
        }
        if (shouldDraw) {
            double dist = curPos.distance(newPos);
            Location particleLocation = curPos.clone();
            Vector dir = newPos.clone().subtract(curPos).toVector().normalize();
            for (int i = 0; i < Math.ceil(dist); i++) {
                particleLocation.add(dir);
                curPos.getWorld().spawnParticle(Particle.REDSTONE, particleLocation.getX(), particleLocation.getY(), particleLocation.getZ(), particleDensity, 0, 0, 0, 0, tracer, true);
            }
        }
    }

    /**
     * Called when a projectile collides with an entity
     *
     * @param e
     */
    public void hit(Entity e) {
        if (e instanceof Damageable) {
            ((Damageable) e).damage(hitDamage, owner);
        }
    }

    /**
     * Does nothing, but is called at the start of the tick() method. Can be
     * used in class implementations
     */
    public void before() {

    }

    /**
     * Does nothing, but is called at the end of the tick() method. Can be used
     * in class implementations
     */
    public void after() {

    }

    public boolean isAlive() {
        return this.alive;
    }

    public void destroy() {
        alive = false;
        parent.decrement();
    }

    public void setPos(Location pos) {
        if (this.pos == null) {
            this.pos = new Location(pos.getWorld(), pos.getX(), pos.getY(), pos.getZ());
        } else if (pos == null) {
            return;
        } else {
            this.pos.setX(pos.getX());
            this.pos.setY(pos.getY());
            this.pos.setZ(pos.getZ());
        }
    }

    public void setVel(Vector vel) {
        if (this.vel == null) {
            this.vel = new Vector(vel.getX(), vel.getY(), vel.getZ());
        } else if (vel == null) {
            return;
        } else {
            this.vel.setX(vel.getX());
            this.vel.setY(vel.getY());
            this.vel.setZ(vel.getZ());
        }
    }
}
