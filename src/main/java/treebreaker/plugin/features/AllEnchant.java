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

import main.java.treebreaker.plugin.misc.Permissions;
import org.apache.commons.lang.WordUtils;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.EnchantmentStorageMeta;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import static main.java.treebreaker.plugin.utils.Utils.*;

/**
 *
 * @author Daniel Allen
 */
public class AllEnchant {

    public static boolean run(CommandSender player, Command cmd, String cmdLabel, String[] args) {
        if (player instanceof Player == false) {
            return false;
        }
        if(!player.isOp() && !player.hasPermission(Permissions.getPerm("TreeEnchants"))){
            return true;
        }
        if (args.length < 1 || args.length > 2) {
            player.sendMessage("§c:Error: §4Incorrect format. Use §a/allEnchant <level|\"max\"> [unsafe?] ");
            return true;
        }
        ItemStack holding = ((Player) player).getInventory().getItemInMainHand();
        if (holding.getType() == Material.AIR) {
            player.sendMessage("§cError: §4You must be holding an item to perform this command");
            return false;
        }
        boolean allowUnsafe = args.length == 2 && args[1].equalsIgnoreCase("true");
        boolean maxEnchLimit = false;
        int lvl = 1;
        if (args[0].equalsIgnoreCase("max")) {
            maxEnchLimit = true;
        } else {
            try {
                lvl = Integer.parseInt(args[0].replaceAll("[^0-9]", ""));
                if (lvl < 0 || lvl > 32767) {
                    throw new NumberFormatException("Level must be between 0 and 32 767 (You used \"" + args[0] + "\"");
                }
            } catch (Exception ex) {
                player.sendMessage("§c§lError: §r§4" + ex.getMessage());
                return false;
            }
        }
        List<String> enchantments = new ArrayList<>();
        if (holding.getType() == Material.ENCHANTED_BOOK) {
            EnchantmentStorageMeta meta = (EnchantmentStorageMeta) holding.getItemMeta();
            if (meta == null) {
                return false;
            }
            for (Enchantment ench : enchList) {
                if (allowUnsafe) {
                    if (lvl > 0) {
                        meta.addStoredEnchant(ench, (maxEnchLimit ? ench.getMaxLevel() : lvl), true);

                    } else {
                        meta.removeStoredEnchant(ench);

                    }

                } else {
                    if (ench.canEnchantItem(holding)) {
                        if (lvl > 0) {
                            meta.addStoredEnchant(ench, (maxEnchLimit ? ench.getMaxLevel() : lvl), true);

                        } else {
                            meta.removeEnchant(ench);

                        }
                    }
                }
            }
            holding.setItemMeta(meta);
            return true;
        } else {
            ItemMeta meta = holding.getItemMeta();
            if (meta == null) {
                return false;
            }
            for (Enchantment ench : enchList) {
                if (allowUnsafe) {
                    if (lvl > 0) {
                        meta.addEnchant(ench, (maxEnchLimit ? ench.getMaxLevel() : lvl), true);
                        String keyKey = ench.getKey().getKey();
                        enchantments.add(enchantmentNamePrefix + WordUtils.capitalizeFully(keyKey, new char[]{' ', '_'}).replaceAll("_", " ") + enchantmentLevelPrefix + intToRomanNumeral(maxEnchLimit ? ench.getMaxLevel() : lvl));

                    } else {
                        meta.removeEnchant(ench);

                    }
                } else {
                    if (ench.canEnchantItem(holding)) {
                        if (lvl > 0) {
                            meta.addEnchant(ench, (maxEnchLimit ? ench.getMaxLevel() : lvl), true);

                            String keyKey = ench.getKey().getKey();
                            enchantments.add(enchantmentNamePrefix + WordUtils.capitalizeFully(keyKey, new char[]{' ', '_'}).replaceAll("_", " ") + enchantmentLevelPrefix + intToRomanNumeral(maxEnchLimit ? ench.getMaxLevel() : lvl));
                        } else {
                            meta.removeEnchant(ench);

                        }
                    }
                }
            }
            enchantments.sort(new Comparator<String>() {
                @Override
                public int compare(String o1, String o2) {
                    return o1.compareToIgnoreCase(o2);
                }
            });
            //meta.setLore(enchantments);
            //meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
            holding.setItemMeta(meta);
            return true;
        }
    }
}
