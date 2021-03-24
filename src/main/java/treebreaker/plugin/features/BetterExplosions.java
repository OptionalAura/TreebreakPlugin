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
package main.java.treebreaker.plugin.features;

import main.java.treebreaker.plugin.utils.Utils;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.block.data.Levelled;
import org.bukkit.entity.Damageable;
import org.bukkit.entity.Entity;
import org.bukkit.util.Vector;

/**
 *
 * @author Daniel Allen
 */
public class BetterExplosions {

    private static final boolean outputDebugMessages = false;

    public static void createExplosion(Location loc, double power, Entity owner, int type) {
        if (loc.getWorld() == null) {
            return;
        }
        loc.getWorld().spawnParticle(Particle.EXPLOSION_HUGE, loc, 1);
        loc.getWorld().playSound(loc, Sound.ENTITY_GENERIC_EXPLODE, 1, 1);
        switch (type) {
            case SPHERICAL:
                createSphericalExplosion(loc, power, owner);
                break;
            case TRACED:
                createTracedExplosion(loc, power, owner);
                break;
        }
    }

    private static double getMod(double multi) {
        double rand = Math.random() * multi;
        if (rand < 0) {
            rand -= 1;
        } else if (rand > 0) {
            rand += 1;
        } else {
            rand = 1;
        }
        return rand;
    }

    private static void createSphericalExplosion(Location loc, double power, Entity owner) {
        if(loc.getWorld() == null)
            return;
        double radius = getMaximumRange(power);
        double multi = Utils.getProperty("world.physics.explosions.explosionRandomness", 0.3);

        double xMod = Math.abs(getMod(multi));
        double yMod = Math.abs(getMod(multi));
        double zMod = Math.abs(getMod(multi));

        double xModN = Math.abs(getMod(multi));
        double yModN = Math.abs(getMod(multi));
        double zModN = Math.abs(getMod(multi));
        double initX = loc.getX() - ((radius) * xMod);
        double initY = Math.max(loc.getY() - ((radius) * yMod), 0);
        double initZ = loc.getZ() - ((radius) * zMod);

        double endX = loc.getX() + ((radius) * xModN);
        double endY = Math.min(loc.getY() + ((radius) * yModN), loc.getWorld().getMaxHeight());
        double endZ = loc.getZ() + ((radius) * zModN);
        if (outputDebugMessages) {
            Bukkit.getConsoleSender().sendMessage("---------------------------------");
            Bukkit.getConsoleSender().sendMessage("Start: [" + initX + ", " + initY + ", " + initZ + "], End: [" + endX + ", " + endY + ", " + endZ + "]");
            Bukkit.getConsoleSender().sendMessage("Mod: [" + xMod + ", " + yMod + ", " + zMod + "], ModN: [" + xModN + ", " + yModN + ", " + zModN + "]");
        }
        boolean brokeBlock = false;
        for (double x = initX; x < endX; x++) {
            double xDif = (loc.getX() - x);
            double xRel = xDif * (xDif < 0 ? xModN : xMod);

            for (double y = initY; y < endY; y++) {
                double yDif = (loc.getY() - y);
                double yRel = yDif * (yDif < 0 ? yModN : yMod);

                for (double z = initZ; z < endZ; z++) {
                    double zDif = (loc.getZ() - z);
                    double zRel = zDif * (zDif < 0 ? zModN : zMod);

                    if (xRel * xRel + yRel * yRel + zRel * zRel < power * power) {
                        Block block = loc.getWorld().getBlockAt((int) x, (int) y, (int) z);
                        if (!block.isEmpty()) {
                            if (block.getType().getBlastResistance() < 10 || (block.isLiquid() && ((Levelled) block.getBlockData()).getLevel() == 0)) {
                                block.breakNaturally();
                                if (outputDebugMessages) {
                                    brokeBlock = true;
                                }
                            }
                        }
                    }
                }
            }
        }
        if (outputDebugMessages) {
            if (brokeBlock) {
                Bukkit.getConsoleSender().sendMessage("Broke block");
            }
            Bukkit.getConsoleSender().sendMessage("---------------------------------");
        }
        double damageRadius = 2 * power;
        for (Entity e : loc.getWorld().getNearbyEntities(loc, damageRadius, damageRadius, damageRadius)) {
            if (e instanceof Damageable) {
                double impact = 1 - (e.getLocation().distance(loc) / damageRadius);
                double dmg = (int) impact * impact * impact * 7 * power + 1;
                ((Damageable) e).damage(dmg, owner);
            }
        }
    }

    private static void createTracedExplosion(Location loc, double power, Entity owner) {
        if(loc.getWorld() == null)
            return;
        int pow = (int) Math.ceil(power);
        int powSquared = (int) Math.pow(pow, 2);
        int size = powSquared * 6;
        Vector[] pos = new Vector[size];
        double[] intensity = new double[size];
        for (int i = 0; i < size; i++) {
            pos[i] = new Vector(loc.getX(), loc.getY(), loc.getZ());
            intensity[i] = (0.7 + (Math.random() * 0.6)) * power;
        }
        double dist = 2d / ((double) pow);

        Vector[] dir = new Vector[size];

        int count = 0;

        for (double i = -1; i <= 1; i += 2) {
            for (int a = 0; a < pow; a++) {
                double aPos = -1 + a * dist;
                for (int b = 0; b < pow; b++) {
                    double bPos = -1 + b * dist;
                    int index = count * powSquared + a * pow + b;
                    dir[index] = new Vector(aPos, bPos, i).normalize();
                }
            }
            count++;
        }
        for (double i = -1; i <= 1; i += 2) {
            for (int a = 0; a < pow; a++) {
                double aPos = -1 + a * dist;
                for (int b = 0; b < pow; b++) {
                    double bPos = -1 + b * dist;
                    int index = count * powSquared + a * pow + b;
                    dir[index] = new Vector(aPos, i, bPos).normalize();
                }
            }
            count++;
        }
        for (double i = -1; i <= 1; i += 2) {
            for (int a = 0; a < pow; a++) {
                double aPos = -1 + a * dist;
                for (int b = 0; b < pow; b++) {
                    double bPos = -1 + b * dist;
                    int index = count * powSquared + a * pow + b;
                    dir[index] = new Vector(i, aPos, bPos).normalize();
                }
            }
            count++;
        }
        for (int i = 0; i < size; i++) {
            while (intensity[i] > 0) {
                if (dir[i] == null) {
                    Bukkit.getConsoleSender().sendMessage(i + " is null?");
                    continue;
                }
                pos[i].setX(pos[i].getX() + dir[i].getX() * 0.3);
                pos[i].setY(pos[i].getY() + dir[i].getY() * 0.3);
                pos[i].setZ(pos[i].getZ() + dir[i].getZ() * 0.3);
                intensity[i] -= 0.225;
                Block block = loc.getWorld().getBlockAt(pos[i].getBlockX(), pos[i].getBlockY(), pos[i].getBlockZ());//loc.getWorld().getBlockAt((int) pos[i].getX(), (int) pos[i].getY(), (int) pos[i].getZ());
                if (!block.isEmpty()) {
                    intensity[i] -= (block.getType().getBlastResistance() + 0.3) * 0.3;
                    if (intensity[i] > 0) {
                        block.breakNaturally();
                    }
                }
            }
        }
        double damageRadius = 2 * power;
        for (Entity e : loc.getWorld().getNearbyEntities(loc, damageRadius, damageRadius, damageRadius)) {
            if (e instanceof Damageable) {
                double impact = 1 - (e.getLocation().distance(loc) / damageRadius);
                double dmg = (int) impact * impact * impact * 7 * power + 1;
                ((Damageable) e).damage(dmg, owner);
            }
        }
    }

    private static double getMaximumRange(double power) {
        return Math.floor(1.3 * power / 0.225) * 0.3;
    }
    public static final int SPHERICAL = 0,
            TRACED = 1;

}
