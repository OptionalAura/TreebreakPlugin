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

import main.java.treebreaker.plugin.features.BetterExplosions;
import main.java.treebreaker.plugin.utils.Utils;
import org.bukkit.*;
import org.bukkit.entity.Damageable;
import org.bukkit.entity.Entity;
import org.bukkit.util.RayTraceResult;
import org.bukkit.util.Vector;

/**
 * @author Daniel Allen
 */
public class Rocket extends Projectile {

    private final float explosionPower;
    private boolean safe = true;

    public Rocket(Location newPos, Vector vel, double damage, Entity shooter, Shot shot, int i, Particle.DustOptions tracer, float power) {
        super(newPos, vel, damage, shooter, shot, i);
        this.tracer = tracer;
        explosionPower = power;
    }

    public Rocket(Location newPos, Vector vel, double damage, Entity shooter, Shot shot, int i, Particle.DustOptions tracer, float power, boolean safe) {
        super(newPos, vel, damage, shooter, shot, i);
        this.tracer = tracer;
        explosionPower = power;
        this.safe = safe;
    }

    public float getPower() {
        return this.explosionPower;
    }

    /**
     * Called every tick on this projectile
     */
    @Override
    public void tick() {
        if (pos.getWorld() == null) {
            return;
        }
        lifetime++;

        Location curPos = pos.clone();
        move();
        if ((curPos.getY() <= curPos.getWorld().getMaxHeight() && curPos.getY() > 0) || (pos.getY() <= pos.getWorld().getMaxHeight() && pos.getY() > 0)) {
            boolean hitBlock = true;
            while (hitBlock && isAlive()) {
                RayTraceResult hits = pos.getWorld().rayTrace(curPos, vel.clone().normalize(), vel.length() / 20, FluidCollisionMode.ALWAYS, true, 0.084, hitFilter);

                hitBlock = false;
                if (hits != null && (hits.getHitBlock() != null || hits.getHitEntity() != null)) {
                    Vector p = hits.getHitPosition();
                    pos.setX(p.getX());
                    pos.setY(p.getY());
                    pos.setZ(p.getZ());
                    if (hits.getHitEntity() != null) {
                        hit(hits.getHitEntity());
                    } else if (hits.getHitBlock() != null) {
                        Material mat = hits.getHitBlock().getBlockData().getMaterial();
                        if (mat.isBlock()) {
                            if (mat.equals(Material.WATER)) {
                                Vector dir = vel.clone().normalize();
                                double angleIncidence = Math.abs(Math.asin(dir.getY()));
                                double targetAngle = 10 * Math.PI / 180;
                                if (angleIncidence < targetAngle || angleIncidence > Math.PI - targetAngle) {
                                    vel.setY(vel.getY() * -1);
                                } else {
                                    destroy();
                                }
                            } else {
                                if (mat.getHardness() < 0.4 && mat.getHardness() != -1) {
                                    hits.getHitBlock().breakNaturally();
                                    hitBlock = true;
                                } else {
                                    if (!safe || lifetime > ((1.3 * explosionPower / 0.225) * 0.3) / (vel.length() / 20)) {
                                        Particle.DustOptions dust = new Particle.DustOptions(Color.fromRGB(150, 55, 55), 1);
                                        curPos.getWorld().spawnParticle(Particle.REDSTONE, pos, 4, 0, 0, 0, dust);
                                        explode();
                                        destroy();
                                    } else {
                                        destroy();
                                    }
                                }
                            }
                        } else {
                            destroy();
                        }
                    }
                }
            }
        }
        drawLine(curPos, pos);
    }

    public void explode() {
        boolean defaultExplosions = Utils.getProperty("world.physics.explosions.vanillaExplosions", true);
        if (defaultExplosions) {
            pos.getWorld().createExplosion(pos, explosionPower, false, true, owner);
        } else {
            boolean vectorExplosions = Utils.getProperty("world.physics.explosions.vectorExplosions", true);
            BetterExplosions.createExplosion(pos, explosionPower, owner, vectorExplosions ? BetterExplosions.TRACED : BetterExplosions.SPHERICAL);
        }
    }

    @Override
    public void hit(Entity e) {
        explode();
        if (e instanceof Damageable) {
            ((Damageable) e).damage(hitDamage, owner);
        }
        destroy();
    }

}
