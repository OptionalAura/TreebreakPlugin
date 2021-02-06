package main.java.treebreaker.plugin.features;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import main.java.treebreaker.plugin.misc.Permissions;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.TextComponent;

import org.apache.commons.lang.WordUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.enchantments.EnchantmentWrapper;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.EnchantmentStorageMeta;
import org.bukkit.inventory.meta.ItemMeta;

/*
    @author Daniel Allen
    14-Aug-2019
 */
public class EZEnchant {

    public static boolean run(CommandSender sender, Command cmd, String cmdLabel, String[] args) {
        if (!sender.isOp() && !sender.hasPermission(Permissions.getPerm("TreeEnchants"))) {
            return true;
        }
        if (args.length != 0) {
            if (args.length == 1) {
                sender.sendMessage(ChatColor.RED + "Improper format. Use /ezenchant <Enchantment> <Level> [Player]" + ChatColor.RESET);
                return true;
            }
            Enchantment ench = null;
            for (Enchantment e : Enchantment.values()) {
                if (e.getKey().getKey().equalsIgnoreCase(args[0])) {
                    ench = e;
                    break;
                }
            }
            if (ench != null) {
                try {
                    int level = Integer.parseInt(args[1]);
                    Player target = null;
                    if (args.length == 3) {
                        if (args[2].equalsIgnoreCase("-")) {
                            if (sender instanceof Player) {
                                target = ((Player) sender);
                            }
                        } else {
                            String playerTargetName = args[2];
                            Player targetPlayer = Bukkit.getPlayerExact(playerTargetName);
                            if (targetPlayer != null) {
                                target = targetPlayer;
                            }
                        }
                    } else if (args.length == 2) {
                        if (sender instanceof Player) {
                            target = ((Player) sender);
                        }
                    }
                    if (target != null) {
                        ItemStack item = target.getInventory().getItemInMainHand();
                        if (item == null || item.getType().equals(Material.AIR)) {
                            sender.sendMessage(ChatColor.RED + "You must be holding an item in your hand to do this" + ChatColor.RESET);
                            return true;
                        }
                        if (item.getType().equals(Material.ENCHANTED_BOOK)) {
                            EnchantmentStorageMeta im = (EnchantmentStorageMeta) item.getItemMeta();
                            if (level == 0) {
                                im.removeStoredEnchant(ench);
                            } else {
                                im.addStoredEnchant(ench, level, true);
                            }
                            item.setItemMeta(im);
                        } else {
                            ItemMeta im = item.getItemMeta();
                            if (im != null) {
                                if (level == 0) {
                                    im.removeEnchant(ench);
                                } else {
                                    im.addEnchant(ench, level, true);
                                }
                                //Utils.addEnchantmentsToLore(im);
                                item.setItemMeta(im);
                            }
                        }

                    } else {
                        return false;
                    }
                } catch (NumberFormatException ex) {
                    sender.sendMessage(ChatColor.RED + "Cannot read level" + ChatColor.RESET);
                    return true;
                }
            } else {
                sender.sendMessage(ChatColor.RED + "Unknown enchantment" + ChatColor.RESET);
                return true;
            }
        }
        if ((args.length == 0 || (args.length == 3 && args[2].equalsIgnoreCase("-"))) && sender instanceof Player) {
            ezenchant((Player) sender, ((Player) sender).getInventory().getItemInMainHand());
            return true;
        }
        return true;
    }

    private static void ezenchant(Player sender, final ItemStack item) {
        if (item == null || item.getType().equals(Material.AIR)) {
            sender.sendMessage(ChatColor.RED + "You must be holding an item in your hand to do this" + ChatColor.RESET);
            return;
        }
        //get all enchantments
        List<Enchantment> list = new ArrayList<>();
        //list.addAll(Arrays.asList(EnchantmentWrapper.values()));

        for(Enchantment e : EnchantmentWrapper.values()){
            if(e.canEnchantItem(item)){
                list.add(e);
            }
        }
        
        //sort enchantments first by if the item can normally have that enchantment (inverted), then by name
        list.sort(new Comparator<Enchantment>() {
            @Override
            public int compare(Enchantment o1, Enchantment o2) {
                if (o1.canEnchantItem(item) && o2.canEnchantItem(item)) {
                    return o1.getKey().getKey().compareTo(o2.getKey().getKey());
                } else {
                    return o1.canEnchantItem(item) ? 1 : (item.getType().equals(Material.BOW) && o1.equals(Enchantment.MULTISHOT)) ? 1 : -1;
                }
            }

        });

        //send a textcomponent to the user for each enchantment
        for (int i = 0; i < list.size(); i++) {
            Enchantment ench = list.get(i);
            int levelApplied = item.getEnchantmentLevel(ench);
            TextComponent enchTextComp = new TextComponent((ench.canEnchantItem(item) ? ChatColor.DARK_GREEN : (item.getType().equals(Material.BOW) && ench.equals(Enchantment.MULTISHOT)) ? ChatColor.DARK_GREEN : ChatColor.WHITE) + WordUtils.capitalizeFully(ench.getKey().getKey(), new char[]{' ', '_'}).replaceAll("_", " ") + " ");
            TextComponent inputText = new TextComponent((levelApplied > ench.getMaxLevel() ? ChatColor.WHITE + " " + levelApplied : ChatColor.GRAY + "_"));
            inputText.setClickEvent(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, "/ezenchant " + ench.getKey().getKey() + " " + ench.getMaxLevel()));
            enchTextComp.addExtra(inputText);
            for (int z = 0; z <= ench.getMaxLevel(); z++) {
                TextComponent levelText = new TextComponent((levelApplied == z ? ChatColor.WHITE : ChatColor.GRAY) + "" + z + " ");
                levelText.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/ezenchant " + ench.getKey().getKey() + " " + z + " -"));
                enchTextComp.addExtra(levelText);
            }
            sender.spigot().sendMessage(enchTextComp);
        }
    }
}
