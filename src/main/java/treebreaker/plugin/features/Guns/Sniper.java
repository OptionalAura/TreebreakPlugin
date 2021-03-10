/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package main.java.treebreaker.plugin.features.Guns;

import java.util.concurrent.ConcurrentHashMap;
import static main.java.treebreaker.plugin.features.Guns.Gun.shots;
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
public class Sniper extends Gun {

    public static final double velocity = 853;
    private static final ConcurrentHashMap<String, Integer> lastShot = new ConcurrentHashMap<>();
    public static final long cooldown = 20*3;
    public static final double damage = 15;
    private static final Particle.DustOptions tracer = new Particle.DustOptions(Color.fromRGB(80, 80, 80), 1);
    public static void shoot(Player shooter, ItemStack item, String uuid) {
        
        if (!lastShot.containsKey(uuid) || tickCount - lastShot.get(uuid) > cooldown) {
            lastShot.put(uuid, tickCount);
            Location playerPos = shooter.getEyeLocation();
            playerPos.getWorld().playSound(playerPos, Sound.ENTITY_GENERIC_EXPLODE, 1, 0.5f);
            
            Shot shot = new Shot(1);
            Location newPos = playerPos.clone();
            SniperBullet p = new SniperBullet(newPos, velocity, damage, shooter, shot, 25, tracer);
            newPos.getWorld().spawnParticle(Particle.EXPLOSION_LARGE, newPos.clone().add(newPos.getDirection().clone().multiply(0.2)), 1);
            shot.add(p);
            shots.add(shot);
        }
    }
}
