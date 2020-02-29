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
import static main.java.treebreaker.plugin.utils.Utils.getProperty;
import org.bukkit.ChatColor;
import org.bukkit.Location;
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

    private ConcurrentHashMap<Player, Location> deathCompassMap = new ConcurrentHashMap<>();

    @EventHandler
    public void onPlayerDeath(final PlayerDeathEvent event) {
        if (getProperty(DEATH_COMPASS_ENABLED_TAG, true)) {
            final Player p = event.getEntity();
            deathCompassMap.put(p, event.getEntity().getLocation());
            new BukkitRunnable() {
                @Override
                public void run() {
                    deathCompassMap.remove(p);
                }
            }.runTaskLater(Main.thisPlugin, 20 * 60 * 5);
        }
        if (getProperty(DEATH_MARKER_ENABLED_TAG, true)) {

        }
        if (getProperty(DEATH_LOCATION_MESSAGE_TAG, true)) {
            Location loc = event.getEntity().getLocation();
            event.getEntity().sendMessage(ChatColor.DARK_RED + "Your death location: (" + loc.getX() + ", " + loc.getY() + ", " + loc.getZ() + ")" + ChatColor.RESET);
        }
    }

    @EventHandler
    public void onPlayerMove(final PlayerMoveEvent event) {
        if (!event.isCancelled()) {
            if (getProperty(DEATH_COMPASS_ENABLED_TAG, true)) {
                Player p = event.getPlayer();
                if (deathCompassMap.containsKey(p)) {
                    Location deathLocBukkit = deathCompassMap.get(p);
                    Location curLocBukkit = event.getTo();
                    Point2D deathLoc = new Point2D(deathLocBukkit.getX(), deathLocBukkit.getZ());
                    Point2D curLoc = new Point2D(curLocBukkit.getX(), curLocBukkit.getZ());
                    double rotY = curLocBukkit.getDirection().getY();
                    double rotX = curLocBukkit.getDirection().getY();
                    double xz = Math.cos(Math.toRadians(rotY));

                    double vecX = -xz * Math.sin(Math.toRadians(rotX));
                    double vecZ = xz * Math.cos(Math.toRadians(rotX));

                    Vec2D vec = new Vec2D(new Point2D(curLoc.getX() + vecX, curLoc.getY() + vecZ), curLoc);
                    double angleBetweenInDegrees = deathLoc.getVectorTo(curLoc).getAngleBetweenInDegrees(vec);
                    int angleDiv10 = (int) Math.round(angleBetweenInDegrees / 10);
                    int num = 36;
                    StringBuilder sb = new StringBuilder();
                    for (int i = 0; i < num; i++) {
                        if (i == 36 - angleDiv10) {
                            sb.append('|');
                        } else {
                            sb.append('.');
                        }
                    }
                    ActionBarAPI.sendActionBar(p, sb.toString());
                }
            }
        }
    }
}
