package main.java.dp.plugin.features;

import java.util.HashSet;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.inventory.PrepareAnvilEvent;
import org.bukkit.inventory.AnvilInventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

/*
    @author Daniel Allen
    14-Aug-2019
 */
public class ColoredNames implements Listener {

    @EventHandler
    public void onPrepareAnvil(PrepareAnvilEvent e) {
        if (e.getResult() != null) {
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
                        if(e.getInventory().getItem(0) != null)
                        {
                            if(e.getInventory().getItem(0).hasItemMeta()){
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
