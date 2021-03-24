/*
 * Copyright (C) 2021 Daniel Allen
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package main.java.treebreaker.plugin.features.Guns;

import main.java.treebreaker.plugin.utils.Utils;
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
import static main.java.treebreaker.plugin.utils.Utils.setProperty;

/**
 *
 * @author Daniel Allen
 */
public class Shotgun extends Gun {

    private static final ConcurrentHashMap<String, Integer> lastShot = new ConcurrentHashMap<>();

    @Override
    public double getDefaultVelocity() {
        return 475.5d;
    }

    @Override
    public double getDefaultFireRate() {
        return 20d;
    }

    @Override
    public double getDefaultDamage() {
        return 5d;
    }

    @Override
    public double getDefaultSpread() {
        return 0.15d;
    }

    public double getDefaultCount() {
        return 82;
    }

    @Override
    public void shoot(Player shooter, ItemStack item, String uuid) {
        if (!lastShot.containsKey(uuid) || tickCount - lastShot.get(uuid) > getProperty("guns." + getName() + ".firerate", getDefaultFireRate())) {
            lastShot.put(uuid, tickCount);
            Location playerPos = shooter.getEyeLocation();
            int count = getProperty("guns." + getName() + ".count", getDefaultCount()).intValue();
            Shot shot = new Shot(count);
            playerPos.getWorld().playSound(playerPos, Sound.ENTITY_GENERIC_EXPLODE, 1, 1);
            double spread = getProperty("guns." + getName() + ".spread", getDefaultSpread());
            double velocity = getProperty("guns." + getName() + ".velocity", getDefaultVelocity());
            for (int i = 0; i < count; i++) {
                Location newPos = playerPos.clone();
                Vector dir = newPos.getDirection().multiply(velocity);
                dir = Utils.randomizeDirection(dir, velocity * spread);
                Vector playerVel = shooter.getVelocity().clone();
                if (((Entity) shooter).isOnGround()) {
                    playerVel.setY(0);
                }
                dir.add(playerVel);
                Particle.DustOptions dust = new Particle.DustOptions(Color.fromRGB(255, 226, 163), 1);
                newPos.getWorld().spawnParticle(Particle.REDSTONE, newPos.clone().add(newPos.getDirection().clone().multiply(0.5)), 5, 0.1, 0.1, 0.1, dust);
                Projectile p = new Projectile(newPos, dir, getProperty("guns." + getName() + ".damage", getDefaultDamage()), shooter, shot);
                shot.add(p);
            }
            addShot(shot);
        }
    }

    @Override
    public String getName() {
        return "Shotgun";
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
        itemLore.add(ChatColor.GRAY + "Shoots a lot of small stuff but slow" + ChatColor.RESET);
        return itemLore;
    }

    @Override
    public void addProperty(FileConfiguration settingsConfig, String key, Object def) {
        String str = "guns." + getName() + "." + key;
        Object val = settingsConfig.get(str, def);
        customProperties.put(str, val);
        setProperty(str, val);
    }

    @Override
    void init(FileConfiguration settingsConfig) {
        addProperty(settingsConfig, "count", getDefaultCount());
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
        result = result * 31 + doubleToIntCode(getProperty("guns." + getName() + ".count", getDefaultCount()));
        return result;
    }
}
