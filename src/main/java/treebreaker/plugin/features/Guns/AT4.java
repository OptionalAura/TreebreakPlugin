/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package main.java.treebreaker.plugin.features.Guns;

import java.util.concurrent.ConcurrentHashMap;
import static main.java.treebreaker.plugin.features.Guns.Gun.shots;
import static main.java.treebreaker.plugin.features.Guns.Gun.tickCount;
import main.java.treebreaker.plugin.utils.Utils;
import org.bukkit.Bukkit;
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
public class AT4 extends Gun{
    public static final double velocity = 220;
    private static final double spread = 0.05;
    private static final ConcurrentHashMap<String, Integer> lastShot = new ConcurrentHashMap<>();
    public static final long cooldown = 20*10;
    public static double damage = 5;
    public static final Particle.DustOptions tracer = new Particle.DustOptions(Color.fromRGB(80, 80, 80), 1);
    
    public static void shoot(Player shooter, ItemStack item, String uuid) {
        if (!lastShot.containsKey(uuid) || tickCount - lastShot.get(uuid) > cooldown) {
            lastShot.put(uuid, tickCount);
            Location playerPos = shooter.getEyeLocation();
            playerPos.getWorld().playSound(playerPos, Sound.ENTITY_FIREWORK_ROCKET_LAUNCH, 1, 0.3f);
            Location newPos = playerPos.clone();
            Vector dir = newPos.getDirection();
            dir.setX(dir.getX() + (Math.random() - 0.5) * spread);
            dir.setY(dir.getY() + (Math.random() - 0.5) * spread);
            dir.setZ(dir.getZ() + (Math.random() - 0.5) * spread);
            dir.normalize();
            newPos.setDirection(dir);
            Particle.DustOptions dust = new Particle.DustOptions(Color.fromRGB(247, 55, 24), 3);
            newPos.getWorld().spawnParticle(Particle.REDSTONE, newPos.clone().add(newPos.getDirection().clone().multiply(-0.3)), 25, 0.3, 0.3, 0.3, dust);
            Shot shot = new Shot(1);
            Double power = Utils.getProperty(AT4_EXPLOSION_POWER_TAG, 5d);
            Rocket p = new Rocket(newPos, velocity, damage, shooter, shot, 25, tracer, power.floatValue());
            shot.add(p);
            shots.add(shot);
        }
    }
}
