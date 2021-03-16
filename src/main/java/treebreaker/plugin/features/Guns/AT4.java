/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package main.java.treebreaker.plugin.features.Guns;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import static main.java.treebreaker.plugin.features.Guns.Gun.shots;
import static main.java.treebreaker.plugin.features.Guns.Gun.tickCount;
import main.java.treebreaker.plugin.utils.Utils;
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
public class AT4 extends Gun {

    private static final ConcurrentHashMap<String, Integer> lastShot = new ConcurrentHashMap<>();
    public static final Particle.DustOptions tracer = new Particle.DustOptions(Color.fromRGB(80, 80, 80), 1);

    public AT4() {

    }

    @Override
    public void shoot(Player shooter, ItemStack item, String uuid) {
        if (!lastShot.containsKey(uuid) || tickCount - lastShot.get(uuid) > getProperty("guns." + getName() + ".firerate", getDefaultFireRate())) {
            lastShot.put(uuid, tickCount);
            Location playerPos = shooter.getEyeLocation();
            playerPos.getWorld().playSound(playerPos, Sound.ENTITY_FIREWORK_ROCKET_LAUNCH, 1, 0.3f);
            Location newPos = playerPos.clone();
            Vector dir = newPos.getDirection().add(shooter.getVelocity());
            double spread = getProperty("guns." + getName() + ".spread", getDefaultSpread());
            dir.setX(dir.getX() + (Math.random() - 0.5) * spread);
            dir.setY(dir.getY() + (Math.random() - 0.5) * spread);
            dir.setZ(dir.getZ() + (Math.random() - 0.5) * spread);
            dir.normalize();
            newPos.setDirection(dir);
            dir.multiply(getProperty("guns." + getName() + ".velocity", getDefaultVelocity()));
            Vector playerVel = shooter.getVelocity().clone();
            if (((Entity) shooter).isOnGround()) {
                playerVel.setY(0);
            }
            dir.add(playerVel);
            Particle.DustOptions dust = new Particle.DustOptions(Color.fromRGB(247, 55, 24), 3);
            newPos.getWorld().spawnParticle(Particle.REDSTONE, newPos.clone().add(newPos.getDirection().clone().multiply(-0.3)), 25, 0.3, 0.3, 0.3, dust);
            Shot shot = new Shot(1);
            Double power = Utils.getProperty("guns." + getName() + ".blastPower", 5d);
            Rocket p = new Rocket(newPos, dir, getProperty("guns." + getName() + ".damage", getDefaultFireRate()), shooter, shot, 25, tracer, power.floatValue());
            shot.add(p);
            shots.add(shot);
        }
    }

    @Override
    public double getDefaultVelocity() {
        return 220;
    }

    @Override
    public double getDefaultFireRate() {
        return 20 * 10;
    }

    @Override
    public double getDefaultDamage() {
        return 0;
    }

    @Override
    public double getDefaultSpread() {
        return 0.05;
    }

    @Override
    public String getName() {
        return "AT4";
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
        itemLore.add(ChatColor.WHITE + "Blast Radius: " + powerToRadius(getProperty("guns." + getName() + ".blastPower", 5d)) + ChatColor.RESET);

        itemLore.add(ChatColor.GRAY + "Shoots explody stuff but slow" + ChatColor.RESET);
        return itemLore;
    }

    private double powerToRadius(double power) {
        return Math.floor(1.3 * power / 0.225) * 0.3;
    }

    @Override
    void init(FileConfiguration settingsConfig) {
        addProperty(settingsConfig, "blastPower", 5d);
    }

    @Override
    void term(FileConfiguration settingsConfig) {
        Set<String> keySet = customProperties.keySet();
        for (String key : keySet) {
            settingsConfig.set(key, getProperty(key, customProperties.get(key)));
        }
    }

    @Override
    public int versionCode() {
        int result = 17;
        result = result * 31 + getName().hashCode();
        result = result * 31 + doubleToIntCode(getProperty("guns." + getName() + ".velocity", getDefaultVelocity()));
        result = result * 31 + doubleToIntCode(getProperty("guns." + getName() + ".firerate", getDefaultFireRate()));
        result = result * 31 + doubleToIntCode(getProperty("guns." + getName() + ".damage", getDefaultDamage()));
        result = result * 31 + doubleToIntCode(getProperty("guns." + getName() + ".spread", getDefaultSpread()));
        result = result * 31 + doubleToIntCode(getProperty("guns." + getName() + ".blastPower", 5d));
        return result;
    }
}
