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

/*
    @author Daniel Allen
    14-Aug-2019
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
