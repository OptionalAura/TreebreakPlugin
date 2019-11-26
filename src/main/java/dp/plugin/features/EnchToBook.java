package main.java.dp.plugin.features;

import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import main.java.dp.plugin.Main;
import main.java.dp.plugin.data.DataSerializable.*;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.inventory.PrepareAnvilEvent;
import org.bukkit.inventory.AnvilInventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.EnchantmentStorageMeta;

/*
    @author Daniel Allen
    13-Aug-2019
 */
public class EnchToBook implements Listener {

    @EventHandler
    public void onPrepareAnvil(PrepareAnvilEvent e) {
        if (e.getView().getType() == InventoryType.ANVIL) {
            if (e.getInventory().getItem(0) != null) {
                if (e.getInventory().getItem(0).getEnchantments().size() > 0) {
                    if (e.getInventory().getItem(1) != null) {
                        if (e.getInventory().getItem(1).getType() == Material.BOOK) {
                            Map<Enchantment, Integer> enchs = e.getInventory().getItem(0).getEnchantments();
                            ItemStack outputItem = new ItemStack(Material.ENCHANTED_BOOK);
                            EnchantmentStorageMeta meta = (EnchantmentStorageMeta) outputItem.getItemMeta();
                            Iterator it = enchs.entrySet().iterator();
                            while (it.hasNext()) {
                                Entry<Enchantment, Integer> pair = (Entry<Enchantment, Integer>) it.next();
                                meta.addStoredEnchant(pair.getKey(), pair.getValue(), true);
                            }
                            e.getInventory().setRepairCost(Math.min(5 * enchs.size(), 30));
                            outputItem.setItemMeta(meta);
                            e.setResult(outputItem);
                            ((Player) e.getView().getPlayer()).updateInventory();
                        }
                    }
                }
            }
        }
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent e) {
        ItemStack item1 = e.getInventory().getItem(0);
        ItemStack item2 = e.getInventory().getItem(1);
        if (item1 == null || item2 == null) {
            return;
        }
        if (e.getInventory().getType() == InventoryType.ANVIL) {
            AnvilInventory ai = (AnvilInventory) e.getInventory();
            if (item2.getType() == Material.BOOK) {
                if (item1.getItemMeta().getEnchants().size() < 1) {
                    return;
                }
                Map<Enchantment, Integer> enchs = item1.getEnchantments();
                ItemStack outputItem = new ItemStack(Material.ENCHANTED_BOOK);
                EnchantmentStorageMeta meta = (EnchantmentStorageMeta) outputItem.getItemMeta();
                Iterator it = enchs.entrySet().iterator();
                while (it.hasNext()) {
                    Entry<Enchantment, Integer> pair = (Entry<Enchantment, Integer>) it.next();
                    meta.addStoredEnchant(pair.getKey(), pair.getValue(), true);
                }
                outputItem.setItemMeta(meta);
                ai.setItem(2, outputItem);
                Player player = (Player) e.getWhoClicked();
                Integer playerLevel = player.getLevel();
                Integer reqLevel = Math.min((int)0x00000010 << (enchs.size()-1),45);
                if (playerLevel >= reqLevel) {
                    if (e.getClick() == ClickType.LEFT) {
                        e.getWhoClicked().setItemOnCursor(outputItem);
                        player.setLevel(playerLevel -= reqLevel);
                        e.getInventory().setItem(0, new ItemStack(e.getInventory().getItem(0).getType(), e.getInventory().getItem(0).getAmount() - 1));
                        e.getInventory().setItem(1, new ItemStack(e.getInventory().getItem(1).getType(), e.getInventory().getItem(1).getAmount() - 1));
                        e.getWhoClicked().getWorld().spawnParticle(Particle.FLAME, e.getInventory().getLocation(), 20, 0.3, 0.2, 0.2, 0.3);
                        e.getWhoClicked().getWorld().playSound(e.getInventory().getLocation().add(0.5, 0, 0.5), Sound.BLOCK_ANVIL_USE, 1, 1);
                    } else if (e.getClick() == ClickType.SHIFT_LEFT) {
                        if (e.getWhoClicked().getInventory().firstEmpty() != -1) {
                            player.setLevel(playerLevel -= reqLevel);
                            e.getInventory().setItem(0, new ItemStack(e.getInventory().getItem(0).getType(), e.getInventory().getItem(0).getAmount() - 1));
                            e.getInventory().setItem(1, new ItemStack(e.getInventory().getItem(1).getType(), e.getInventory().getItem(1).getAmount() - 1));
                            e.getWhoClicked().getInventory().addItem(outputItem);
                            e.getWhoClicked().setItemOnCursor(new ItemStack(Material.AIR, 0));
                            e.getWhoClicked().getWorld().spawnParticle(Particle.FIREWORKS_SPARK, e.getInventory().getLocation(), 20, 0.3, 0.2, 0.2, 0.3);

                            e.getWhoClicked().getWorld().playSound(e.getInventory().getLocation().add(0.5, 0, 0.5), Sound.BLOCK_ANVIL_USE, 1, 1);

                            e.setResult(Event.Result.DENY);
                            e.setCancelled(true);
                        } else {
                            e.setResult(Event.Result.DENY);
                            e.setCancelled(true);
                        }

                    }
                    /*else if(playerLevel < reqLevel){
                player.sendMessage("You need to be level "+reqLevel+" to do that...");
                }*/
                }
            }
        }
    }
}
