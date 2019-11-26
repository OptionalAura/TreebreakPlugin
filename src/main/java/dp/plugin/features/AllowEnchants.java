package main.java.dp.plugin.features;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;
import static main.java.dp.plugin.maps.EnchantmentData.*;
import org.apache.commons.lang.WordUtils;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.inventory.PrepareAnvilEvent;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.EnchantmentStorageMeta;
import org.bukkit.inventory.meta.ItemMeta;

/*
    @author Daniel Allen
    13-Aug-2019
 */
public class AllowEnchants implements Listener {

    @EventHandler
    public void onPrepareAnvil(PrepareAnvilEvent e) {
        List<String> enchants = new ArrayList<>();
        ItemStack item1 = e.getInventory().getItem(0);
        ItemStack item2 = e.getInventory().getItem(1);
        //if either of the slots have no item, do nothing.
        if (item1 == null || item2 == null) {
            return;
        }

        if (e.getInventory().getType() == InventoryType.ANVIL) {
            e.getInventory().setMaximumRepairCost(999);
            if (item2.getType() == Material.ENCHANTED_BOOK) {
                ItemStack outputItem = new ItemStack(item1.getType(), 1);
                Map<Enchantment, Integer> enchsA;
                if (item1.getType() == Material.ENCHANTED_BOOK) {
                    EnchantmentStorageMeta meta1 = (EnchantmentStorageMeta) item1.getItemMeta();
                    enchsA = meta1.getStoredEnchants();
                } else {
                    enchsA = item1.getEnchantments();
                }
                EnchantmentStorageMeta meta2 = (EnchantmentStorageMeta) item2.getItemMeta();
                Map<Enchantment, Integer> enchsB = meta2.getStoredEnchants();
                Map<Enchantment, Integer> enchs = new HashMap<>();

                //iterate through the enchantments of the first item
                Iterator enitA = enchsA.entrySet().iterator();
                while (enitA.hasNext()) {
                    Entry<Enchantment, Integer> pair = (Entry<Enchantment, Integer>) enitA.next();
                    enchs.put(pair.getKey(), pair.getValue());
                }

                //iterate through the enchantments of the second item
                Iterator enitB = enchsB.entrySet().iterator();
                while (enitB.hasNext()) {
                    Entry<Enchantment, Integer> pair = (Entry<Enchantment, Integer>) enitB.next();
                    if (enchs.containsKey(pair.getKey())) {
                        int newNum = 0;
                        if (enchs.get(pair.getKey()).equals(pair.getValue())) {
                            if (restrictedEnchantments.contains(pair.getKey())) {
                                newNum = 1;
                            } else if (pair.getValue() < 127) {
                                if (pair.getKey().equals(Enchantment.LOOT_BONUS_MOBS)) {
                                    newNum = Math.min(25, pair.getValue() + 1);
                                } else if (pair.getKey().equals(Enchantment.LOOT_BONUS_BLOCKS)) {
                                    newNum = Math.min(50, pair.getValue() + 1);
                                } else {
                                    newNum = pair.getValue() + 1;
                                }
                            }
                        } else {
                            newNum = Math.max(enchs.get(pair.getKey()), pair.getValue());
                        }
                        enchs.replace(pair.getKey(), newNum);
                    } else {
                        enchs.put(pair.getKey(), pair.getValue());
                    }

                }
                ArrayList<Entry<Enchantment, Integer>> enchsSorted = new ArrayList<>(enchs.entrySet());
                Collections.sort(enchsSorted, new Comparator<Entry<Enchantment, Integer>>() {
                    @Override
                    public int compare(Entry<Enchantment, Integer> o1, Entry<Enchantment, Integer> o2) {
                        int lvl = Integer.compare(o2.getValue(), o1.getValue());
                        return (lvl == 0 ? o1.getKey().getKey().getKey().compareToIgnoreCase(o2.getKey().getKey().getKey()) : lvl);
                    }
                });
                //add enchantments to the output item
                Iterator it = enchsSorted.iterator();

                if (outputItem.getType() == Material.ENCHANTED_BOOK) {
                    EnchantmentStorageMeta meta = (EnchantmentStorageMeta) outputItem.getItemMeta();
                    while (it.hasNext()) {
                        Entry<Enchantment, Integer> pair = (Entry<Enchantment, Integer>) it.next();
                        meta.addStoredEnchant(pair.getKey(), pair.getValue(), true);
                        it.remove();
                    }
                    outputItem.setItemMeta(meta);
                } else {
                    ItemMeta meta = outputItem.getItemMeta();
                    while (it.hasNext()) {
                        Entry<Enchantment, Integer> pair = (Entry<Enchantment, Integer>) it.next();
                        if (pair.getKey().canEnchantItem(outputItem)) {
                            meta.addEnchant(pair.getKey(), pair.getValue(), true);
                        }
                        String keyKeyKey = pair.getKey().getKey().getKey();
                        enchants.add(enchantmentNamePrefix + WordUtils.capitalizeFully(keyKeyKey, new char[]{' ', '_'}).replaceAll("_", " ") + enchantmentLevelPrefix + intToRomanNumeral(pair.getValue()));
                        it.remove();
                    }
                    if (item1.hasItemMeta()) {
                        if (item1.getDurability() != item1.getType().getMaxDurability()) {
                            outputItem.setDurability(item1.getDurability());
                        }
                    }
                    /*enchants.sort(new Comparator<String>(){
                    @Override
                    public int compare(String o1, String o2) {
                    String[] o1parts = o1.split(" ");
                    String o1numeral = o1parts[o1parts.length-1];
                    int o1val = romanNumeralToInt(o1numeral);
                    String[] o2parts = o2.split(" ");
                    String o2numeral = o2parts[o2parts.length-1];
                    int o2val = romanNumeralToInt(o2numeral);
                    return Integer.compare(o1val, o2val);
                    }
                    });*/
                    meta.setLore(enchants);
                    meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
                    outputItem.setItemMeta(meta);
                }
                e.setResult(outputItem);
            } else {
                ItemStack outputItem = new ItemStack(item1.getType(), 1);
                Map<Enchantment, Integer> enchsA;
                if (item1.getType() == Material.ENCHANTED_BOOK) {
                    return;
                } else {
                    enchsA = item1.getItemMeta().getEnchants();
                }
                ItemMeta meta2 = item2.getItemMeta();
                Map<Enchantment, Integer> enchsB = meta2.getEnchants();
                Map<Enchantment, Integer> enchs = new HashMap<>();

                //iterate through the enchantments of the first item
                Iterator enitA = enchsA.entrySet().iterator();
                while (enitA.hasNext()) {
                    Entry<Enchantment, Integer> pair = (Entry<Enchantment, Integer>) enitA.next();
                    enchs.put(pair.getKey(), pair.getValue());

                }

                //iterate through the enchantments of the second item
                Iterator enitB = enchsB.entrySet().iterator();
                while (enitB.hasNext()) {
                    Entry<Enchantment, Integer> pair = (Entry<Enchantment, Integer>) enitB.next();
                    if (enchs.containsKey(pair.getKey())) {
                        int newNum = 0;
                        if (enchs.get(pair.getKey()).equals(pair.getValue())) {
                            if (restrictedEnchantments.contains(pair.getKey())) {
                                newNum = 1;
                            } else if (pair.getValue() < 127) {
                                if (pair.getKey().equals(Enchantment.LOOT_BONUS_MOBS)) {
                                    newNum = Math.min(25, pair.getValue() + 1);
                                } else if (pair.getKey().equals(Enchantment.LOOT_BONUS_BLOCKS)) {
                                    newNum = Math.min(50, pair.getValue() + 1);
                                } else {
                                    newNum = pair.getValue() + 1;
                                }
                            }
                        } else {
                            newNum = Math.max(enchs.get(pair.getKey()), pair.getValue());
                        }
                        enchs.replace(pair.getKey(), newNum);
                    } else {
                        enchs.put(pair.getKey(), pair.getValue());
                    }

                }
                ArrayList<Entry<Enchantment, Integer>> enchsSorted = new ArrayList<>(enchs.entrySet());
                Collections.sort(enchsSorted, new Comparator<Entry<Enchantment, Integer>>() {
                    @Override
                    public int compare(Entry<Enchantment, Integer> o1, Entry<Enchantment, Integer> o2) {
                        int lvl = Integer.compare(o2.getValue(), o1.getValue());
                        return (lvl == 0 ? o1.getKey().getKey().getKey().compareToIgnoreCase(o2.getKey().getKey().getKey()) : lvl);
                    }
                });
                //add enchantments to the output item
                Iterator it = enchsSorted.iterator();
                if (outputItem.getType() == Material.ENCHANTED_BOOK) {
                    EnchantmentStorageMeta meta = (EnchantmentStorageMeta) outputItem.getItemMeta();
                    while (it.hasNext()) {
                        Entry<Enchantment, Integer> pair = (Entry<Enchantment, Integer>) it.next();
                        meta.addStoredEnchant(pair.getKey(), pair.getValue(), true);
                        it.remove();
                    }
                    outputItem.setItemMeta(meta);
                } else {
                    ItemMeta meta = outputItem.getItemMeta();
                    while (it.hasNext()) {
                        Entry<Enchantment, Integer> pair = (Entry<Enchantment, Integer>) it.next();
                        if (pair.getKey().canEnchantItem(outputItem)) {
                            meta.addEnchant(pair.getKey(), pair.getValue(), true);
                            String keyKeyKey = pair.getKey().getKey().getKey();
                            enchants.add(enchantmentNamePrefix + WordUtils.capitalizeFully(keyKeyKey, new char[]{' ', '_'}).replaceAll("_", " ") + enchantmentLevelPrefix + intToRomanNumeral(pair.getValue()));
                        }
                        it.remove();
                    }
                    meta.setLore(enchants);
                    meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
                    String coloredName = e.getInventory().getItem(0).getItemMeta().getDisplayName();
                    meta.setDisplayName(coloredName);
                    outputItem.setItemMeta(meta);
                }
                e.setResult(outputItem);
           }
        }
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onInventoryClick(InventoryClickEvent e) {

        List<String> enchants = new ArrayList<>();

        if (e.getSlot() != 2) {
            return;
        }
        ItemStack item1 = e.getInventory().getItem(0);
        ItemStack item2 = e.getInventory().getItem(1);
        if (item1 == null || item2 == null) {
            return;
        }
        if (e.getInventory().getType() == InventoryType.ANVIL) {
            if (item2.getType() == Material.ENCHANTED_BOOK) {
                ItemStack outputItem = new ItemStack(item1.getType(), 1);
                Map<Enchantment, Integer> enchsA;
                if (item1.getType() == Material.ENCHANTED_BOOK) {
                    EnchantmentStorageMeta meta1 = (EnchantmentStorageMeta) item1.getItemMeta();
                    enchsA = meta1.getStoredEnchants();
                } else {
                    enchsA = item1.getItemMeta().getEnchants();
                }
                EnchantmentStorageMeta meta2 = (EnchantmentStorageMeta) item2.getItemMeta();
                Map<Enchantment, Integer> enchsB = meta2.getStoredEnchants();
                Map<Enchantment, Integer> enchs = new HashMap<>();

                //iterate through the enchantments of the first item
                Iterator enitA = enchsA.entrySet().iterator();
                while (enitA.hasNext()) {
                    Entry<Enchantment, Integer> pair = (Entry<Enchantment, Integer>) enitA.next();
                    enchs.put(pair.getKey(), pair.getValue());

                }

                //iterate through the enchantments of the second item
                Iterator enitB = enchsB.entrySet().iterator();
                while (enitB.hasNext()) {
                    Entry<Enchantment, Integer> pair = (Entry<Enchantment, Integer>) enitB.next();
                    if (enchs.containsKey(pair.getKey())) {
                        int newNum = 0;
                        if (enchs.get(pair.getKey()).equals(pair.getValue())) {
                            if (restrictedEnchantments.contains(pair.getKey())) {
                                newNum = 1;
                            } else if (pair.getValue() < 127) {
                                if (pair.getKey().equals(Enchantment.LOOT_BONUS_MOBS)) {
                                    newNum = Math.min(25, pair.getValue() + 1);
                                } else if (pair.getKey().equals(Enchantment.LOOT_BONUS_BLOCKS)) {
                                    newNum = Math.min(50, pair.getValue() + 1);
                                } else {
                                    newNum = pair.getValue() + 1;
                                }
                            }
                        } else {
                            newNum = Math.max(enchs.get(pair.getKey()), pair.getValue());
                        }
                        enchs.replace(pair.getKey(), newNum);
                    } else {
                        enchs.put(pair.getKey(), pair.getValue());
                    }

                }
                ArrayList<Entry<Enchantment, Integer>> enchsSorted = new ArrayList<>(enchs.entrySet());
                Collections.sort(enchsSorted, new Comparator<Entry<Enchantment, Integer>>() {
                    @Override
                    public int compare(Entry<Enchantment, Integer> o1, Entry<Enchantment, Integer> o2) {
                        int lvl = Integer.compare(o2.getValue(), o1.getValue());
                        return (lvl == 0 ? o1.getKey().getKey().getKey().compareToIgnoreCase(o2.getKey().getKey().getKey()) : lvl);
                    }
                });
                //add enchantments to the output item
                Iterator it = enchsSorted.iterator();
                if (outputItem.getType() == Material.ENCHANTED_BOOK) {
                    EnchantmentStorageMeta meta = (EnchantmentStorageMeta) outputItem.getItemMeta();
                    while (it.hasNext()) {
                        Entry<Enchantment, Integer> pair = (Entry<Enchantment, Integer>) it.next();
                        meta.addStoredEnchant(pair.getKey(), pair.getValue(), true);
                        it.remove();
                    }
                    outputItem.setItemMeta(meta);
                } else {
                    ItemMeta meta = outputItem.getItemMeta();
                    while (it.hasNext()) {
                        Entry<Enchantment, Integer> pair = (Entry<Enchantment, Integer>) it.next();
                        if (pair.getKey().canEnchantItem(outputItem)) {
                            meta.addEnchant(pair.getKey(), pair.getValue(), true);
                            String keyKeyKey = pair.getKey().getKey().getKey();
                            enchants.add(enchantmentNamePrefix + WordUtils.capitalizeFully(keyKeyKey, new char[]{' ', '_'}).replaceAll("_", " ") + enchantmentLevelPrefix + intToRomanNumeral(pair.getValue()));
                        }
                        it.remove();
                    }
                    meta.setLore(enchants);
                    meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
                    String coloredName = e.getInventory().getItem(0).getItemMeta().getDisplayName();
                    meta.setDisplayName(coloredName);
                    outputItem.setItemMeta(meta);
                }
                if (e.getClick() == ClickType.LEFT) {
                    e.getWhoClicked().setItemOnCursor(outputItem);
                    e.getInventory().setItem(0, new ItemStack(e.getInventory().getItem(0).getType(), e.getInventory().getItem(0).getAmount() - 1));
                    e.getInventory().setItem(1, new ItemStack(e.getInventory().getItem(1).getType(), e.getInventory().getItem(1).getAmount() - 1));
                    e.getWhoClicked().getWorld().spawnParticle(Particle.FIREWORKS_SPARK, e.getInventory().getLocation(), 20, 0.3, 0.2, 0.2, 0.3);
                    e.getWhoClicked().getWorld().playSound(e.getInventory().getLocation().add(0.5, 0, 0.5), Sound.BLOCK_ANVIL_USE, 1, 1);
                } else if (e.getClick() == ClickType.SHIFT_LEFT) {
                    if (e.getWhoClicked().getInventory().firstEmpty() != -1) {
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
            } else {
                ItemStack outputItem = new ItemStack(item1.getType(), 1);
                Map<Enchantment, Integer> enchsA;
                if (item1.getType() == Material.ENCHANTED_BOOK) {
                    return;
                } else {
                    enchsA = item1.getItemMeta().getEnchants();
                }
                ItemMeta meta2 = item2.getItemMeta();
                Map<Enchantment, Integer> enchsB = meta2.getEnchants();
                Map<Enchantment, Integer> enchs = new HashMap<>();

                //iterate through the enchantments of the first item
                Iterator enitA = enchsA.entrySet().iterator();
                while (enitA.hasNext()) {
                    Entry<Enchantment, Integer> pair = (Entry<Enchantment, Integer>) enitA.next();
                    enchs.put(pair.getKey(), pair.getValue());

                }

                //iterate through the enchantments of the second item
                Iterator enitB = enchsB.entrySet().iterator();
                while (enitB.hasNext()) {
                    Entry<Enchantment, Integer> pair = (Entry<Enchantment, Integer>) enitB.next();
                    if (enchs.containsKey(pair.getKey())) {
                        int newNum = 0;
                        if (enchs.get(pair.getKey()).equals(pair.getValue())) {
                            if (restrictedEnchantments.contains(pair.getKey())) {
                                newNum = 1;
                            } else if (pair.getValue() < 127) {
                                if (pair.getKey().equals(Enchantment.LOOT_BONUS_MOBS)) {
                                    newNum = Math.min(25, pair.getValue() + 1);
                                } else if (pair.getKey().equals(Enchantment.LOOT_BONUS_BLOCKS)) {
                                    newNum = Math.min(50, pair.getValue() + 1);
                                } else {
                                    newNum = pair.getValue() + 1;
                                }
                            }
                        } else {
                            newNum = Math.max(enchs.get(pair.getKey()), pair.getValue());
                        }
                        enchs.replace(pair.getKey(), newNum);
                    } else {
                        enchs.put(pair.getKey(), pair.getValue());
                    }

                }
                ArrayList<Entry<Enchantment, Integer>> enchsSorted = new ArrayList<>(enchs.entrySet());
                Collections.sort(enchsSorted, new Comparator<Entry<Enchantment, Integer>>() {
                    @Override
                    public int compare(Entry<Enchantment, Integer> o1, Entry<Enchantment, Integer> o2) {
                        int lvl = Integer.compare(o2.getValue(), o1.getValue());
                        return (lvl == 0 ? o1.getKey().getKey().getKey().compareToIgnoreCase(o2.getKey().getKey().getKey()) : lvl);
                    }
                });
                //add enchantments to the output item
                Iterator it = enchsSorted.iterator();
                if (outputItem.getType() == Material.ENCHANTED_BOOK) {
                    EnchantmentStorageMeta meta = (EnchantmentStorageMeta) outputItem.getItemMeta();
                    while (it.hasNext()) {
                        Entry<Enchantment, Integer> pair = (Entry<Enchantment, Integer>) it.next();
                        meta.addStoredEnchant(pair.getKey(), pair.getValue(), true);
                        it.remove();
                    }
                    outputItem.setItemMeta(meta);
                } else {
                    ItemMeta meta = outputItem.getItemMeta();
                    while (it.hasNext()) {
                        Entry<Enchantment, Integer> pair = (Entry<Enchantment, Integer>) it.next();
                        if (pair.getKey().canEnchantItem(outputItem)) {
                            meta.addEnchant(pair.getKey(), pair.getValue(), true);
                            String keyKeyKey = pair.getKey().getKey().getKey();
                            enchants.add(enchantmentNamePrefix + WordUtils.capitalizeFully(keyKeyKey, new char[]{' ', '_'}).replaceAll("_", " ") + enchantmentLevelPrefix + intToRomanNumeral(pair.getValue()));
                        }
                        it.remove();
                    }
                    meta.setLore(enchants);
                    meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
                    String coloredName = e.getInventory().getItem(0).getItemMeta().getDisplayName();
                    meta.setDisplayName(coloredName);
                    outputItem.setItemMeta(meta);
                }
                if (e.getClick() == ClickType.LEFT) {
                    e.getWhoClicked().setItemOnCursor(outputItem);
                    e.getInventory().setItem(0, new ItemStack(e.getInventory().getItem(0).getType(), e.getInventory().getItem(0).getAmount() - 1));
                    e.getInventory().setItem(1, new ItemStack(e.getInventory().getItem(1).getType(), e.getInventory().getItem(1).getAmount() - 1));
                    e.getWhoClicked().getWorld().spawnParticle(Particle.FLAME, e.getInventory().getLocation(), 20, 0.3, 0.2, 0.2, 0.3);
                    e.getWhoClicked().getWorld().playSound(e.getInventory().getLocation().add(0.5, 0, 0.5), Sound.BLOCK_ANVIL_USE, 1, 1);
                } else if (e.getClick() == ClickType.SHIFT_LEFT) {
                    if (e.getWhoClicked().getInventory().firstEmpty() != -1) {
                        e.getInventory().setItem(0, new ItemStack(e.getInventory().getItem(0).getType(), e.getInventory().getItem(0).getAmount() - 1));
                        e.getInventory().setItem(1, new ItemStack(e.getInventory().getItem(1).getType(), e.getInventory().getItem(1).getAmount() - 1));
                        e.getWhoClicked().getInventory().addItem(outputItem);
                        e.getWhoClicked().setItemOnCursor(new ItemStack(Material.AIR, 0));
                        e.getWhoClicked().getWorld().spawnParticle(Particle.FLAME, e.getInventory().getLocation(), 20, 0.3, 0.2, 0.2, 0.3);
                        e.getWhoClicked().getWorld().playSound(e.getInventory().getLocation().add(0.5, 0, 0.5), Sound.BLOCK_ANVIL_USE, 1, 1);
                        e.setResult(Event.Result.DENY);
                        e.setCancelled(true);
                    } else {
                        e.setResult(Event.Result.DENY);
                        e.setCancelled(true);
                    }
                }
            }
        }
    }
}
