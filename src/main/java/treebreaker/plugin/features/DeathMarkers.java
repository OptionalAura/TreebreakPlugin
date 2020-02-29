/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package main.java.treebreaker.plugin.features;

import java.util.concurrent.ConcurrentHashMap;
import main.java.treebreaker.plugin.Main;
import main.java.treebreaker.plugin.misc.ActionBarAPI;
import main.java.treebreaker.plugin.utils.Time;
import main.java.treebreaker.plugin.utils.Utils;
import static main.java.treebreaker.plugin.utils.Utils.getProperty;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.scheduler.BukkitRunnable;

/**
 *
 * @author Daniel Allen
 */
public class DeathMarkers implements Listener {

    public static String DEATH_MARKER_ENABLED_TAG = "show_death_nametag",
            DEATH_MARKER_TIME_TAG = "death_marker_time_visible",
            DEATH_COMPASS_ENABLED_TAG = "death_compass_enabled",
            DEATH_LOCATION_MESSAGE_TAG = "death_location_message";

    public static boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (sender instanceof Player) {
            if (deathTimerMap.containsKey((Player) sender)) {
                long deathTime = deathTimerMap.get((Player) sender);
                long curTime = ((Player) sender).getLocation().getWorld().getFullTime();
                long timeRemaining = getProperty(DEATH_MARKER_TIME_TAG, 300l) - (curTime - deathTime);
                if (timeRemaining <= 0) {
                    deathTimerMap.remove((Player) sender);
                    Location loc = deathCompassMap.get((Player) sender);
                    if (loc != null) {
                        sender.sendMessage(ChatColor.DARK_RED + "Your last death location was (" + loc.getBlockX() + ", " + loc.getBlockY() + ", " + loc.getBlockZ() + ")" + ChatColor.RESET);
                        sender.sendMessage(ChatColor.DARK_RED + "Your items have already despawned");
                    }
                } else {
                    deathTimerMap.remove((Player) sender);
                    Location loc = deathCompassMap.get((Player) sender);
                    if (loc != null) {
                        sender.sendMessage(ChatColor.DARK_RED + "Your last death location was (" + loc.getBlockX() + ", " + loc.getBlockY() + ", " + loc.getBlockZ() + ")" + ChatColor.RESET);
                        Time time = new Time(Utils.ticksToMillis(timeRemaining));
                        time.setShouldPrintMilliseconds(false);
                        sender.sendMessage(ChatColor.DARK_RED + "Your items will despawn in " + time + ChatColor.RESET);
                    }
                }
            } else {
                sender.sendMessage(ChatColor.DARK_RED + "You don't have a previous death location saved!" + ChatColor.RESET);
            }
            return true;
        }
        return false;
    }

    private static ConcurrentHashMap<Player, Location> deathCompassMap = new ConcurrentHashMap<>();
    private static ConcurrentHashMap<Player, Long> deathTimerMap = new ConcurrentHashMap<>();

    @EventHandler
    public void onPlayerDeath(final PlayerDeathEvent event) {
        if (getProperty(DEATH_COMPASS_ENABLED_TAG, true)) {
            final Player p = event.getEntity();
            deathCompassMap.put(p, event.getEntity().getLocation());
            deathTimerMap.put(p, p.getLocation().getWorld().getFullTime());
            new BukkitRunnable() {
                @Override
                public void run() {
                    deathCompassMap.remove(p);
                    deathTimerMap.remove(p);
                }
            }.runTaskLater(Main.thisPlugin, 20 * 60 * 5);
        }
        if (getProperty(DEATH_MARKER_ENABLED_TAG, true)) {

        }
        if (getProperty(DEATH_LOCATION_MESSAGE_TAG, true)) {
            Location loc = event.getEntity().getLocation();
            event.getEntity().sendMessage(ChatColor.DARK_RED + "Your death location: (" + loc.getBlockX() + ", " + loc.getBlockY() + ", " + loc.getBlockZ() + ")" + ChatColor.RESET);
        }
    }

    @EventHandler
    public void onPlayerMove(final PlayerMoveEvent event) {
        if (!event.isCancelled()) {
            if (getProperty(DEATH_COMPASS_ENABLED_TAG, true)) {
                final Player p = event.getPlayer();
                if (deathTimerMap.containsKey(p)) {
                    Location deathLocBukkit = deathCompassMap.get(p);
                    Location curLocBukkit = event.getTo();

                    curLocBukkit.getYaw();

                    StringBuilder sb = new StringBuilder();

                    ActionBarAPI.sendActionBar(p, sb.toString());
                }
            }
        }
    }
}
