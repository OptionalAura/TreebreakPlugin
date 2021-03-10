/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package main.java.treebreaker.plugin.misc;

import java.util.UUID;
import main.java.treebreaker.plugin.Main;
import main.java.treebreaker.plugin.features.Guns.AT4;
import main.java.treebreaker.plugin.features.Guns.AssaultRifle;
import main.java.treebreaker.plugin.features.Guns.Shotgun;
import main.java.treebreaker.plugin.features.Guns.Sniper;
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
import org.bukkit.util.Vector;

/**
 *
 * @author dsato
 */
public class Events implements Listener {

    public static NamespacedKey b_shouldDespawn = new NamespacedKey(Main.thisPlugin, "shouldDespawn"),
            i_stickGun = new NamespacedKey(Main.thisPlugin, "stickGun"),
            i_stickGunUUID = new NamespacedKey(Main.thisPlugin, "stickGunUUID");
    public static final int SHOTGUN = 0,
            ASSAULT_RIFLE = 1,
            SNIPER = 2,
            SUBMACHINE_GUN = 3,
            ROCKET_LAUNCHER = 4,
            DMR = 5,
            OTHER = -1;

    public Events() {

    }

    @EventHandler
    public void onStickRightClick(org.bukkit.event.player.PlayerInteractEvent event) {
        ItemStack itemInHand = event.getItem();
        if (event.getAction() == Action.RIGHT_CLICK_AIR || event.getAction() == Action.RIGHT_CLICK_BLOCK) {
            if (itemInHand != null && itemInHand.getType().equals(Material.STICK)) {
                if (itemInHand.hasItemMeta()) {
                    ItemMeta itemMeta = itemInHand.getItemMeta();
                    PersistentDataContainer pdc = itemMeta.getPersistentDataContainer();
                    if (pdc.has(i_stickGun, PersistentDataType.INTEGER)) {
                        int val = pdc.get(i_stickGun, PersistentDataType.INTEGER);
                        String uuid;
                        if(pdc.has(i_stickGunUUID, PersistentDataType.STRING)){
                            uuid = pdc.get(i_stickGunUUID, PersistentDataType.STRING);
                        } else {
                            uuid = UUID.randomUUID().toString();
                            pdc.set(i_stickGunUUID, PersistentDataType.STRING, uuid);
                        }
                        itemInHand.setItemMeta(itemMeta);
                        switch (val) {
                            case SHOTGUN:
                                Shotgun.shoot(event.getPlayer(), itemInHand, uuid);
                                break;
                            case SNIPER: 
                                Sniper.shoot(event.getPlayer(), itemInHand, uuid);
                                break;
                            case ASSAULT_RIFLE: 
                                AssaultRifle.shoot(event.getPlayer(), itemInHand, uuid);
                                break;
                            case ROCKET_LAUNCHER:
                                AT4.shoot(event.getPlayer(), itemInHand, uuid);
                                break;
                            default:
                                break;
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
                spawned.setFireTicks(((Arrow) event.getProjectile()).getFireTicks());
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
                spawned.setFireTicks(((Arrow) event.getProjectile()).getFireTicks());
                spawned.setKnockbackStrength(((Arrow) event.getProjectile()).getKnockbackStrength());
                spawned.setPierceLevel(((Arrow) event.getProjectile()).getPierceLevel());

                spawned.getPersistentDataContainer().set(b_shouldDespawn, PersistentDataType.INTEGER, 1);
            }
        }
    }

    @EventHandler
    public void ArrowHitTarget(org.bukkit.event.entity.ProjectileHitEvent event) {
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
