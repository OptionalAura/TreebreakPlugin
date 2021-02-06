/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package main.java.treebreaker.plugin.misc;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import main.java.treebreaker.plugin.Main;
import static main.java.treebreaker.plugin.Main.thisPlugin;
import main.java.treebreaker.plugin.utils.Utils;
import main.java.treebreaker.plugin.utils.Version;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

/**
 *
 * @author dsato
 */
public class UpdateNotifier implements Listener{
    public UpdateNotifier(){
        
    }
    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event){
        if(event.getPlayer().isOp()){
            try {
                URL updateCheckURL = new URL("https://raw.githubusercontent.com/OptionalAura/TreebreakPlugin/master/src/plugin.yml");
                try ( BufferedReader br = new BufferedReader(new InputStreamReader(updateCheckURL.openStream()))) {
                    String line;
                    while ((line = br.readLine()) != null) {
                        if (line.startsWith("version: ")) {
                            line = Utils.stringAfter(line, "version: ");
                            Version updatedVersion = new Version(line);
                            if (Main.version.compareTo(updatedVersion) == -1) {
                                Main.updateAvailable = true;
                                Main.updateMessage = "There is an update available for " + Main.thisPlugin.getName() + "(v. " + Main.version.getVersionString() + " -> v. " + line.replaceAll("[^0-9.]", "") + ")";
                                Bukkit.getConsoleSender().sendMessage(ChatColor.GREEN + Main.updateMessage + ChatColor.RESET);
                            }
                        }
                    }
                }
            } catch (IOException | NumberFormatException e) {
                Bukkit.getConsoleSender().sendMessage(ChatColor.DARK_RED + "Error detecting updates for Treebreaker:" + ChatColor.RESET);
                Bukkit.getConsoleSender().sendMessage(ChatColor.DARK_RED + e.getLocalizedMessage() + ChatColor.RESET);
            }
        }
    }
}
