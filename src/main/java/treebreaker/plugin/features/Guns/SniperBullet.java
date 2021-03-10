/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package main.java.treebreaker.plugin.features.Guns;

import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.FluidCollisionMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.entity.Player;
import org.bukkit.util.RayTraceResult;
import org.bukkit.util.Vector;

/**
 *
 * @author dsato
 */
public class SniperBullet extends Projectile {

    public SniperBullet(Location newPos, double velocity, double damage, Player shooter, Shot shot, int i, Particle.DustOptions tracer) {
        super(newPos, velocity, damage, shooter, shot, i, tracer);

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
            RayTraceResult hits = pos.getWorld().rayTrace(origin, vel.clone().normalize(), vel.length() / 20, FluidCollisionMode.ALWAYS, true, 0.013, hitFilter);
            hitBlock = false;
            if (hits != null && (hits.getHitBlock() != null || hits.getHitEntity() != null)) {
                Vector p = hits.getHitPosition();
                pos.setX(p.getX());
                pos.setY(p.getY());
                pos.setZ(p.getZ());
                Particle.DustOptions dust = new Particle.DustOptions(Color.fromRGB(55, 55, 55), 1);
                curPos.getWorld().spawnParticle(Particle.REDSTONE, newPos, 4, 0, 0, 0, dust);
                if (hits.getHitEntity() != null) {
                    hit(hits.getHitEntity());
                } else if (hits.getHitBlock() != null) {
                    Material mat = hits.getHitBlock().getBlockData().getMaterial();
                    if (mat.isBlock()) {
                        if (mat.equals(Material.WATER)) {
                            Vector dir = vel.clone().normalize();
                            double angleIncidence = Math.abs(Math.asin(dir.getY()));
                            double targetAngle = 8 * Math.PI / 180;
                            //Bukkit.getConsoleSender().sendMessage("Hit water with angle " + angleIncidence);
                            if (angleIncidence < targetAngle || angleIncidence > Math.PI - targetAngle) {
                                vel.setY(vel.getY() * -1);
                            } else {
                                destroy();
                            }
                        } else {
                            //Bukkit.getConsoleSender().sendMessage("Hit " + mat.toString() + " with hardness " + mat.getHardness());
                            if (mat.getHardness() < 0.4) {
                                hits.getHitBlock().breakNaturally();
                                hitBlock = true;
                            } else if (mat.getHardness() < 0.7) {
                                hits.getHitBlock().breakNaturally();
                                destroy();
                            } else {
                                destroy();
                            }
                        }
                    } else {
                        destroy();
                    }
                }
                if (!isAlive()) {
                    newPos.setX(p.getX());
                    newPos.setY(p.getY());
                    newPos.setZ(p.getZ());
                }
            }
        }
        drawLine(curPos, newPos);
        after();
    }
}
