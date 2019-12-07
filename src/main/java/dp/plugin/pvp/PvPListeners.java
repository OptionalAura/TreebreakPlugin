package main.java.dp.plugin.pvp;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;
import static main.java.dp.plugin.Main.thisPlugin;
import main.java.dp.plugin.entities.EntityMoveEvent;
import main.java.dp.plugin.entities.entities.CustomTNT;
import static main.java.dp.plugin.pvp.PvPQueue.activePlayers;
import static main.java.dp.plugin.pvp.PvPQueue.timer;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Damageable;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LargeFireball;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.TNTPrimed;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import static org.bukkit.event.EventPriority.HIGH;
import static org.bukkit.event.EventPriority.NORMAL;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.RayTraceResult;
import org.bukkit.util.Vector;

/*
    @author Daniel Allen
    25-Nov-2019
 */
public class PvPListeners implements Listener {

    static NamespacedKey entityType = new NamespacedKey(thisPlugin, "entitytype");
    static NamespacedKey weaponType = new NamespacedKey(thisPlugin, "weapontype");

    @EventHandler(priority = HIGH)
    public static void playerAttack(EntityDamageByEntityEvent event) {
        Entity hit = event.getEntity();
        Entity hitter = event.getDamager();
        if (hit.isInvulnerable() && hit.isCustomNameVisible() && hit instanceof Player == false) {
            hitter.sendMessage(ChatColor.RED + "You cannot damage this entity!" + ChatColor.RESET);
            event.setCancelled(true);
        }

    }
    private static Map<String, Integer> railgunCooldowns = new HashMap<>();
    private static Map<String, Integer> wandCooldowns = new HashMap<>();

    @EventHandler(priority = NORMAL)
    public static void playerRightClick(PlayerInteractEvent event) {
        if (event.isBlockInHand()) {
            ItemStack held = event.getItem();
            if (held != null && held.hasItemMeta()) {
                ItemMeta heldMeta = held.getItemMeta();
                PersistentDataContainer container = heldMeta.getPersistentDataContainer();

                if (container.has(weaponType, PersistentDataType.INTEGER)) {
                    final Player player = event.getPlayer();
                    Integer type = container.get(weaponType, PersistentDataType.INTEGER);
                    if (type == WeaponID.SWORD.getId()) {
                        //sword
                    } else if (type == WeaponID.BOW.getId()) {
                        //bow
                    } else if (type == WeaponID.RAILGUN.getId() && (event.getAction() == Action.RIGHT_CLICK_AIR || event.getAction() == Action.RIGHT_CLICK_BLOCK)) {
                        boolean run = false;
                        if (((activePlayers[0] != null && activePlayers[0].getUniqueId().compareTo(player.getUniqueId()) == 0)
                                || (activePlayers[1] != null && activePlayers[1].getUniqueId().compareTo(player.getUniqueId()) == 0))) {
                            if (timer <= 0 && !railgunCooldowns.containsKey(player.getUniqueId().toString())) {
                                run = true;
                            }
                        } else {
                            if (!railgunCooldowns.containsKey(player.getUniqueId().toString())) {
                                run = true;
                            }
                        }
                        if (run) {
                            //railgun
                            player.getWorld().playSound(player.getLocation().add(0, 1, 0), Sound.BLOCK_CONDUIT_ACTIVATE, 1, 1);
                            railgunCooldowns.put(player.getUniqueId().toString(), 40);
                            BukkitRunnable cooldown = new BukkitRunnable() {
                                @Override
                                public void run() {
                                    player.getWorld().playSound(player.getLocation().add(0, 1, 0), Sound.ITEM_SHIELD_BLOCK, 1, 1);
                                    railgunCooldowns.remove(player.getUniqueId().toString());
                                    Location loc = player.getEyeLocation();
                                    Vector pVec = player.getEyeLocation().getDirection().normalize();
                                    RayTraceResult rtr = player.getWorld().rayTraceEntities(player.getEyeLocation(), pVec, 30, new Predicate<Entity>() {
                                        @Override
                                        public boolean test(Entity t) {
                                            if (t instanceof LivingEntity == false) {
                                                return false;
                                            }
                                            return t.getUniqueId().compareTo(player.getUniqueId()) != 0;
                                        }
                                    });
                                    if (rtr != null && rtr.getHitEntity() != null && rtr.getHitEntity() instanceof LivingEntity) {
                                        LivingEntity hit = (LivingEntity) rtr.getHitEntity();
                                        if (hit.getUniqueId().compareTo(player.getUniqueId()) != 0) {
                                            hit.damage(8, player);
                                        }
                                    }
                                    for (double i = 2; i < 30; i += 0.2) {
                                        double x = pVec.getX() * i;
                                        double y = pVec.getY() * i;
                                        double z = pVec.getZ() * i;
                                        player.getWorld().spawnParticle(Particle.CRIT_MAGIC, loc.add(x, y, z), 1, 0, 0, 0);
                                    }
                                }
                            };
                            cooldown.runTaskLater(thisPlugin, 40);
                        }
                    } else if (type == WeaponID.FIRE_STAFF.getId() && (event.getAction() == Action.RIGHT_CLICK_AIR || event.getAction() == Action.RIGHT_CLICK_BLOCK)) {
                        //wand
                        boolean run = false;
                        if ((activePlayers[0] != null && activePlayers[0].getUniqueId().compareTo(player.getUniqueId()) == 0) || (activePlayers[1] != null && activePlayers[1].getUniqueId().compareTo(player.getUniqueId()) == 0)) {
                            if (timer <= 0 && !wandCooldowns.containsKey(player.getUniqueId().toString())) {
                                run = true;
                            }
                        } else {
                            if (!wandCooldowns.containsKey(player.getUniqueId().toString())) {
                                run = true;
                            }
                        }
                        if (run) {
                            player.getWorld().playSound(player.getLocation().add(0, 1, 0), Sound.ENTITY_BLAZE_SHOOT, 1, 1);
                            LargeFireball fb = player.getWorld().spawn(player.getEyeLocation(), LargeFireball.class);
                            fb.setShooter(player);
                            fb.setDirection(player.getLocation().getDirection().normalize());
                            fb.setVelocity(player.getLocation().getDirection().normalize());
                            wandCooldowns.put(player.getUniqueId().toString(), 15);
                            BukkitRunnable cooldown = new BukkitRunnable() {
                                @Override
                                public void run() {
                                    //player.getWorld().playSound(player.getLocation().add(0, 1, 0), Sound.BLOCK_BEACON_POWER_SELECT, 1, 1);
                                    wandCooldowns.remove(player.getUniqueId().toString());
                                }
                            };
                            cooldown.runTaskLater(thisPlugin, 15);
                        }
                    } else if (type == WeaponID.BOOMER_TNT.getId() && event.getAction() == Action.RIGHT_CLICK_BLOCK) {

                        Location toSpawn = event.getClickedBlock().getLocation().add(event.getBlockFace().getDirection().normalize());
                        TNTPrimed spawnedEntity = (TNTPrimed) toSpawn.getWorld().spawnEntity(toSpawn, EntityType.PRIMED_TNT);
                        spawnedEntity.setYield(boomerYield);
                        spawnedEntity.setFuseTicks(boomerFuse);
                        spawnedEntity.getPersistentDataContainer().set(entityType, PersistentDataType.INTEGER, 0);

                    }
                }
            }
        }
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void tntExplode(final EntityExplodeEvent event) {
        if (event.isCancelled()) {
            return;
        }
        if (event.getEntityType() == EntityType.PRIMED_TNT) {
            PersistentDataContainer pdc = event.getEntity().getPersistentDataContainer();
            if ((pdc.has(entityType, PersistentDataType.INTEGER) && pdc.get(entityType, PersistentDataType.INTEGER) == 0) || true) {
                Bukkit.getConsoleSender().sendMessage(pdc.has(entityType, PersistentDataType.INTEGER) ? "Explosion had tag with value " + pdc.get(entityType, PersistentDataType.INTEGER) + "." : "Explosion did not have tag.");
                new BukkitRunnable() {
                    @Override
                    public void run() {
                        List<Entity> nearbyEntities = event.getEntity().getNearbyEntities(event.getYield(), event.getYield(), event.getYield());
                        for (Entity e : nearbyEntities) {
                            if (e instanceof Damageable) {
                                /*
                                        exposure = [2×width+1] by [2×height+1] by [2×depth+1]
                                        1. (1-[distance from explosion/damage radius])×[exposure]
                                        2. ((impact×impact+impact)×7×power+1)
                                        3. (exposure×[max Blast Protection from all armor]×0.15)
                                 */
                                double exposure = event.getYield() - e.getLocation().distance(event.getLocation());
                                double impact = (1 - (e.getLocation().distance(event.getLocation()) / event.getYield()));
                                double damage = Math.max(Math.floor((impact * impact + impact) * 7 * event.getYield() / 2 + 1), 1);
                                double armor = 0;
                                if (e instanceof LivingEntity) {
                                    LivingEntity le = (LivingEntity) e;
                                    for (ItemStack is : le.getEquipment().getArmorContents()) {
                                        if (is.hasItemMeta()) {
                                            ItemMeta im = is.getItemMeta();
                                            if (im.hasEnchant(Enchantment.PROTECTION_EXPLOSIONS)) {
                                                armor += im.getEnchantLevel(Enchantment.PROTECTION_EXPLOSIONS);
                                            }
                                        }
                                    }
                                }
                                exposure = Math.max(0, exposure - exposure * armor * 0.15);

                                Vector direction = e.getLocation().subtract(event.getLocation()).toVector().normalize().multiply(exposure);
                                ((Damageable) e).damage(damage, event.getEntity());
                                e.setVelocity(direction);
                            }
                        }
                        for (double x = -event.getYield() / 2; x < event.getYield() / 2; x++) {
                            for (double y = -event.getYield() / 2; y < event.getYield() / 2; y++) {
                                for (double z = -event.getYield() / 2; z < event.getYield() / 2; z++) {
                                    Location cur = event.getLocation().add(x, y, z);
                                    if (Math.sqrt(Math.pow(x, 2) + Math.pow(y, 2) + Math.pow(z, 2)) <= event.getYield() / 2) {
                                        if (cur.getBlock().getType() != Material.AIR) {
                                            if (cur.getBlock().getType().getBlastResistance() < 100) {
                                                for (ItemStack is : cur.getBlock().getDrops()) {
                                                    cur.getWorld().dropItem(cur, is);
                                                }
                                            }
                                        }
                                        cur.getBlock().setType(Material.AIR);
                                    }
                                }
                            }
                        }
                    }
                }.runTask(thisPlugin);
            }
        }
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void entityMove(EntityMoveEvent event) {
        Entity e = event.getEntity();
        if (e instanceof CustomTNT) {
            Bukkit.getConsoleSender().sendMessage(ChatColor.GOLD + "Entity " + e.getName() + " (of type " + e.getClass().getSimpleName() + ") removed." + ChatColor.RESET);
            e.remove();
        } else {
            Bukkit.getConsoleSender().sendMessage(ChatColor.GOLD + "Entity " + e.getName() + " (of type " + e.getClass().getSimpleName() + ") moved from (" + event.getInitalLocation().toString() + ") to (" + event.getFinalLocation().toString() + ")." + ChatColor.RESET);
        }
    }
    private static int boomerFuse = 60;
    private static float boomerYield = 15;

    public static void setBoomerTNTData(int fuse, float yield) {
        boomerFuse = fuse;
        boomerYield = yield;
    }
}
