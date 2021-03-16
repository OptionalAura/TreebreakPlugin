/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package main.java.treebreaker.plugin.features.Guns;

import org.bukkit.Color;
import org.bukkit.FluidCollisionMode;
import org.bukkit.Location;
import org.bukkit.Material;
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
public class Rocket extends Projectile {

    private float explosionPower;

    public Rocket(Location newPos, Vector vel, double damage, Player shooter, Shot shot, int i, Particle.DustOptions tracer, float power) {
        super(newPos, vel, damage, shooter, shot, i);
        this.tracer = tracer;
        explosionPower = power;
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
        boolean hitBlock = true;
        while (hitBlock && isAlive()) {
            RayTraceResult hits = pos.getWorld().rayTrace(origin, vel.clone().normalize(), vel.length() / 20, FluidCollisionMode.ALWAYS, true, 0.084, hitFilter);

            hitBlock = false;
            if (hits != null && (hits.getHitBlock() != null || hits.getHitEntity() != null)) {
                Vector p = hits.getHitPosition();
                pos.setX(p.getX());
                pos.setY(p.getY());
                pos.setZ(p.getZ());
                if (hits.getHitEntity() != null) {
                    hit(hits.getHitEntity());
                } else if (hits.getHitBlock() != null) {
                    Material mat = hits.getHitBlock().getBlockData().getMaterial();
                    if (mat.isBlock()) {
                        if (mat.equals(Material.WATER)) {
                            Vector dir = vel.clone().normalize();
                            double angleIncidence = Math.abs(Math.asin(dir.getY()));
                            double targetAngle = 10*Math.PI/180;
                            //Bukkit.getConsoleSender().sendMessage("Hit water with angle " + angleIncidence);
                            if(angleIncidence < targetAngle || angleIncidence > Math.PI - targetAngle){
                                vel.setY(vel.getY()*-1);
                            } else {
                                destroy();
                            }
                        } else {
                            //Bukkit.getConsoleSender().sendMessage("Hit " + mat.toString() + " with hardness " + mat.getHardness());
                            if (mat.getHardness() < 0.4 && mat.getHardness() != -1) {
                                hits.getHitBlock().breakNaturally();
                                hitBlock = true;
                            } else {
                                if (lifetime > ((1.3*explosionPower/0.225)*0.3)/(vel.length()/20)) {
                                    Particle.DustOptions dust = new Particle.DustOptions(Color.fromRGB(150, 55, 55), 1);
                                    curPos.getWorld().spawnParticle(Particle.REDSTONE, newPos, 4, 0, 0, 0, dust);
                                    explode();
                                } else {
                                    destroy();
                                }
                            }
                        }
                    } else {
                        destroy();
                    }
                }
            }
        }
        drawLine(curPos, newPos);
        after();
    }

    public void explode() {
        pos.getWorld().createExplosion(pos, explosionPower, false, true, owner);
        destroy();
    }

    public void hit(Entity e) {
        explode();
        if (e instanceof Damageable) {
            ((Damageable) e).damage(hitDamage, owner);
        }
        destroy();
    }
}
