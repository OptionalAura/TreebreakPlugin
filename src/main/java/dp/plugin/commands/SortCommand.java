package main.java.dp.plugin.commands;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.UUID;
import java.util.stream.Collectors;
import main.java.dp.plugin.Main;
import main.java.dp.plugin.maps.Group;
import main.java.dp.plugin.maps.Groups;
import org.bukkit.FluidCollisionMode;
import org.bukkit.Material;
import org.bukkit.block.BlockState;
import org.bukkit.block.Chest;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.util.RayTraceResult;

/*
    @author Daniel Allen
    20-Aug-2019
 */
public class SortCommand {

    public static boolean SortStorage(final CommandSender player, Command cmd, String cmdLabel, String[] args) {
        if (player instanceof Player == false) {
            player.sendMessage("Only a player can issue this command!");
            return true;
        }
        final Player p = (Player) player;
        RayTraceResult rtr = ((Player) player).rayTraceBlocks(10, FluidCollisionMode.NEVER);
        if (rtr == null) {
            player.sendMessage("§4No chest was selected!");
            return true;
        }
        if (rtr.getHitBlock().isEmpty()) {
            player.sendMessage("§4No chest was selected!");
            return true;
        }
        BlockState hitBlock = ((Player) player).getWorld().getBlockAt(rtr.getHitBlock().getLocation()).getState();
        if (hitBlock == null) {
            player.sendMessage("Debug: You are not looking at a block!");
            return true;
        }
        if (hitBlock instanceof Chest) {
            Chest chest = (Chest) hitBlock;
            ArrayList<ItemStack> itemsInChest = new ArrayList<>();
            for (ItemStack is : chest.getInventory().getContents()) {
                if (is != null) {
                    if (!is.getType().equals(Material.AIR)) {
                        itemsInChest.add(is);
                    }
                }

            }
            final UUID uuid = p.getUniqueId();

            Comparator sorter = new Comparator<ItemStack>() {
                @Override
                public int compare(ItemStack o1, ItemStack o2) {
                    Group g1 = Groups.getGroup(o1.getType());
                    Group g2 = Groups.getGroup(o2.getType());
                    if (g1 == null) {
                        g1 = Groups.GROUP_OTHER;
                    }
                    if (g2 == null) {
                        g2 = Groups.GROUP_OTHER;
                    }
                    int g1order = g1.getOrder();
                    int g2order = g2.getOrder();

                    if (g1order == g2order) {
                        String itemName1 = (o1.hasItemMeta() ? (o1.getItemMeta().hasDisplayName() ? o1.getItemMeta().getDisplayName() : o1.getType().toString()) : o1.getType().toString()).replaceAll("Splash|Lingering|White|Orage|Magenta|Blue|Yellow|Lime|Pink|Gray|Cyan|Purple|Brown|Green|Red|Black|Light", "").trim();
                        String itemName2 = (o2.hasItemMeta() ? (o2.getItemMeta().hasDisplayName() ? o2.getItemMeta().getDisplayName() : o2.getType().toString()) : o2.getType().toString()).replaceAll("Splash|Lingering|White|Orage|Magenta|Blue|Yellow|Lime|Pink|Gray|Cyan|Purple|Brown|Green|Red|Black|Light", "").trim();
                        int stringComp = itemName1.compareToIgnoreCase(itemName2);

                        if (stringComp == 0) {
                            int countComp = o1.getAmount() > o2.getAmount() ? 1 : o1.getAmount() < o2.getAmount() ? -1 : 0;
                            if (countComp != 0) {
                                return countComp;
                            }
                            int maxEnch1 = 1;
                            int maxEnch2 = 1;
                            if (o1.hasItemMeta()) {
                                if (o1.getItemMeta().getEnchants().size() > 0) {
                                    Map<Enchantment, Integer> enchs = new HashMap<>();

                                    enchs.putAll(o1.getItemMeta().getEnchants());
                                    ItemMeta im1 = o1.getItemMeta();
                                    Iterator rem = im1.getEnchants().entrySet().iterator();
                                    while (rem.hasNext()) {
                                        Map.Entry<Enchantment, Integer> pair = (Map.Entry<Enchantment, Integer>) rem.next();
                                        im1.removeEnchant(pair.getKey());
                                    }
                                    Iterator put = enchs.entrySet().iterator();

                                    while (put.hasNext()) {
                                        Map.Entry<Enchantment, Integer> pair = (Map.Entry<Enchantment, Integer>) put.next();
                                        im1.addEnchant(pair.getKey(), pair.getValue(), true);
                                        if (pair.getValue() > maxEnch1) {
                                            maxEnch1 = pair.getValue();
                                        }
                                    }
                                    o1.setItemMeta(im1);
                                }
                            }

                            if (o2.hasItemMeta()) {
                                if (o2.getEnchantments().size() > 0) {
                                    Map<Enchantment, Integer> enchs = new HashMap<>();
                                    enchs.putAll(o2.getItemMeta().getEnchants());
                                    ItemMeta im2 = o2.getItemMeta();
                                    Iterator rem = im2.getEnchants().entrySet().iterator();
                                    while (rem.hasNext()) {
                                        Map.Entry<Enchantment, Integer> pair = (Map.Entry<Enchantment, Integer>) rem.next();
                                        im2.removeEnchant(pair.getKey());
                                        rem.remove();
                                    }
                                    Iterator put = enchs.entrySet().iterator();

                                    while (put.hasNext()) {
                                        Map.Entry<Enchantment, Integer> pair = (Map.Entry<Enchantment, Integer>) put.next();
                                        im2.addEnchant(pair.getKey(), pair.getValue(), true);
                                        if (pair.getValue() > maxEnch2) {
                                            maxEnch2 = pair.getValue();
                                        }
                                    }
                                    o2.setItemMeta(im2);
                                }
                            }

                            if (maxEnch1 > maxEnch2) {
                                return 1;
                            }
                            if (maxEnch2 > maxEnch1) {
                                return -1;
                            }

                            if (o1.hasItemMeta() && o2.hasItemMeta()) {
                                if (o1.getItemMeta().isUnbreakable() != o2.getItemMeta().isUnbreakable()) {
                                    return o1.getItemMeta().isUnbreakable() ? 1 : -1;
                                }
                            } else if (o1.hasItemMeta() || o2.hasItemMeta()) {
                                return o1.hasItemMeta() ? 1 : -1;
                            }

                            if (o1.getDurability() > o2.getDurability()) {
                                return 1;
                            } else if (o1.getDurability() < o2.getDurability()) {
                                return -1;
                            }

                            return o1.getType().getKey().toString().compareToIgnoreCase(o2.getType().getKey().toString());

                        } else if (stringComp < 0) {
                            return -1;
                        } else {
                            return 1;
                        }
                    } else {
                        if (g1order < g2order) {
                            return -1;
                        } else {
                            return 1;
                        }
                    }
                }
            };

            itemsInChest.sort(sorter);


            chest.getInventory().clear();

            for (ItemStack is : itemsInChest) {
                chest.getInventory().addItem(is);
            }

        } else {
            player.sendMessage("You are not looking at a chest.");
        }
        return true;
    }
}
