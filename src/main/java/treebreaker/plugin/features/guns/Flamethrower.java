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

import main.java.treebreaker.plugin.Main;
import org.bukkit.*;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

import static main.java.treebreaker.plugin.utils.Utils.getProperty;

public class Flamethrower extends Gun {
    private static final Flamethrower instance = new Flamethrower();
    private static final Particle.DustOptions tracer = new Particle.DustOptions(Color.fromRGB(255, 131, 22), 3);
    private static final ConcurrentHashMap<String, Long> lastShot = new ConcurrentHashMap<>();

    public static Flamethrower getInstance() {
        return instance;// != null ? instance : new AT4();
    }

    @Override
    public double getDefaultVelocity() {
        return 25;
    }

    @Override
    public double getDefaultFireRate() {
        return 10;
    }

    @Override
    public double getDefaultDamage() {
        return 1;
    }

    @Override
    public double getDefaultSpread() {
        return 0.4;
    }

    @Override
    public void shoot(Player shooter, ItemStack item, String uuid) {
        boolean isShooting = (lastShot.containsKey(uuid) && tickCount - lastShot.get(uuid) < getProperty("guns." + getName() + ".firerate", getDefaultFireRate()) && shooter.getInventory().getItemInMainHand().equals(item));
        lastShot.put(uuid, tickCount);
        if (!isShooting) {
            Bukkit.getScheduler().runTaskTimerAsynchronously(Main.thisPlugin, new Runnable() {
                /**
                 * When an object implementing interface {@code Runnable} is used
                 * to create a thread, starting the thread causes the object's
                 * {@code run} method to be called in that separately executing
                 * thread.
                 * <p>
                 * The general contract of the method {@code run} is that it may
                 * take any action whatsoever.
                 *
                 * @see Thread#run()
                 */
                @Override
                public void run() {
                    if (!doFire(shooter, item, uuid)) {
                        Thread.currentThread().interrupt();
                    }
                }
            }, 0, 1);
        }
    }

    private boolean doFire(Player shooter, ItemStack item, String uuid) {
        if (lastShot.containsKey(uuid) && tickCount - lastShot.get(uuid) < getProperty("guns." + getName() + ".firerate", getDefaultFireRate()) && shooter.getInventory().getItemInMainHand().equals(item)) {
            Location playerPos = shooter.getEyeLocation().subtract(0, 1, 0);
            Shot shot = new Shot(1);
            //playerPos.getWorld().playSound(playerPos, Sound.ENTITY_GENERIC_EXPLODE, 0.6f, 1.4f);
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
            if (((Entity) shooter).isOnGround()) {
                playerVel.setY(0);
            }
            dir.add(playerVel);
            Flame f = new Flame(newPos, dir, getProperty("guns." + getName() + ".damage", getDefaultFireRate()), shooter, shot, 2, tracer);
            shot.add(f);
            Gun.addShot(shot);
            return true;
        }
        return false;
    }

    @Override
    void init(FileConfiguration settingsConfig) {

    }

    @Override
    void term(FileConfiguration settingsConfig) {

    }

    @Override
    public String getName() {
        return "Flamethrower";
    }

    @Override
    public List<String> getLore() {
        List<String> itemLore = new ArrayList<>();
        if (getProperty("guns." + getName() + ".firerate", 0d) <= 0) {
            itemLore.add(ChatColor.WHITE + "Burst duration: 0 s" + ChatColor.RESET);
        } else {
            itemLore.add(ChatColor.WHITE + "Burst duration: " + (getProperty("guns." + getName() + ".firerate", getDefaultFireRate()) / 20) + " s" + ChatColor.RESET);
        }
        itemLore.add(ChatColor.WHITE + "Damage: " + getProperty("guns." + getName() + ".damage", getDefaultDamage()) / 2 + ChatColor.RED + " ❤" + ChatColor.RESET);
        itemLore.add(ChatColor.WHITE + "Velocity: " + getProperty("guns." + getName() + ".velocity", getDefaultVelocity()) + " m/s" + ChatColor.RESET);
        itemLore.add(ChatColor.GRAY + "Shoots hot stuff" + ChatColor.RESET);
        return itemLore;
    }
}
