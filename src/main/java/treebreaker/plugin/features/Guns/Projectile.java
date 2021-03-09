/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package main.java.treebreaker.plugin.features.Guns;

import java.util.function.Predicate;
import org.bukkit.Color;
import org.bukkit.FluidCollisionMode;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.entity.Damageable;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.util.RayTraceResult;
import org.bukkit.util.Vector;

/**
 *
 * @author dsato
 */
public class Projectile {

    private boolean alive;
    private final Location pos;
    private final Vector vel;
    private final double hitDamage;
    private final Entity owner;
    private final Shot parent;
    
    private long lifetime = 0;
    
    public Projectile(Location origin, double speed, double damage, Entity owner, Shot parent){
        this.pos = origin;
        this.vel = pos.getDirection().multiply(speed);
        this.hitDamage = damage;
        this.owner = owner;
        this.parent = parent;
        
    }
    
    
    /**
     * Called every tick on this projectile
     */
    public final void tick() {
        lifetime++;
        before();
        RayTraceResult hits = checkHits(true);
        Location curPos = pos;
        move();
        Location newPos = pos;
        if (hits.getHitBlock() != null || hits.getHitEntity() != null) {
            Vector p = hits.getHitPosition();
            newPos.setX(p.getX());
            newPos.setY(p.getY());
            newPos.setZ(p.getZ());
            destroy();
            if(hits.getHitEntity() != null){
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
        vel.add(new Vector(0, -9.81 / 20, 0));
    }
    
    Predicate<Entity> hitFilter = new Predicate<Entity>() {
        @Override
        public boolean test(Entity t) {
            return !(t.equals(owner) && lifetime < 20);
        }
    };
    
    /**
     * Checks hit entities.Returns null if it does not hit an entity
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
    public final static void drawLine(Location curPos, Location newPos) {
        if (curPos.isWorldLoaded() && curPos.getWorld().equals(newPos.getWorld())) {
            double dist = curPos.distance(newPos);
            Location particleLocation = curPos;
            Particle.DustOptions dust = new Particle.DustOptions(Color.fromRGB(255, 93, 93), 1);
            for (int i = 0; i < dist; i++) {
                particleLocation.add(curPos.getDirection());
                curPos.getWorld().spawnParticle(Particle.REDSTONE, particleLocation.getX(), particleLocation.getY(), particleLocation.getZ(), 0, 0, 0, 0, dust);
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
     * Does nothing, but is called at the end of the tick() method. Can be
     * used in class implementations
     */
    public void after() {

    }
    public boolean isAlive(){
        return this.alive;
    }
    public void destroy(){
        alive = false;
        parent.decrement();
    }
}
