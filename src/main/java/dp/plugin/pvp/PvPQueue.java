package main.java.dp.plugin.pvp;

import java.util.HashMap;
import java.util.Map;
import java.util.Stack;
import java.util.UUID;
import main.java.dp.plugin.Main;
import main.java.dp.plugin.pvp.classKits.Kit;
import static main.java.dp.plugin.utils.Utils.bpsToMovementSpeed;
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
import org.bukkit.block.Block;
import org.bukkit.block.data.BlockData;
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
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

/*
    @author Daniel Allen
    24-Nov-2019
 */
public class PvPQueue implements Listener {

    private static Stack<Player> queue = new Stack<>();
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

    public static void joinQueue(Player p) {
        if (pvpWorld == null) {
            pvpWorld = Main.thisPlugin.getServer().getWorld("superflat");
        }
        if (playerClasses.containsKey(p.getUniqueId().toString())) {
            if (!queue.contains(p)) {
                queue.add(p);
                p.sendMessage("You joined the PvP queue. You are currently in position " + ChatColor.AQUA + queue.size());
                if (queue.size() > 1 && !ongoing) {
                    ready = true;
                } else if (!ongoing && Bukkit.getServer().getOnlinePlayers().size() == 1) {
                    ready = true;
                }
            } else {
                p.sendMessage("You are already in the queue.");
            }
            if (ready) {
                if(queue.size() > 1){
                    startMatch(true);
                } else {
                    startMatch(false);
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
    private static Object[][][][] arenaBackup = new Object[81][200][81][2];
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
                        for (int y = 56; y < 255; y++) {
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
        if (queue.size() > 1) {
            final Player p1 = queue.pop();
            final Player p2 = queue.pop();
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
        } else if (queue.size() == 1 && !pvp) {
            Location eastGate = new Location(pvpWorld, 42099, 64, 42069, 90, 0);
            Location westGate = new Location(pvpWorld, 42039, 64, 42069, -90, 0);
            final Player p1 = queue.pop();
            final LivingEntity p2 = generateZombie((int) Math.floor(Math.random() * 420) + 1, pvpWorld, westGate);
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
        BukkitRunnable restore = new BukkitRunnable() {
            @Override
            public void run() {
                for (int x = 42029; x <= 42109; x++) {
                    for (int y = 56; y < 255; y++) {
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
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public static void playerMove(PlayerMoveEvent event) {
        if (ongoing && timer > 0) {
            Player player = event.getPlayer();
            if (activePlayers[0].getUniqueId().compareTo(player.getUniqueId()) == 0 || activePlayers[1].getUniqueId().compareTo(player.getUniqueId()) == 0) {
                event.setCancelled(true);
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
        if (queue.contains(player)) {
            queue.remove(player);
            for (int i = 0; i < queue.size(); i++) {
                Player cur = queue.get(i);
                cur.sendMessage("A player has left the queue so you've moved into position " + (queue.size() - i) + ".");
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

    public static Zombie generateZombie(int level, World w, Location l) {
        Zombie z = (Zombie) w.spawnEntity(l, EntityType.ZOMBIE);
        z.setBaby(false);
        z.getAttribute(Attribute.GENERIC_MAX_HEALTH).setBaseValue(25 * Math.pow(2, level));
        z.getAttribute(Attribute.GENERIC_MOVEMENT_SPEED).setBaseValue(bpsToMovementSpeed(1 + (Math.log(level == 0 ? 1 : level)/Math.log(2))));
        z.setCustomName(ChatColor.GREEN + "Zombie " + ChatColor.DARK_PURPLE + "[Lvl. " + level + "]");
        z.setCustomNameVisible(true);
        z.addPotionEffect(new PotionEffect(PotionEffectType.FIRE_RESISTANCE, Integer.MAX_VALUE, 1, true, false));
        z.setRemoveWhenFarAway(true);
        return z;
    }
}
