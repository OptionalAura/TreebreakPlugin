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
package main.java.treebreaker.plugin.misc;

import main.java.treebreaker.plugin.Main;
import main.java.treebreaker.plugin.utils.Utils;
import main.java.treebreaker.plugin.utils.Version;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;

import static main.java.treebreaker.plugin.Main.updateAvailable;
import static main.java.treebreaker.plugin.Main.updateMessage;

/**
 *
 * @author Daniel Allen
 */
public class UpdateNotifier implements Listener{
    public UpdateNotifier(){
        
    }
    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        if (event.getPlayer().isOp()) {
            event.getPlayer().setDisplayName(ChatColor.RED + event.getPlayer().getDisplayName() + ChatColor.RESET);

            try {
                URL updateCheckURL = new URL("https://raw.githubusercontent.com/OptionalAura/TreebreakPlugin/master/src/plugin.yml");
                try ( BufferedReader br = new BufferedReader(new InputStreamReader(updateCheckURL.openStream()))) {
                    String line;
                    while ((line = br.readLine()) != null) {
                        if (line.startsWith("version: ")) {
                            line = Utils.stringAfter(line, "version: ");
                            Version updatedVersion = new Version(line);
                            if (Main.version.compareTo(updatedVersion) == -1) {
                                updateAvailable = true;
                                updateMessage = "There is an update available for " + Main.thisPlugin.getName() + "(v. " + Main.thisPlugin.getDescription().getVersion() + " -> v. " + line.replaceAll("[^0-9.]", "") + ")";
                                event.getPlayer().sendMessage(ChatColor.RED + updateMessage + ChatColor.RESET);
                                TextComponent updateText = new TextComponent(ChatColor.RED + "Click to update" + ChatColor.RESET);
                                updateText.setClickEvent(new ClickEvent(ClickEvent.Action.OPEN_URL, "https://github.com/OptionalAura/TreebreakPlugin/releases"));
                                event.getPlayer().spigot().sendMessage(updateText);
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
