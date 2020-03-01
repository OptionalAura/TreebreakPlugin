/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package main.java.treebreaker.plugin.features;

import java.util.concurrent.ConcurrentHashMap;
import main.java.treebreaker.plugin.Main;
import main.java.treebreaker.plugin.misc.ActionBarAPI;
import main.java.treebreaker.plugin.misc.Point2D;
import main.java.treebreaker.plugin.misc.Vec2D;
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
import org.bukkit.util.Vector;

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
                long timeRemaining = getTicksRemainingBeforeItemsDespawn((Player) sender);
                Location loc = deathCompassMap.get((Player) sender);
                if (timeRemaining <= 0) {
                    if (loc != null) {
                        sender.sendMessage(ChatColor.DARK_RED + "Your last death location was (" + loc.getBlockX() + ", " + loc.getBlockY() + ", " + loc.getBlockZ() + ")" + ChatColor.RESET);
                        sender.sendMessage(ChatColor.DARK_RED + "Your items have already despawned");
                    }
                } else {
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

    private static long getTicksRemainingBeforeItemsDespawn(Player player) {
        Long deathTime = deathTimerMap.get(player);
        if (deathTime == null) {
            return -1;
        }
        long curTime = Main.getCurrentTick();
        return 20 * getProperty(DEATH_MARKER_TIME_TAG, 300L) - (curTime - deathTime);
    }

    @EventHandler
    public void onPlayerDeath(final PlayerDeathEvent event) {
        final Player p = event.getEntity();
        deathCompassMap.put(p, event.getEntity().getLocation());
        deathTimerMap.put(p, Main.getCurrentTick());
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
                if (deathCompassMap.containsKey(p)) {
                    long ticksRemaining = getTicksRemainingBeforeItemsDespawn(p);
                    if (ticksRemaining > 0) {
                        Location deathLoc = deathCompassMap.get(p);
                        Location curLoc = event.getTo();
                        
                        Vector lookVec = curLoc.getDirection().clone().setY(0).normalize();
                        Vector moveVec = deathLoc.toVector().subtract(curLoc.toVector()).clone().setY(0).normalize();
                        //Vector southVec = new Vector(0, 0, 1);
                        double angleLook = Math.atan2(lookVec.getZ()-1, lookVec.getX());
                        double angleMove = Math.atan2(moveVec.getZ()-1, moveVec.getX());
                        int dif = (int) Math.round(Math.toDegrees(angleLook-angleMove));
                        StringBuilder sb = new StringBuilder();
                        for(int i = 45; i > -45; i--){
                            if(dif == i){
                                sb.append("|");
                            } else if (dif + 1 == i || dif - 1 == i) {
                                sb.append(":");
                            } else {
                                sb.append(".");
                            }
                        }
                        ActionBarAPI.sendActionBar(p, sb.toString());
                    }
                }
            }
        }
    }
}
