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
package main.java.treebreaker.plugin.misc;

import main.java.treebreaker.plugin.Main;
import main.java.treebreaker.plugin.features.guns.Gun;
import main.java.treebreaker.plugin.features.guns.Targeting;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.AbstractArrow.PickupStatus;
import org.bukkit.entity.Arrow;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.util.RayTraceResult;
import org.bukkit.util.Vector;

import java.util.UUID;

/**
 *
 * @author Daniel Allen
 */
public class Events implements Listener {

    public static NamespacedKey b_shouldDespawn = new NamespacedKey(Main.thisPlugin, "shouldDespawn"),
            i_stickGun = new NamespacedKey(Main.thisPlugin, "stickGun"),
            i_stickGunUUID = new NamespacedKey(Main.thisPlugin, "stickGunUUID"),
            i_stickGunHashCode = new NamespacedKey(Main.thisPlugin, "stickGunHashCode");
    
    public Events() {

    }

    @EventHandler
    public void onInventoryStickThing(org.bukkit.event.inventory.InventoryOpenEvent event) {
        for (ItemStack item : event.getInventory().getContents()) {
            if (item != null && item.getType().equals(Material.STICK)) {
                if (item.hasItemMeta()) {
                    ItemMeta itemMeta = item.getItemMeta();
                    PersistentDataContainer pdc = itemMeta.getPersistentDataContainer();
                    if (pdc.has(i_stickGun, PersistentDataType.STRING)) {
                        String val = pdc.get(i_stickGun, PersistentDataType.STRING);
                        String uuid;
                        if (pdc.has(i_stickGunUUID, PersistentDataType.STRING)) {
                            uuid = pdc.get(i_stickGunUUID, PersistentDataType.STRING);
                        } else {
                            uuid = UUID.randomUUID().toString();
                            pdc.set(i_stickGunUUID, PersistentDataType.STRING, uuid);
                        }
                        Gun gun = Gun.guns.get(val);
                        if (gun != null) {
                            if (pdc.has(i_stickGunHashCode, PersistentDataType.INTEGER)) {
                                int hash = pdc.get(i_stickGunHashCode, PersistentDataType.INTEGER);
                                int gunHash = gun.versionCode();
                                if (hash != gunHash) {
                                    itemMeta.setLore(gun.getLore());
                                    pdc.set(i_stickGunHashCode, PersistentDataType.INTEGER, gun.versionCode());
                                }
                            } else {
                                pdc.set(i_stickGunHashCode, PersistentDataType.INTEGER, gun.versionCode());
                            }
                            item.setItemMeta(itemMeta);
                        }
                    }
                }
            }
        }
    }

    @EventHandler
    public void onStickRightClick(org.bukkit.event.player.PlayerInteractEvent event) {
        ItemStack itemInHand = event.getItem();
        if (event.getAction() == Action.RIGHT_CLICK_AIR || event.getAction() == Action.RIGHT_CLICK_BLOCK) {
            if (itemInHand != null && itemInHand.getType().equals(Material.STICK)) {
                if (itemInHand.hasItemMeta()) {
                    ItemMeta itemMeta = itemInHand.getItemMeta();
                    PersistentDataContainer pdc = itemMeta.getPersistentDataContainer();
                    if (pdc.has(i_stickGun, PersistentDataType.STRING)) {
                        String val = pdc.get(i_stickGun, PersistentDataType.STRING);
                        String uuid;
                        if (pdc.has(i_stickGunUUID, PersistentDataType.STRING)) {
                            uuid = pdc.get(i_stickGunUUID, PersistentDataType.STRING);
                        } else {
                            uuid = UUID.randomUUID().toString();
                            pdc.set(i_stickGunUUID, PersistentDataType.STRING, uuid);
                        }
                        Gun gun = Gun.guns.get(val);
                        
                        if (gun != null) {
                            if (pdc.has(i_stickGunHashCode, PersistentDataType.INTEGER)) {
                                int hash = pdc.get(i_stickGunHashCode, PersistentDataType.INTEGER);
                                int gunHash = gun.versionCode();
                                if (hash != gunHash) {
                                    itemMeta.setLore(gun.getLore());
                                    pdc.set(i_stickGunHashCode, PersistentDataType.INTEGER, gun.versionCode());
                                }
                            } else {
                                pdc.set(i_stickGunHashCode, PersistentDataType.INTEGER, gun.versionCode());
                            }
                            itemInHand.setItemMeta(itemMeta);

                            gun.shoot(event.getPlayer(), itemInHand, uuid);
                        }
                    }
                }
            }
        } else if (event.getAction() == Action.LEFT_CLICK_AIR || event.getAction() == Action.LEFT_CLICK_BLOCK) {
            if (itemInHand != null && itemInHand.getType().equals(Material.STICK)) {
                if (itemInHand.hasItemMeta()) {
                    ItemMeta itemMeta = itemInHand.getItemMeta();
                    PersistentDataContainer pdc = itemMeta.getPersistentDataContainer();
                    if (pdc.has(i_stickGun, PersistentDataType.STRING)) {
                        String val = pdc.get(i_stickGun, PersistentDataType.STRING);
                        String uuid;
                        if (pdc.has(i_stickGunUUID, PersistentDataType.STRING)) {
                            uuid = pdc.get(i_stickGunUUID, PersistentDataType.STRING);
                        } else {
                            uuid = UUID.randomUUID().toString();
                            pdc.set(i_stickGunUUID, PersistentDataType.STRING, uuid);
                        }
                        Gun gun = Gun.guns.get(val);
                        if (gun != null) {
                            if (pdc.has(i_stickGunHashCode, PersistentDataType.INTEGER)) {
                                int hash = pdc.get(i_stickGunHashCode, PersistentDataType.INTEGER);
                                int gunHash = gun.versionCode();
                                if (hash != gunHash) {
                                    itemMeta.setLore(gun.getLore());
                                    pdc.set(i_stickGunHashCode, PersistentDataType.INTEGER, gun.versionCode());
                                }
                            } else {
                                pdc.set(i_stickGunHashCode, PersistentDataType.INTEGER, gun.versionCode());
                            }
                            itemInHand.setItemMeta(itemMeta);
                            RayTraceResult rtr = event.getPlayer().getWorld().rayTraceBlocks(event.getPlayer().getEyeLocation(), event.getPlayer().getEyeLocation().getDirection(), 4680);
                            if (rtr != null) {
                                if (rtr.getHitBlock() != null) {
                                    if(gun instanceof Targeting)
                                    ((Targeting) gun).setTarget(uuid, rtr.getHitPosition().toLocation(event.getPlayer().getWorld()));
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    @EventHandler
    public void onBowShoot(EntityShootBowEvent event) {
        if (event.getBow().containsEnchantment(Enchantment.MULTISHOT)) {
            int arrowCount = Math.min(event.getBow().getEnchantmentLevel(Enchantment.MULTISHOT) * 2, 50);
            double radSpread = 0.2;
            double spacing = radSpread / arrowCount;
            double yaw = event.getEntity().getLocation().getYaw() - radSpread;
            Vector facing = event.getEntity().getLocation().getDirection();
            Arrow spawned;
            for (int i = 0; i < arrowCount / 2; i++) {
                facing.rotateAroundY(-spacing);
                spawned = event.getEntity().getWorld().spawnArrow(event.getProjectile().getLocation(), facing, (float) event.getProjectile().getVelocity().length(), 2);
                spawned.setShooter(event.getEntity());
                spawned.setDamage(((Arrow) event.getProjectile()).getDamage());
                spawned.setPickupStatus(PickupStatus.CREATIVE_ONLY);
                if (((Arrow) event.getProjectile()).hasCustomEffects()) {
                    spawned.setBasePotionData(((Arrow) event.getProjectile()).getBasePotionData());
                }
                spawned.setFireTicks(event.getProjectile().getFireTicks());
                spawned.setKnockbackStrength(((Arrow) event.getProjectile()).getKnockbackStrength());
                spawned.setPierceLevel(((Arrow) event.getProjectile()).getPierceLevel());

                spawned.getPersistentDataContainer().set(b_shouldDespawn, PersistentDataType.INTEGER, 1);
            }
            facing = event.getEntity().getLocation().getDirection();
            for (int i = 0; i < arrowCount / 2; i++) {
                facing.rotateAroundY(spacing);
                spawned = event.getEntity().getWorld().spawnArrow(event.getProjectile().getLocation(), facing, (float) event.getProjectile().getVelocity().length(), 2);
                spawned.setShooter(event.getEntity());
                spawned.setDamage(((Arrow) event.getProjectile()).getDamage());
                spawned.setPickupStatus(PickupStatus.CREATIVE_ONLY);
                if (((Arrow) event.getProjectile()).hasCustomEffects()) {
                    spawned.setBasePotionData(((Arrow) event.getProjectile()).getBasePotionData());
                }
                spawned.setFireTicks(event.getProjectile().getFireTicks());
                spawned.setKnockbackStrength(((Arrow) event.getProjectile()).getKnockbackStrength());
                spawned.setPierceLevel(((Arrow) event.getProjectile()).getPierceLevel());

                spawned.getPersistentDataContainer().set(b_shouldDespawn, PersistentDataType.INTEGER, 1);
            }
        }
    }

    @EventHandler
    public void ArrowHitTarget(org.bukkit.event.entity.ProjectileHitEvent event
    ) {
        if (event.getEntity() instanceof Arrow) {
            Arrow arrow = (Arrow) event.getEntity();
            //if (arrow.isInBlock()) {
            PersistentDataContainer pdc = arrow.getPersistentDataContainer();
            if (pdc.has(b_shouldDespawn, PersistentDataType.INTEGER)) {
                int val = pdc.get(b_shouldDespawn, PersistentDataType.INTEGER);
                if (val == 1) {
                    arrow.remove();
                }
            }
            //}
        }
    }
}
