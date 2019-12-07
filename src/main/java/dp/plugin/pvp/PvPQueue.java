package main.java.dp.plugin.pvp;

import java.util.AbstractMap;
import java.util.Deque;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map;
import java.util.Objects;
import main.java.dp.plugin.Main;
import main.java.dp.plugin.pvp.classKits.Kit;
import static main.java.dp.plugin.utils.Utils.bpsToMovementSpeed;
import static main.java.dp.plugin.utils.Utils.createEnchantedItem;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.attribute.Attribute;
import org.bukkit.block.data.BlockData;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Zombie;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.scheduler.BukkitRunnable;

/*
    @author Daniel Allen
    24-Nov-2019
 */
public class PvPQueue implements Listener {

    private static Deque<Map.Entry<Player, Boolean>> queue = new LinkedList<>();
    protected static LivingEntity[] activePlayers = new LivingEntity[2];
    protected static Map<String, Kit> playerClasses = new HashMap<>();
    private static boolean ready = false;
    private static boolean ongoing = false;

    public PvPQueue() {
        for (World w : Main.thisPlugin.getServer().getWorlds()) {
            Bukkit.getConsoleSender().sendMessage("§9  Found world: " + w.getName());
        }
        if (pvpWorld == null) {
            pvpWorld = Main.thisPlugin.getServer().getWorld("superflat");
        }
    }

    public static void joinQueue(Player p, Boolean pvp) {
        if (pvpWorld == null) {
            pvpWorld = Main.thisPlugin.getServer().getWorld("superflat");
        }
        boolean alreadyHas = false;
        Iterator<Map.Entry<Player, Boolean>> it = queue.iterator();
        while (it.hasNext()) {
            Map.Entry<Player, Boolean> pair = it.next();
            if (pair.getKey().getUniqueId().compareTo(p.getUniqueId()) == 0) {
                alreadyHas = true;
                if (Objects.equals(pvp, pair.getValue())) {
                    alreadyHas = false;
                    it.remove();
                }
                break;
            }
        }
        if (playerClasses.containsKey(p.getUniqueId().toString())) {
            if (!alreadyHas) {
                queue.add(new AbstractMap.SimpleEntry(p, pvp));
                p.sendMessage("You joined the PvP queue. You are currently in position " + ChatColor.AQUA + queue.size());
                if (!ongoing) {
                    ready = true;
                }
            } else {
                p.sendMessage("You are already in the queue.");
            }

            if (ready) {
                boolean valid = false;
                boolean solo = !queue.peek().getValue();
                if (!solo) {
                    for (int i = 1; i < queue.size(); i++) {
                        if (((LinkedList<Map.Entry<Player, Boolean>>) queue).get(i).getValue()) {
                            valid = true;
                        }
                    }
                } else if (solo) {
                    valid = true;
                }
                if (valid) {
                    startMatch(!solo);
                }
            }
        } else {
            TextComponent msg = new TextComponent(ChatColor.WHITE + "You need to ");
            TextComponent msg2 = new TextComponent(ChatColor.AQUA + "choose a class");
            TextComponent msg3 = new TextComponent(ChatColor.WHITE + " first");
            msg2.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder(ChatColor.GOLD + "Click to choose your class").create()));
            msg2.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, ("/class")));
            msg.addExtra(msg2);
            msg.addExtra(msg3);
            p.spigot().sendMessage(msg);
        }
    }
    //arena is from region (42109, 255, 42109) to (42029, 56, 42029)
    private static Object[][][][] arenaBackup = new Object[81][201][81][2];
    private static boolean backedup = false;
    protected static int timer = 0;

    public static void startMatch(boolean pvp) {
        if (pvpWorld == null) {
            pvpWorld = Main.thisPlugin.getServer().getWorld("superflat");
        }
        if (!backedup) {
            BukkitRunnable backup = new BukkitRunnable() {
                @Override
                public void run() {
                    for (int x = 42029; x <= 42109; x++) {
                        for (int y = 56; y <= 255; y++) {
                            for (int z = 42029; z <= 42109; z++) {
                                arenaBackup[x - 42029][y - 56][z - 42029][0] = pvpWorld.getBlockAt(x, y, z).getType();
                                arenaBackup[x - 42029][y - 56][z - 42029][1] = pvpWorld.getBlockAt(x, y, z).getBlockData();
                            }
                        }
                    }
                }
            };
            backup.run();
            backedup = true;
        }
        if (queue.size() > 1 && pvp) {
            final Player p1 = queue.pop().getKey();
            Player p2temp = queue.peek().getKey();
            for (int i = 0; i < queue.size(); i++) {
                if (((LinkedList<Map.Entry<Player, Boolean>>) queue).get(i).getValue()) {
                    p2temp = ((LinkedList<Map.Entry<Player, Boolean>>) queue).remove(i).getKey();
                    break;
                }
            }
            final Player p2 = p2temp;
            playerClasses.get(p1.getUniqueId().toString()).applyToPlayer(p1, false);
            playerClasses.get(p2.getUniqueId().toString()).applyToPlayer(p2, false);
            activePlayers[0] = p1;
            activePlayers[1] = p2;
            ongoing = true;
            ready = false;
            Location eastGate = new Location(pvpWorld, 42099, 64, 42069, 90, 0);
            Location westGate = new Location(pvpWorld, 42039, 64, 42069, -90, 0);
            p1.teleport(eastGate, PlayerTeleportEvent.TeleportCause.PLUGIN);
            p2.teleport(westGate, PlayerTeleportEvent.TeleportCause.PLUGIN);
            timer = 5;
            BukkitRunnable r = new BukkitRunnable() {
                @Override
                public void run() {
                    p1.sendMessage(ChatColor.BOLD + "Match starts in " + ChatColor.RED + "" + ChatColor.BOLD + timer + "");
                    p2.sendMessage(ChatColor.BOLD + "Match starts in " + ChatColor.RED + "" + ChatColor.BOLD + timer + "");
                    timer--;
                    if (timer <= 0) {
                        p1.sendTitle("Match Started!", "", 10, 30, 10);
                        p2.sendTitle("Match Started!", "", 10, 30, 10);
                        cancel();
                    }
                }
            };
            r.runTaskTimer(Main.thisPlugin, 10, 20);
        } else if (queue.size() >= 1 && !pvp) {
            Location eastGate = new Location(pvpWorld, 42099, 64, 42069, 90, 0);
            Location westGate = new Location(pvpWorld, 42039, 64, 42069, -90, 0);
            final Player p1 = queue.pop().getKey();
            final LivingEntity p2 = generateZombie((int) Math.floor(Math.random() * 2500) + 1, pvpWorld, westGate);
            playerClasses.get(p1.getUniqueId().toString()).applyToPlayer(p1, false);
            activePlayers[0] = p1;
            activePlayers[1] = (LivingEntity) p2;
            ongoing = true;
            ready = false;
            p1.teleport(eastGate, PlayerTeleportEvent.TeleportCause.PLUGIN);
            p2.teleport(westGate, PlayerTeleportEvent.TeleportCause.PLUGIN);
            timer = 5;
            BukkitRunnable r = new BukkitRunnable() {
                @Override
                public void run() {
                    p1.sendMessage(ChatColor.BOLD + "Match starts in " + ChatColor.RED + "" + ChatColor.BOLD + timer + "");
                    timer--;
                    if (timer < 0) {
                        p1.sendTitle("Match Started!", "", 10, 30, 10);
                        cancel();
                    }
                }
            };
            r.runTaskTimer(Main.thisPlugin, 10, 20);
        }
    }
    public static World pvpWorld = Main.thisPlugin.getServer().getWorld("superflat");

    public static void stopMatch(int winner) {
        if (pvpWorld == null) {
            pvpWorld = Main.thisPlugin.getServer().getWorld("superflat");
        }
        final LivingEntity p1 = activePlayers[0];
        final LivingEntity p2 = activePlayers[1];
        Bukkit.broadcastMessage(ChatColor.BOLD + (winner == 1 ? p1 instanceof Player ? ((Player) p1).getDisplayName() : p1.getName() : p2 instanceof Player ? ((Player) p2).getDisplayName() : p2.getName()) + ChatColor.RESET + ChatColor.BOLD + " fricked " + (winner == 2 ? p1 instanceof Player ? ((Player) p1).getDisplayName() : p1.getName() : p2 instanceof Player ? ((Player) p2).getDisplayName() : p2.getName()) + ChatColor.RESET + ChatColor.BOLD + " up in the arena!");
        activePlayers[0] = null;
        activePlayers[1] = null;
        if (p1 != null) {
            p1.teleport(new Location(pvpWorld, 42069, 72, 42115, 180, 0));
        }
        if (p2 != null) {
            if (p2 instanceof Player) {
                p2.teleport(new Location(pvpWorld, 42069, 72, 42115, 180, 0));
            } else {
                p2.remove();
            }
        }
        for (Entity e : pvpWorld.getEntities()) {
            double px = e.getLocation().getX();
            double pz = e.getLocation().getZ();
            double dist = Math.sqrt(Math.pow(px - 42069, 2) + Math.pow(pz - 42069, 2));
            if (dist <= 30) {
                if (e instanceof Player == false) {
                    e.remove();
                }
            }
        }
        BukkitRunnable restore = new BukkitRunnable() {
            @Override
            public void run() {
                for (int x = 42029; x <= 42109; x++) {
                    for (int y = 56; y <= 255; y++) {
                        for (int z = 42029; z <= 42109; z++) {
                            Object[] saved = arenaBackup[x - 42029][y - 56][z - 42029];
                            pvpWorld.getBlockAt(x, y, z).setType((Material) saved[0]);
                            pvpWorld.getBlockAt(x, y, z).setBlockData((BlockData) saved[1]);
                        }
                    }
                }
            }
        };

        restore.run();
        ongoing = false;
        ready = queue.size() > 1;
        if (ready) {
            if (queue.size() > 1) {
                startMatch(true);
            } else {
                startMatch(false);
            }
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public static void playerMove(PlayerMoveEvent event) {
        if (ongoing && timer > 0) {
            Player player = event.getPlayer();
            if (activePlayers[0].getUniqueId().compareTo(player.getUniqueId()) == 0 || activePlayers[1].getUniqueId().compareTo(player.getUniqueId()) == 0) {
                event.setCancelled(true);
            }
        } else if (ongoing) {
            Player player = event.getPlayer();
            if (activePlayers[0].getUniqueId().compareTo(player.getUniqueId()) == 0 || activePlayers[1].getUniqueId().compareTo(player.getUniqueId()) == 0) {
                double px = player.getLocation().getX();
                double pz = player.getLocation().getZ();
                double dist = Math.sqrt(Math.pow(px - 42069, 2) + Math.pow(pz - 42069, 2));
                if (dist > 31) {
                    player.setHealth(0);
                }
            }
        }
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public static void playerLeave(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        if (ongoing) {
            if (activePlayers[0].getUniqueId().compareTo(player.getUniqueId()) == 0 || activePlayers[1].getUniqueId().compareTo(player.getUniqueId()) == 0) {
                stopMatch(activePlayers[0].getUniqueId().compareTo(player.getUniqueId()) == 0 ? 2 : 1);
            }
        }
        Iterator<Map.Entry<Player, Boolean>> it = queue.iterator();
        while (it.hasNext()) {
            Map.Entry<Player, Boolean> pair = it.next();
            if (pair.getKey().getUniqueId().compareTo(player.getUniqueId()) == 0) {
                it.remove();
                for (int i = 0; i < queue.size(); i++) {
                    Player cur = ((LinkedList<Map.Entry<Player, Boolean>>) queue).get(i).getKey();
                    cur.sendMessage("A player has left the queue so you've moved into position " + (queue.size() - i) + ".");
                }
                break;
            }
        }
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public static void playerDeath(org.bukkit.event.entity.EntityDeathEvent event) {
        if (ongoing) {
            if (event.getEntity() instanceof Player || event.getEntity() instanceof Zombie) {
                LivingEntity player = (LivingEntity) event.getEntity();
                if (activePlayers[0].getUniqueId().compareTo(player.getUniqueId()) == 0 || activePlayers[1].getUniqueId().compareTo(player.getUniqueId()) == 0) {

                    stopMatch(activePlayers[0].getUniqueId().compareTo(player.getUniqueId()) == 0 ? 2 : 1);
                }
            }
        }
    }

    @EventHandler(priority = EventPriority.NORMAL)
    public static void hungerChange(org.bukkit.event.entity.FoodLevelChangeEvent event) {
        if (ongoing) {
            LivingEntity player = (LivingEntity) event.getEntity();
            if (activePlayers[0].getUniqueId().compareTo(player.getUniqueId()) == 0 || activePlayers[1].getUniqueId().compareTo(player.getUniqueId()) == 0) {
                event.setCancelled(true);
            }
        }
    }

    public static Zombie generateZombie(int level, World w, Location l) {
        Zombie z = (Zombie) w.spawnEntity(l, EntityType.ZOMBIE);
        z.setBaby(false);
        z.getAttribute(Attribute.GENERIC_MAX_HEALTH).setBaseValue(Math.min(2 * level, 2048));
        z.setHealth(Math.min(2 * level, 2048));
        double speed = 0.08 + (Math.log(level <= 0 ? 1 : level) / Math.log(1.333333));
        Bukkit.getConsoleSender().sendMessage("Zombie level " + level + " summoned with speed " + speed + "bps (" + bpsToMovementSpeed(speed) + ")");
        z.getAttribute(Attribute.GENERIC_MOVEMENT_SPEED).setBaseValue(bpsToMovementSpeed(speed));
        z.setCustomName(ChatColor.GREEN + "Zombie " + ChatColor.DARK_PURPLE + "[Lvl. " + level + "]");
        z.setCustomNameVisible(true);
        z.getEquipment().setHelmet(createEnchantedItem(Material.DIAMOND_HELMET, "Zombie Helmet", 1, new Object[][]{{Enchantment.PROTECTION_ENVIRONMENTAL, 4}}, true, true));
        z.getEquipment().setChestplate(createEnchantedItem(Material.DIAMOND_CHESTPLATE, "Zombie Chestplate", 1, new Object[][]{{Enchantment.PROTECTION_ENVIRONMENTAL, 4}}, true, true));
        z.getEquipment().setLeggings(createEnchantedItem(Material.DIAMOND_LEGGINGS, "Zombie Leggings", 1, new Object[][]{{Enchantment.PROTECTION_ENVIRONMENTAL, 4}}, true, true));
        z.getEquipment().setBoots(createEnchantedItem(Material.DIAMOND_BOOTS, "Zombie Boots", 1, new Object[][]{{Enchantment.PROTECTION_ENVIRONMENTAL, 4}}, true, true));
        z.setRemoveWhenFarAway(false);
        return z;
    }
}
