/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package main.java.treebreaker.plugin.features.Guns;

import java.util.function.Predicate;
import main.java.treebreaker.plugin.utils.Utils;
import org.bukkit.Color;
import org.bukkit.FluidCollisionMode;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.entity.Damageable;
import org.bukkit.entity.Entity;
import org.bukkit.util.RayTraceResult;
import org.bukkit.util.Vector;

/**
 *
 * @author dsato
 */
public class Projectile {

    protected boolean alive = true;
    protected final Location pos;
    protected final Vector vel;
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
        if (hits == null) {
            //Bukkit.getConsoleSender().sendMessage("Hits was null");
        }
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

    /**
     * Checks hit entities. Returns null if it does not hit an entity
     *
     * @param passthrough Whether it should detect hits through blocks
     * @return
     */
    public final RayTraceResult checkHits(boolean passthrough) {
        RayTraceResult rtr = pos.getWorld().rayTrace(pos, vel.clone().normalize(), vel.length() / 20, FluidCollisionMode.ALWAYS, passthrough, 0, hitFilter);
        return rtr;
    }

    /**
     * Draws a line between curPos and newPos.
     *
     * @param curPos
     * @param newPos
     */
    public void drawLine(Location curPos, Location newPos) {
        //if (curPos.isWorldLoaded()) {
            double dist = curPos.distance(newPos);
            Location particleLocation = curPos.clone();
            Vector dir = newPos.clone().subtract(curPos).toVector().normalize();
            //Bukkit.getConsoleSender().sendMessage("Be-for-loop " + dist);
            for (int i = 0; i < Math.ceil(dist); i++) {
                //Bukkit.getConsoleSender().sendMessage("Drawing line of length " + dist);
                particleLocation.add(dir);
                curPos.getWorld().spawnParticle(Particle.REDSTONE, particleLocation.getX(), particleLocation.getY(), particleLocation.getZ(), particleDensity, 0, 0, 0, 0, tracer, true);
            }
        //}
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
}
