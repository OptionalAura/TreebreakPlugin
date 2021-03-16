/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package main.java.treebreaker.plugin.features.Guns;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import static main.java.treebreaker.plugin.features.Guns.Gun.shots;
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
public class Sniper extends Gun {

    private static final ConcurrentHashMap<String, Integer> lastShot = new ConcurrentHashMap<>();
    private static final Particle.DustOptions tracer = new Particle.DustOptions(Color.fromRGB(80, 80, 80), 1);

    @Override
    public void shoot(Player shooter, ItemStack item, String uuid) {
        if (!lastShot.containsKey(uuid) || tickCount - lastShot.get(uuid) > getProperty("guns." + getName() + ".firerate", getDefaultFireRate())) {
            lastShot.put(uuid, tickCount);
            Location playerPos = shooter.getEyeLocation();
            playerPos.getWorld().playSound(playerPos, Sound.ENTITY_GENERIC_EXPLODE, 1, 0.5f);

            Shot shot = new Shot(1);
            Location newPos = playerPos.clone();
            Vector dir = newPos.getDirection();//.add(shooter.getVelocity());
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
            SniperBullet p = new SniperBullet(newPos, dir, getProperty("guns." + getName() + ".damage", getDefaultFireRate()), shooter, shot, 25, tracer);
            newPos.getWorld().spawnParticle(Particle.EXPLOSION_LARGE, newPos.clone().add(newPos.getDirection().clone().multiply(0.2)), 1);
            shot.add(p);
            shots.add(shot);
        }
    }

    @Override
    public String getName() {
        return "Sniper";
    }

    @Override
    public List<String> getLore() {
        List<String> itemLore = new ArrayList<>();
        if (getProperty("guns." + getName() + ".firerate", 0d) <= 0) {
            itemLore.add(ChatColor.WHITE + "Fire rate: ∞ rpm" + ChatColor.RESET);
        } else {
            itemLore.add(ChatColor.WHITE + "Fire rate: " + 60 / (getProperty("guns." + getName() + ".firerate", getDefaultFireRate()) / 20) + " rpm" + ChatColor.RESET);
        }
        itemLore.add(ChatColor.WHITE + "Damage: " + getProperty("guns." + getName() + ".damage", getDefaultDamage()) / 2 + ChatColor.RED + " ❤" + ChatColor.RESET);
        itemLore.add(ChatColor.WHITE + "Velocity: " + getProperty("guns." + getName() + ".velocity", getDefaultVelocity()) + " m/s" + ChatColor.RESET);
        itemLore.add(ChatColor.WHITE + "Spread: " + getProperty("guns." + getName() + ".spread", getDefaultSpread()) + ChatColor.RESET);

        itemLore.add(ChatColor.GRAY + "Shoots big stuff but slow" + ChatColor.RESET);
        return itemLore;
    }

    @Override
    void init(FileConfiguration settingsConfig) {

    }
    @Override
    void term(FileConfiguration settingsConfig) {

    }

    @Override
    public double getDefaultVelocity() {
        return 853d;
    }

    @Override
    public double getDefaultFireRate() {
        return 20*3;
    }

    @Override
    public double getDefaultDamage() {
        return 15;
    }

    @Override
    public double getDefaultSpread() {
        return 0;
    }
}
