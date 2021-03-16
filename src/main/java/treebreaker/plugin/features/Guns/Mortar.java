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
import static main.java.treebreaker.plugin.features.Guns.AT4.tracer;
import static main.java.treebreaker.plugin.features.Guns.Gun.shots;
import main.java.treebreaker.plugin.utils.Utils;
import static main.java.treebreaker.plugin.utils.Utils.getProperty;
import org.bukkit.ChatColor;
import org.bukkit.Location;
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
public class Mortar extends Gun {

    @Override
    public double getDefaultVelocity() {
        return 73.0d;
    }

    public double getChargeVelocity(double dist) {
        if (dist > 1700) {
            return 250;
        }
        if (dist > 1340) {
            return 224;
        }
        if (dist > 1070) {
            return 195;
        }
        if (dist > 780) {
            return 162;
        }
        if (dist > 580) {
            return 137;
        }
        if (dist > 390) {
            return 110;
        }
        return 73;

    }

    public static double getAngleToHit(double vel, double dist, double y) {
        double g = getProperty("world.physics.gravity", -9.81d);
        //double plus = = vel + Math.sqrt(vel * vel - )
        double plus = vel * vel + Math.sqrt(vel * vel * vel * vel - g * (g * dist * dist + 2 * y * vel * vel));
//double minus = v*v + Math.sqrt(v*v*v*v-g*(g*x*x+2*y*v*v));
        return -(Math.atan(plus / (g * dist)));
    }

    @Override
    public double getDefaultFireRate() {
        return 80;
    }

    @Override
    public double getDefaultDamage() {
        return 2;
    }

    @Override
    public double getDefaultSpread() {
        return 0.02;
    }

    @Override
    public String getName() {
        return "Mortar";
    }

    private static final ConcurrentHashMap<String, Integer> lastShot = new ConcurrentHashMap<>();

    @Override
    public void shoot(Player shooter, ItemStack item, String uuid) {
        if (!lastShot.containsKey(uuid) || tickCount - lastShot.get(uuid) > getProperty("guns." + getName() + ".firerate", getDefaultFireRate())) {
            lastShot.put(uuid, tickCount);
            if (targets.containsKey(uuid)) {

                Location target = targets.get(uuid).clone();
                Vector targetRelative = new Vector(target.getX() - shooter.getEyeLocation().getX(), 0, target.getZ() - shooter.getEyeLocation().getZ());
                double dist = Math.sqrt(Math.pow(target.getX() - shooter.getEyeLocation().getX(), 2) + Math.pow(target.getZ() - shooter.getEyeLocation().getZ(), 2));
                double vel = getChargeVelocity(dist);
                Double angleY = getAngleToHit(vel, dist, target.getY() - shooter.getEyeLocation().getY());

                Double angleXZ = Math.atan2(targetRelative.getX(), targetRelative.getZ());
                double y = vel * Math.sin(angleY);
                double xz = vel * Math.cos(angleY);
                double z = xz * Math.cos(angleXZ);
                double x = xz * Math.sin(angleXZ);
                Vector dir = new Vector(x, y, z);
                double spread = getProperty("guns." + getName() + ".spread", getDefaultSpread());
                dir.setX(dir.getX() + (Math.random() - 0.5) * spread * vel);
                dir.setY(dir.getY() + (Math.random() - 0.5) * spread * vel);
                dir.setZ(dir.getZ() + (Math.random() - 0.5) * spread * vel);
                Vector playerVel = shooter.getVelocity().clone();
                if (((Entity) shooter).isOnGround()) {
                    playerVel.setY(0);
                }
                dir.add(playerVel);
                Shot shot = new Shot(1);
                Double power = Utils.getProperty("guns." + getName() + ".blastPower", 5d);
                Rocket p = new Rocket(shooter.getEyeLocation(), dir, getProperty("guns." + getName() + ".damage", getDefaultDamage()), shooter, shot, 25, tracer, power.floatValue());
                shot.add(p);
                shots.add(shot);
                shooter.getLocation().getWorld().playSound(shooter.getLocation(), Sound.ENTITY_GENERIC_EXPLODE, 1, 0.5f);
                shooter.getLocation().getWorld().playSound(shooter.getLocation(), Sound.ENTITY_FIREWORK_ROCKET_LAUNCH, 1, 0.7f);
            } else {
                shooter.sendMessage("You do not have a target set. Left-click to set a target");
            }
        }
    }

    private ConcurrentHashMap<String, Location> targets = new ConcurrentHashMap<>();

    public void setTarget(String UUID, Location target) {
        targets.put(UUID, target);
    }

    @Override
    void init(FileConfiguration settingsConfig) {
        addProperty(settingsConfig, "blastPower", 10d);
    }

    @Override
    void term(FileConfiguration settingsConfig) {
        Set<String> keySet = customProperties.keySet();
        for (String key : keySet) {
            settingsConfig.set(key, getProperty(key, customProperties.get(key)));
        }
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
        itemLore.add(ChatColor.WHITE + "Blast Radius: " + powerToRadius(getProperty("guns." + getName() + ".blastPower", 10d)) + ChatColor.RESET);

        itemLore.add(ChatColor.GRAY + "Shoots explody stuff but high" + ChatColor.RESET);
        return itemLore;
    }

    @Override
    public int versionCode() {
        int result = 17;
        result = result * 31 + getName().hashCode();
        result = result * 31 + doubleToIntCode(getProperty("guns." + getName() + ".velocity", getDefaultVelocity()));
        result = result * 31 + doubleToIntCode(getProperty("guns." + getName() + ".firerate", getDefaultFireRate()));
        result = result * 31 + doubleToIntCode(getProperty("guns." + getName() + ".damage", getDefaultDamage()));
        result = result * 31 + doubleToIntCode(getProperty("guns." + getName() + ".spread", getDefaultSpread()));
        result = result * 31 + doubleToIntCode(getProperty("guns." + getName() + ".blastPower", 10d));
        return result;
    }

    private double powerToRadius(double power) {
        return Math.floor(1.3 * power / 0.225) * 0.3;
    }
}
