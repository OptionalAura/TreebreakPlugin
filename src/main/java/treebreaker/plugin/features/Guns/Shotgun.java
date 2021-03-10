/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package main.java.treebreaker.plugin.features.Guns;

import java.util.concurrent.ConcurrentHashMap;
import static main.java.treebreaker.plugin.features.Guns.Gun.tickCount;
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
public class Shotgun extends Gun {

    private static final ConcurrentHashMap<String, Integer> lastShot = new ConcurrentHashMap<>();
    private static final int count = 82;
    public static final double velocity = 475.5;
    private static final double spread = 0.25;
    public static final long cooldown = 20 * 1;
    public static final double damage = 5;
    
    public static void shoot(Player shooter, ItemStack item, String uuid) {
        if (!lastShot.containsKey(uuid) || tickCount - lastShot.get(uuid) > cooldown) {
            lastShot.put(uuid, tickCount);
            Location playerPos = shooter.getEyeLocation();
            Shot shot = new Shot(count);
            playerPos.getWorld().playSound(playerPos, Sound.ENTITY_GENERIC_EXPLODE, 1, 1);
            for (int i = 0; i < count; i++) {
                Location newPos = playerPos.clone();
                Vector dir = newPos.getDirection();
                dir.setX(dir.getX() + (Math.random() - 0.5) * spread);
                dir.setY(dir.getY() + (Math.random() - 0.5) * spread);
                dir.setZ(dir.getZ() + (Math.random() - 0.5) * spread);
                dir.normalize();
                newPos.setDirection(dir);
                Particle.DustOptions dust = new Particle.DustOptions(Color.fromRGB(255, 226, 163), 1);
                newPos.getWorld().spawnParticle(Particle.REDSTONE, newPos.clone().add(newPos.getDirection().clone().multiply(0.2)), 15, 0.1, 0.1, 0.1, dust);
                Projectile p = new Projectile(newPos, velocity, damage, shooter, shot);
                shot.add(p);
            }
            shots.add(shot);
        }
    }
}
