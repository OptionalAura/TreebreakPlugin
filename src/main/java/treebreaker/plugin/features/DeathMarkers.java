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
package main.java.treebreaker.plugin.features;

import main.java.treebreaker.plugin.Main;
import main.java.treebreaker.plugin.misc.ActionBarAPI;
import main.java.treebreaker.plugin.utils.Time;
import main.java.treebreaker.plugin.utils.Utils;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
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

import java.util.concurrent.ConcurrentHashMap;

import static main.java.treebreaker.plugin.utils.Utils.getProperty;

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
            switch (cmd.getName().toLowerCase()) {
                case "deathlocation":

                    if (deathTimerMap.containsKey(sender)) {
                        Object inMap = targetCompassMap.get(sender);
                        if (inMap instanceof Location) {
                            long timeRemaining = getTicksRemainingBeforeItemsDespawn((Player) sender);
                            Location loc = (Location) targetCompassMap.get(sender);
                            if (timeRemaining <= 0) {
                                if (loc != null) {
                                    sendDeathLocationToPlayer((Player) sender);
                                    sender.sendMessage(ChatColor.DARK_RED + "Your items have already despawned");
                                }
                            } else {
                                if (loc != null) {
                                    sendDeathLocationToPlayer((Player) sender);
                                    Time time = new Time(Utils.ticksToMillis(timeRemaining));
                                    time.setShouldPrintMilliseconds(false);
                                    sender.sendMessage(ChatColor.RED + "Your items will despawn in " + time + ChatColor.RESET);
                                }
                            }
                        }
                    } else {
                        sendDeathLocationToPlayer((Player) sender);
                    }
                    return true;
                case "track":
                    String playerName = args[0];
                    Player target = Bukkit.getPlayer(playerName);
                    if (target != null) {
                        targetCompassMap.put((Player) sender, target);
                        deathTimerMap.put((Player) sender, 20 * getProperty(DEATH_MARKER_TIME_TAG, 300L));
                    }
                    return false;
            }
        }
        return false;
    }

    private static final ConcurrentHashMap<Player, Object> targetCompassMap = new ConcurrentHashMap<>();
    private static final ConcurrentHashMap<Player, Long> deathTimerMap = new ConcurrentHashMap<>();

    private static long getTicksRemainingBeforeItemsDespawn(Player player) {
        Long deathTime = deathTimerMap.get(player);
        if (deathTime == null) {
            return -1;
        }
        long curTime = Main.getCurrentTick();
        return 20 * getProperty(DEATH_MARKER_TIME_TAG, 300L) - (curTime - deathTime);
    }
    
    private static void sendDeathLocationToPlayer(Player p){
        Location loc = (Location) targetCompassMap.get(p);
        TextComponent deathText = new TextComponent(ChatColor.WHITE + "Your last death location was " + ChatColor.GREEN + "(" + loc.getBlockX() + ", " + loc.getBlockY() + ", " + loc.getBlockZ() + ")" + ChatColor.RESET);
        deathText.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/tp @s " + loc.getBlockX() + " " + loc.getBlockY() + " " + loc.getBlockZ()));
        p.spigot().sendMessage(deathText);
    }
    
    @EventHandler
    public void onPlayerDeath(final PlayerDeathEvent event) {
        final Player p = event.getEntity();
        targetCompassMap.put(p, event.getEntity().getLocation());
        deathTimerMap.put(p, Main.getCurrentTick());
        if (getProperty(DEATH_MARKER_ENABLED_TAG, true)) {

        }
        if (getProperty(DEATH_LOCATION_MESSAGE_TAG, true)) {
            sendDeathLocationToPlayer(p);
        }
    }

    @EventHandler
    public void onPlayerMove(final PlayerMoveEvent event) {
        if (!event.isCancelled()) {
            if (getProperty(DEATH_COMPASS_ENABLED_TAG, true)) {
                final Player p = event.getPlayer();
                if (targetCompassMap.containsKey(p)) {
                    long ticksRemaining = getTicksRemainingBeforeItemsDespawn(p);
                    if (ticksRemaining > 0) {
                        Object inMap = targetCompassMap.get(p);
                        Location tarLoc;
                        if (inMap instanceof Location) {
                            tarLoc = (Location) inMap;
                        } else {
                            tarLoc = ((Player) inMap).getLocation();
                        }
                        Location curLoc = event.getTo();

                        Vector lookVec = curLoc.getDirection().clone().setY(0).normalize();
                        Vector moveVec = tarLoc.toVector().subtract(curLoc.toVector()).clone().setY(0).normalize();
                        //Vector southVec = new Vector(0, 0, 1);
                        double angleLook = Math.atan2(lookVec.getZ() - 1, lookVec.getX());
                        double angleMove = Math.atan2(moveVec.getZ() - 1, moveVec.getX());
                        int dif = (int) Math.round(Math.toDegrees(angleLook - angleMove));
                        StringBuilder sb = new StringBuilder();
                        for (int i = 45; i > -45; i--) {
                            if (dif == i) {
                                sb.append("|");
                            } else if (dif + 1 == i || dif - 1 == i) {
                                sb.append(":");
                            } else {
                                sb.append(".");
                            }
                        }
                        ChatColor actionbarColor;
                        long ticks = getTicksRemainingBeforeItemsDespawn(p);
                        if(ticks < 600){
                            actionbarColor = ChatColor.BLACK;
                        } else if(ticks < 1200){
                            actionbarColor = ChatColor.DARK_RED;
                        } else if(ticks < 2400){
                            actionbarColor = ChatColor.RED;
                        } else if(ticks < 3600){
                            actionbarColor = ChatColor.GOLD;
                        } else if(ticks < 4800){
                            actionbarColor = ChatColor.YELLOW;
                        } else {
                            actionbarColor = ChatColor.WHITE;
                        }
                        ActionBarAPI.sendActionBar(p, actionbarColor + sb.toString() + ChatColor.RESET);
                    }
                }
            }
        }
    }
}
