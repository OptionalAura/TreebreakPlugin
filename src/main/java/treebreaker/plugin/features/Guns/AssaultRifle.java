/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package main.java.treebreaker.plugin.features.Guns;

import java.util.concurrent.ConcurrentHashMap;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

/**
 *
 * @author dsato
 */
public class AssaultRifle extends Gun {

    private static final ConcurrentHashMap<String, Double> lastShot = new ConcurrentHashMap<>();
    public static final double velocity = 910d;
    private static final double spread = 0.03d;
    public static final double cooldown = 20d/(35d/3d);
    public static final double damage = 2d;
    public static final Particle.DustOptions tracer = new Particle.DustOptions(Color.fromRGB(200, 200, 200), 1);
    public static void shoot(Player shooter, ItemStack item, String uuid) {
        if (!lastShot.containsKey(uuid) || tickCount - lastShot.get(uuid) > cooldown) {
            lastShot.put(uuid, Double.valueOf(tickCount));
            Location playerPos = shooter.getEyeLocation();
            Shot shot = new Shot(1);
            playerPos.getWorld().playSound(playerPos, Sound.ENTITY_GENERIC_EXPLODE, 0.6f, 1.4f);
            Location newPos = playerPos.clone();
            Vector dir = newPos.getDirection();
            dir.setX(dir.getX() + (Math.random() - 0.5) * spread);
            dir.setY(dir.getY() + (Math.random() - 0.5) * spread);
            dir.setZ(dir.getZ() + (Math.random() - 0.5) * spread);
            dir.normalize();
            newPos.setDirection(dir);
            Particle.DustOptions dust = new Particle.DustOptions(Color.fromRGB(255, 226, 163), 1);
            newPos.getWorld().spawnParticle(Particle.REDSTONE, newPos.clone().add(newPos.getDirection().clone().multiply(0.2)), 3, 0, 0, 0, dust);
            Projectile p = new Projectile(newPos, velocity, damage, shooter, shot, 1, tracer);
            shot.add(p);
            shots.add(shot);
        }
    }
}
