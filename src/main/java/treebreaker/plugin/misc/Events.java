/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package main.java.treebreaker.plugin.misc;

import main.java.treebreaker.plugin.Main;
import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

/**
 *
 * @author dsato
 */
public class Events implements Listener {
    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        if (Main.isUpdateAvailable()) {
            if (event.getPlayer().isOp()) {
                event.getPlayer().sendMessage(ChatColor.GREEN + Main.getUpdateMessage() + ChatColor.RESET);
            }
        }
    }
}
