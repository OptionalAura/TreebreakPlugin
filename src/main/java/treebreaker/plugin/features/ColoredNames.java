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

import main.java.treebreaker.plugin.utils.Utils;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.PrepareAnvilEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

/**
 *
 * @author Daniel Allen
 */
public class ColoredNames implements Listener {

    public static String ALLOW_CHAT_COLORS_TAG = "allow_chat_colors",
            ALLOW_ITEM_COLORS_TAG = "allow_item_name_colors";

    @EventHandler
    public void onPrepareAnvil(PrepareAnvilEvent e) {
        if (Utils.getProperty(ALLOW_ITEM_COLORS_TAG, true)) {
            if (e.getResult() != null && !e.getResult().getType().equals(Material.AIR)) {
                if (e.getInventory().getRenameText() != null) {
                    if (e.getInventory().getRenameText().isEmpty() == false) {
                        if (e.getInventory().getItem(1) == null) {
                            ItemStack outputItem = e.getResult();
                            if (outputItem == null) {
                                return;
                            }
                            ItemMeta outputMeta = e.getResult().getItemMeta();
                            if (outputMeta == null) {
                                return;
                            }
                            String coloredName = ChatColor.translateAlternateColorCodes('&', e.getInventory().getRenameText().replaceAll("§", "&"));
                            if (coloredName == null) {
                                return;
                            }
                            outputMeta.setDisplayName(coloredName);
                            outputItem.setItemMeta(outputMeta);
                        } else {
                            ItemStack outputItem = e.getResult();
                            if (outputItem == null) {
                                return;
                            }
                            ItemMeta outputMeta = e.getResult().getItemMeta();
                            if (outputMeta == null) {
                                return;
                            }
                            if (e.getInventory().getItem(0) != null) {
                                if (e.getInventory().getItem(0).hasItemMeta()) {
                                    if (e.getInventory().getItem(0).getItemMeta().hasDisplayName()) {
                                        String coloredName = e.getInventory().getItem(0).getItemMeta().getDisplayName();
                                        outputMeta.setDisplayName(coloredName);
                                        outputItem.setItemMeta(outputMeta);
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    @EventHandler
    public void playerSendMessage(AsyncPlayerChatEvent event) {
        if (Utils.getProperty(ALLOW_CHAT_COLORS_TAG, true)) {
            event.setMessage(ChatColor.translateAlternateColorCodes('&', event.getMessage().replaceAll("§", "&")) + ChatColor.RESET);
        }
    }
}
