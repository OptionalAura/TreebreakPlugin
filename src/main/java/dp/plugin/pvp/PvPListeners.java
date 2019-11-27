package main.java.dp.plugin.pvp;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.function.Predicate;
import static main.java.dp.plugin.Main.thisPlugin;
import static main.java.dp.plugin.pvp.PvPQueue.activePlayers;
import static main.java.dp.plugin.pvp.PvPQueue.timer;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.NamespacedKey;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.Damageable;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Fireball;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.SmallFireball;
import org.bukkit.event.EventHandler;
import static org.bukkit.event.EventPriority.HIGH;
import static org.bukkit.event.EventPriority.HIGHEST;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataHolder;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitScheduler;
import org.bukkit.util.RayTraceResult;
import org.bukkit.util.Vector;

/*
    @author Daniel Allen
    25-Nov-2019
 */
public class PvPListeners implements Listener {

    @EventHandler(priority = HIGH)
    public static void playerAttack(EntityDamageByEntityEvent event) {
        Entity hit = event.getEntity();
        Entity hitter = event.getDamager();

    }
    private static ArrayList<String> railgunCooldowns = new ArrayList<>();
    private static ArrayList<String> wandCooldowns = new ArrayList<>();

    @EventHandler(priority = HIGH)
    public static void playerRightClick(PlayerInteractEvent event) {
        ItemStack held = event.getItem();
        if (held != null && held.hasItemMeta()) {
            ItemMeta heldMeta = held.getItemMeta();
            NamespacedKey key = new NamespacedKey(thisPlugin, "weapontype");
            PersistentDataContainer container = heldMeta.getPersistentDataContainer();

            if (container.has(key, PersistentDataType.SHORT)) {
                final Player player = event.getPlayer();
                short type = container.get(key, PersistentDataType.SHORT);
                if (type == 0) {
                    //sword
                } else if (type == 1) {
                    //bow
                } else if (type == 2) {
                    if ((activePlayers[0] != null && activePlayers[0].getUniqueId().compareTo(player.getUniqueId()) == 0) || (activePlayers[1] != null && activePlayers[1].getUniqueId().compareTo(player.getUniqueId()) == 0)) {
                        if (timer <= 0 && !railgunCooldowns.contains(player.getUniqueId().toString())) {
                            //railgun
                            player.getWorld().playSound(player.getLocation().add(0, 1, 0), Sound.BLOCK_CONDUIT_ACTIVATE, 1, 1);
                            railgunCooldowns.add(player.getUniqueId().toString());
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
                                        Bukkit.getConsoleSender().sendMessage("Hit entity " + rtr.getHitEntity() + " from " + player.getName() + "'s attack");
                                        LivingEntity hit = (LivingEntity) rtr.getHitEntity();
                                        if (hit.getUniqueId().compareTo(player.getUniqueId()) != 0) {
                                            hit.damage(8);
                                        }
                                    }
                                    for (double i = 2; i < 30; i += 0.2) {
                                        double x = pVec.getX() * i;
                                        double y = pVec.getY() * i;
                                        double z = pVec.getZ() * i;

                                        player.getWorld().spawnParticle(Particle.SPIT, loc.add(x, y, z), 1, 0, 0, 0);
                                    }
                                }
                            };
                            cooldown.runTaskLater(thisPlugin, 40);
                        }
                    } else {
                        if (!railgunCooldowns.contains(player.getUniqueId().toString())) {
                            //railgun
                            player.getWorld().playSound(player.getLocation().add(0, 1, 0), Sound.BLOCK_CONDUIT_ACTIVATE, 1, 1);
                            railgunCooldowns.add(player.getUniqueId().toString());
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
                                        Bukkit.getConsoleSender().sendMessage("Hit entity " + rtr.getHitEntity() + " from " + player.getName() + "'s attack");
                                        LivingEntity hit = (LivingEntity) rtr.getHitEntity();
                                        if (hit.getUniqueId().compareTo(player.getUniqueId()) != 0) {
                                            hit.damage(8);
                                        }
                                    }
                                    for (double i = 2; i < 30; i += 0.2) {
                                        double x = pVec.getX() * i;
                                        double y = pVec.getY() * i;
                                        double z = pVec.getZ() * i;

                                        player.getWorld().spawnParticle(Particle.SPIT, loc.add(x, y, z), 1, 0, 0, 0);
                                    }
                                }
                            };
                            cooldown.runTaskLater(thisPlugin, 50);
                        }
                    }
                } else if (type == 3) {
                    //wand
                    if ((activePlayers[0] != null && activePlayers[0].getUniqueId().compareTo(player.getUniqueId()) == 0) || (activePlayers[1] != null && activePlayers[1].getUniqueId().compareTo(player.getUniqueId()) == 0)) {
                        if (timer <= 0 && !wandCooldowns.contains(player.getUniqueId().toString())) {
                            player.getWorld().playSound(player.getLocation().add(0, 1, 0), Sound.ENTITY_BLAZE_BURN, 1, 1);
                            Fireball fb = player.getWorld().spawn(player.getLocation().add(0, 1, 0), Fireball.class);
                            fb.setShooter(player);
                            fb.setDirection(player.getLocation().getDirection());
                            wandCooldowns.add(player.getUniqueId().toString());
                            BukkitRunnable cooldown = new BukkitRunnable() {
                                @Override
                                public void run() {
                                    player.getWorld().playSound(player.getLocation().add(0, 1, 0), Sound.BLOCK_BEACON_POWER_SELECT, 1, 1);
                                    wandCooldowns.remove(player.getUniqueId().toString());
                                }
                            };
                            cooldown.runTaskLater(thisPlugin, 15);
                        }
                    } else {
                        if (!wandCooldowns.contains(player.getUniqueId().toString())) {
                            player.getWorld().playSound(player.getLocation().add(0, 1, 0), Sound.BLOCK_BEACON_ACTIVATE, 1, 1);

                            wandCooldowns.add(player.getUniqueId().toString());
                            BukkitRunnable cooldown = new BukkitRunnable() {
                                @Override
                                public void run() {
                                    player.getWorld().playSound(player.getLocation().add(0, 1, 0), Sound.BLOCK_BEACON_AMBIENT, 1, 1);
                                    wandCooldowns.remove(player.getUniqueId().toString());
                                    Fireball fb = player.getWorld().spawn(player.getLocation().add(0, 1, 0), Fireball.class);
                                    fb.setShooter(player);
                                    fb.setDirection(player.getLocation().getDirection());
                                }
                            };
                            cooldown.runTaskLater(thisPlugin, 15);
                        }
                    }
                }
            }
        }
    }
}
