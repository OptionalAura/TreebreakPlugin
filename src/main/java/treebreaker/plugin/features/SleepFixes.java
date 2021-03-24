/*
 * Copyright (C) 2021 Daniel Allen
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package main.java.treebreaker.plugin.features;

import main.java.treebreaker.plugin.Main;
import main.java.treebreaker.plugin.utils.Utils;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.concurrent.ConcurrentHashMap;

import static main.java.treebreaker.plugin.Main.thisPlugin;

/**
 *
 * @author Daniel Allen
 */
public class SleepFixes implements Listener {

    public static final String ONE_SLEEPING_PLAYER_TAG = "require_one_sleeping_player";

    private final ConcurrentHashMap<Player, Long> sleepStartTimes = new ConcurrentHashMap<>();

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
