package main.java.dp.plugin.commands;

import java.util.ArrayList;
import java.util.List;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

/*
    @author Daniel Allen
    23-Nov-2019
 */
public class LoreCreator {

    public static boolean run(CommandSender sender, Command cmd, String cmdLabel, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            ItemStack heldItem = player.getInventory().getItemInMainHand();
            if (heldItem != null && heldItem.getType() != Material.AIR) {
                if (args.length > 0) {
                    StringBuilder sb = new StringBuilder(args.length);
                    for (int i = 0; i < args.length - 1; i++) {
                        sb.append(args[i]).append(" ");
                    }
                    sb.append(args[args.length - 1]);
                    List<String> lore = new ArrayList<>();
                    for (String l : sb.toString().split("\\n")) {
                        player.sendMessage("Found new line");
                        StringBuilder parsed = new StringBuilder(l.length());
                        char[] chars = l.toCharArray();
                        for (int z = 0; z < chars.length; z++) {
                            char cur = chars[z];
                            if (cur == '&') {
                                if (z > 0 && chars[z - 1] != '\\') {
                                    parsed.append("§");
                                } else {
                                    parsed.append("&");
                                }
                            } else {
                                parsed.append(cur);
                            }
                        }
                        parsed.append("\n");
                        lore.add(parsed.toString());
                    }
                    ItemMeta heldMeta = heldItem.getItemMeta();
                    heldMeta.setLore(lore);
                    heldItem.setItemMeta(heldMeta);
                    player.getInventory().setItemInMainHand(heldItem);
                } else {
                    ItemMeta heldMeta = heldItem.getItemMeta();
                    heldMeta.setLore(null);
                    heldItem.setItemMeta(heldMeta);
                    player.getInventory().setItemInMainHand(heldItem);
                }
                return true;
            } else {
                return false;
            }
        }
        return false;
    }
}
