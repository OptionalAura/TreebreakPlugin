/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package main.java.treebreaker.plugin.features.Guns;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import static main.java.treebreaker.plugin.features.Guns.Gun.tickCount;
import static main.java.treebreaker.plugin.utils.Utils.getProperty;
import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

/**
 *
 * @author dsato
 */
public class AssaultRifle extends Gun {

    private static final ConcurrentHashMap<String, Double> lastShot = new ConcurrentHashMap<>();
    @Override
    public double getDefaultVelocity() {
        return 910d;
    }

    @Override
    public double getDefaultFireRate() {
        return 20d/(35d/3d);
    }

    @Override
    public double getDefaultDamage() {
        return 2d;
    }

    @Override
    public double getDefaultSpread() {
        return 0.03d;
    }

    @Override
    public String getName() {
        return "Assault Rifle";
    }
    public static final Particle.DustOptions tracer = new Particle.DustOptions(Color.fromRGB(200, 200, 200), 1);
    @Override
    public void shoot(Player shooter, ItemStack item, String uuid) {
        if (!lastShot.containsKey(uuid) || tickCount - lastShot.get(uuid) > getProperty("guns." + getName() + ".firerate", getDefaultFireRate())) {
            lastShot.put(uuid, Double.valueOf(tickCount));
            Location playerPos = shooter.getEyeLocation();
            Shot shot = new Shot(1);
            playerPos.getWorld().playSound(playerPos, Sound.ENTITY_GENERIC_EXPLODE, 0.6f, 1.4f);
            Location newPos = playerPos.clone();
            Vector dir = newPos.getDirection().add(shooter.getVelocity());
            double spread = getProperty("guns." + getName() + ".spread", getDefaultSpread());
            dir.setX(dir.getX() + (Math.random() - 0.5) * spread);
            dir.setY(dir.getY() + (Math.random() - 0.5) * spread);
            dir.setZ(dir.getZ() + (Math.random() - 0.5) * spread);
            dir.normalize();
            newPos.setDirection(dir);
            dir.multiply(getProperty("guns." + getName() + ".velocity", getDefaultFireRate()));
            Vector playerVel = shooter.getVelocity().clone();
            if(((Entity)shooter).isOnGround()){
                playerVel.setY(0);
            }
            dir.add(playerVel);
            Particle.DustOptions dust = new Particle.DustOptions(Color.fromRGB(255, 226, 163), 1);
            newPos.getWorld().spawnParticle(Particle.REDSTONE, newPos.clone().add(newPos.getDirection().clone().multiply(0.2)), 3, 0, 0, 0, dust);
            Projectile p = new Projectile(newPos, dir, getProperty("guns." + getName() + ".damage", getDefaultFireRate()), shooter, shot, 1, tracer);
            shot.add(p);
            shots.add(shot);
        }
    }
@Override
    public List<String> getLore() {
        List<String> itemLore = new ArrayList<>();
        if(getProperty("guns." + getName() + ".firerate", 0d) <= 0){
            itemLore.add(ChatColor.WHITE + "Fire rate: ∞ rpm" + ChatColor.RESET);
        } else {
            itemLore.add(ChatColor.WHITE + "Fire rate: " + 60 / (getProperty("guns." + getName() + ".firerate", getDefaultFireRate()) / 20) + " rpm" + ChatColor.RESET);
        }
        itemLore.add(ChatColor.WHITE + "Damage: " + getProperty("guns." + getName() + ".damage", getDefaultDamage()) / 2 + ChatColor.RED + " ❤" + ChatColor.RESET);
        itemLore.add(ChatColor.WHITE + "Velocity: " + getProperty("guns." + getName() + ".velocity", getDefaultVelocity()) + " m/s" + ChatColor.RESET);
        itemLore.add(ChatColor.WHITE + "Spread: " + getProperty("guns." + getName() + ".spread", getDefaultSpread()) + ChatColor.RESET);
        itemLore.add(ChatColor.GRAY + "Shoots small stuff but fast" + ChatColor.RESET);
        return itemLore;
    }
    @Override
    void init(FileConfiguration settingsConfig) {

    }
    @Override
    void term(FileConfiguration settingsConfig) {

    }
}
