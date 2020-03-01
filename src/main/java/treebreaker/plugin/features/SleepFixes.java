/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package main.java.treebreaker.plugin.features;

import java.util.concurrent.ConcurrentHashMap;
import main.java.treebreaker.plugin.Main;
import static main.java.treebreaker.plugin.Main.thisPlugin;
import main.java.treebreaker.plugin.utils.Utils;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.scheduler.BukkitRunnable;

/**
 *
 * @author Daniel Allen
 */
public class SleepFixes implements Listener {

    public static final String ONE_SLEEPING_PLAYER_TAG = "require_one_sleeping_player";

    private ConcurrentHashMap<Player, Long> sleepStartTimes = new ConcurrentHashMap<>();

    @EventHandler
    public void onPlayerSleep(final org.bukkit.event.player.PlayerBedEnterEvent event) {
        if (Utils.getProperty(ONE_SLEEPING_PLAYER_TAG, true)) {
            final Player player = event.getPlayer();
            long currentTickTime = Main.getCurrentTick();
            sleepStartTimes.put(player, currentTickTime);
            BukkitRunnable br = new BukkitRunnable() {
                @Override
                public void run() {
                    if (player.isSleeping() && sleepStartTimes.containsKey(player) && Main.getCurrentTick() - sleepStartTimes.get(player) > 100) {
                        player.getWorld().setTime(1000);
                        player.setBedSpawnLocation(event.getBed().getLocation());
                    }
                }
            };
            br.runTaskLater(thisPlugin, 101);
        }
    }

    @EventHandler
    public void onPlayerLeaveBed(final org.bukkit.event.player.PlayerBedLeaveEvent event) {
        if (Utils.getProperty(ONE_SLEEPING_PLAYER_TAG, true)) {
            final Player player = event.getPlayer();
            sleepStartTimes.remove(player);
        }
    }
}
