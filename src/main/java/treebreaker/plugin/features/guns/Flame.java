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

import main.java.treebreaker.plugin.utils.Utils;
import org.bukkit.FluidCollisionMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.block.Block;
import org.bukkit.entity.Damageable;
import org.bukkit.entity.Entity;
import org.bukkit.util.RayTraceResult;
import org.bukkit.util.Vector;

public class Flame extends Projectile {
    public Flame(Location origin, Vector vel, double damage, Entity owner, Shot parent, int particles, Particle.DustOptions tracer) {
        super(origin, vel, damage, owner, parent, particles, tracer);
    }

    @Override
    public void move() {
        pos.add(new Vector(vel.getX() / 20, vel.getY() / 20, vel.getZ() / 20));
        vel.add(new Vector(0, Utils.getProperty("world.physics.gravity", -9.81d) / 20, 0));
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
            RayTraceResult hits = pos.getWorld().rayTrace(curPos, vel.clone().normalize(), vel.length() / 20, FluidCollisionMode.ALWAYS, true, 0.2, hitFilter);
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
                        if (hits.getHitBlockFace() != null) {
                            Block fire = hits.getHitBlock().getRelative(hits.getHitBlockFace());
                            if (fire.isEmpty() || fire.getBlockData().getMaterial().isBurnable()) {
                                fire.setType(Material.FIRE);
                            }
                        }

                    }
                }
                destroy();
            }
        }
        drawLine(curPos, pos);
    }

    /**
     * Called when a projectile collides with an entity
     *
     * @param e The entity that this projectile hit
     */
    @Override
    public void hit(Entity e) {
        if (e instanceof Damageable) {
            ((Damageable) e).damage(hitDamage, owner);
            e.setFireTicks(20);
        }
    }
}
