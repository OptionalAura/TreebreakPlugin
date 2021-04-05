/*
 * Copyright (C) 2021 Daniel Allen
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General
 * Public License as published by the Free Software Foundation, either version 3 of the License, or (at your
 * option) any later version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR
 * PURPOSE.  See the GNU General Public License for more details.
 *
 * You should have received a copy of the
 * GNU General Public License along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package main.java.treebreaker.plugin.features.guns;

import org.bukkit.*;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import static main.java.treebreaker.plugin.utils.Utils.getProperty;
import static main.java.treebreaker.plugin.utils.Utils.randomizeDirection;

/**
 * @author Daniel Allen
 */
public class Hellstorm extends Gun implements Targeting {
    private static final Hellstorm instance = new Hellstorm();

    private static final ConcurrentHashMap<String, Long> lastShot = new ConcurrentHashMap<>();
    private static final Particle.DustOptions tracer = new Particle.DustOptions(Color.fromRGB(80, 80, 80), 4);
    private static final Particle.DustOptions miniTracer = new Particle.DustOptions(Color.fromRGB(80, 80, 80), 1);

    public static Hellstorm getInstance() {
        return instance;// != null ? instance : new Hellstorm();
    }

    public double getDefaultCount() {
        return 25;
    }

    @Override
    public double getDefaultVelocity() {
        return 1000;
    }

    @Override
    public double getDefaultFireRate() {
        return 20 * 60;
    }

    @Override
    public double getDefaultDamage() {
        return 1000;
    }

    @Override
    public double getDefaultSpread() {
        return 0.01;
    }

    @Override
    public String getName() {
        return "Hellstorm";
    }

    public double getDefaultPower() {
        return 6;
    }

    public double getDefaultHeight() {
        return 400;
    }

    public static double getAngleToHit(double vel, double dist, double y) {
        double g = getProperty("world.physics.gravity", -9.81d);
        double plus = vel * vel + Math.sqrt(vel * vel * vel * vel - g * (g * dist * dist + 2 * y * vel * vel));
        return -(Math.atan(plus / (g * dist)));
    }

    @Override
    public void shoot(Player shooter, ItemStack item, String uuid) {
        if (!lastShot.containsKey(uuid) || tickCount - lastShot.get(uuid) > getProperty("guns." + getName() + ".firerate", getDefaultFireRate())) {
            if (targets.containsKey(uuid)) {
                lastShot.put(uuid, tickCount);
                Location target = targets.get(uuid).clone();
                Vector targetRelative = new Vector(target.getX() - shooter.getEyeLocation().getX(), 0, target.getZ() - shooter.getEyeLocation().getZ());
                double dist = Math.sqrt(Math.pow(target.getX() - shooter.getEyeLocation().getX(), 2) + Math.pow(target.getZ() - shooter.getEyeLocation().getZ(), 2));
                double vel = getProperty("guns." + getName() + ".velocity", getDefaultVelocity());
                double angleY = getAngleToHit(vel, dist, target.getY() - shooter.getEyeLocation().getY());
                if (!Double.isNaN(angleY)) {
                    double angleXZ = Math.atan2(targetRelative.getX(), targetRelative.getZ());
                    double y = vel * Math.sin(angleY);
                    double xz = vel * Math.cos(angleY);
                    double z = xz * Math.cos(angleXZ);
                    double x = xz * Math.sin(angleXZ);
                    Vector dir = new Vector(x, y, z);
                    double spread = 0;//getProperty("guns." + getName() + ".spread", getDefaultSpread()) * vel;
                    dir = randomizeDirection(dir, spread);
                    Vector playerVel = shooter.getVelocity().clone();
                    if (((Entity) shooter).isOnGround()) {
                        playerVel.setY(0);
                    }
                    dir.add(playerVel);
                    Shot shot = new Shot(1);
                    Rocket munition = new Rocket(null, null, 10, shooter, null, 1, miniTracer, getProperty("guns." + getName() + ".blastPower", getDefaultPower()).floatValue());
                    HellstormMunition p = new HellstormMunition(shooter.getEyeLocation(), dir, getProperty("guns." + getName() + ".damage", getDefaultDamage()), shooter, shot, 25, tracer, getProperty("guns." + getName() + ".count", getDefaultCount()).intValue(), getProperty("guns." + getName() + ".height", getDefaultHeight()), getProperty("guns." + getName() + ".munitionSpread", getDefaultSpread()), munition);
                    shot.add(p);
                    Gun.addShot(shot);
                    shooter.getLocation().getWorld().playSound(shooter.getLocation(), Sound.ENTITY_GENERIC_EXPLODE, 1, 0.5f);
                    shooter.getLocation().getWorld().playSound(shooter.getLocation(), Sound.ENTITY_FIREWORK_ROCKET_LAUNCH, 2, 0.4f);
                } else {
                    shooter.sendMessage("Target is too far away!");
                }
            } else {
                shooter.sendMessage("You do not have a target set. Left-click to set a target");
            }
        }
    }
    @Override
    void init(FileConfiguration settingsConfig) {
        addProperty(settingsConfig, "count", getDefaultCount());
        addProperty(settingsConfig, "munitionSpread", getDefaultSpread());
        addProperty(settingsConfig, "blastPower", getDefaultPower());
        addProperty(settingsConfig, "height", getDefaultHeight());
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
        result = result * 31 + doubleToIntCode(getProperty("guns." + getName() + ".blastPower", getDefaultPower()));
        result = result * 31 + doubleToIntCode(getProperty("guns." + getName() + ".count", getDefaultCount()));
        return result;
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
        itemLore.add(ChatColor.WHITE + "Count: " + getProperty("guns." + getName() + ".count", getDefaultCount()) + ChatColor.RESET);
        itemLore.add(ChatColor.WHITE + "Blast Power: " + getProperty("guns." + getName() + ".blastPower", getDefaultPower()) + ChatColor.RESET);
        itemLore.add(ChatColor.GRAY + "Shoots a lot of explody stuff out of another explody thing" + ChatColor.RESET);
        return itemLore;
    }

    @Override
    public void setTarget(String UUID, Location target) {
        targets.put(UUID, target);
    }
}
