package main.java.treebreaker.plugin.features;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import static main.java.treebreaker.plugin.Main.thisPlugin;
import static main.java.treebreaker.plugin.utils.Utils.getProperty;
import org.apache.commons.lang.WordUtils;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.block.CreatureSpawner;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

/*
    @author Daniel Allen
    14-Dec-2019
 */
public class SilkSpawners implements Listener {

    public static final String SILK_SPAWNERS_ENABLED_TAG = "drop_spawners",
            DROP_SPAWNERS_IN_CREATIVE_TAG = "drop_spawners_in_creative";

    private NamespacedKey spawnTypeKey = new NamespacedKey(thisPlugin, "mob_spawner_type");
    private NamespacedKey spawnCountKey = new NamespacedKey(thisPlugin, "mob_spawner_count");
    private NamespacedKey spawnRangeKey = new NamespacedKey(thisPlugin, "mob_spawner_range");

    @EventHandler(priority = EventPriority.HIGH)
    public void blockBreak(org.bukkit.event.block.BlockBreakEvent event) {
        if (!event.isCancelled()) {
            if (getProperty(SILK_SPAWNERS_ENABLED_TAG, true)) {
                if (event.getPlayer().getGameMode().equals(GameMode.CREATIVE) || event.getPlayer().getInventory().getItemInMainHand().containsEnchantment(Enchantment.SILK_TOUCH)) {
                    Block broken = event.getBlock();
                    if (broken.getType().equals(Material.SPAWNER)) {
                        ItemStack spawnerItem = new ItemStack(Material.SPAWNER, 1);
                        ItemMeta meta = spawnerItem.getItemMeta();
                        CreatureSpawner spawner = (CreatureSpawner) broken.getState();
                        meta.getPersistentDataContainer().set(spawnTypeKey, PersistentDataType.STRING, spawner.getSpawnedType().toString());
                        meta.getPersistentDataContainer().set(spawnCountKey, PersistentDataType.INTEGER, spawner.getSpawnCount());
                        meta.getPersistentDataContainer().set(spawnRangeKey, PersistentDataType.INTEGER, spawner.getSpawnRange());

                        List<String> lore = new ArrayList<>();
                        lore.add(ChatColor.RESET + "" + ChatColor.GRAY + "Spawn Type: " + WordUtils.capitalizeFully(spawner.getSpawnedType().getKey().getKey(), new char[]{' ', '_'}).replaceAll("_", " ") + ChatColor.RESET);
                        lore.add(ChatColor.RESET + "" + ChatColor.GRAY + "Spawn Count: " + spawner.getSpawnCount() + ChatColor.RESET);
                        lore.add(ChatColor.RESET + "" + ChatColor.GRAY + "Spawn Range: " + spawner.getSpawnRange() + ChatColor.RESET);
                        meta.setLore(lore);
                        meta.setDisplayName(ChatColor.RESET + WordUtils.capitalizeFully(spawner.getSpawnedType().getKey().getKey(), new char[]{' ', '_'}).replaceAll("_", " ") + " Spawner");
                        spawnerItem.setItemMeta(meta);

                        if (event.getPlayer().getGameMode().equals(GameMode.CREATIVE)) {
                            if (getProperty(DROP_SPAWNERS_IN_CREATIVE_TAG, true)) {
                                HashMap<Integer, ItemStack> addItem = event.getPlayer().getInventory().addItem(spawnerItem);
                                for (Iterator<Map.Entry<Integer, ItemStack>> it = addItem.entrySet().iterator(); it.hasNext();) {
                                    event.getBlock().getLocation().getWorld().dropItemNaturally(event.getBlock().getLocation(), it.next().getValue());
                                }
                            }
                        } else {
                            event.getBlock().getLocation().getWorld().dropItemNaturally(event.getBlock().getLocation(), spawnerItem);
                        }
                    }
                }
            }
        }
    }

    @EventHandler
    public void blockPlace(org.bukkit.event.block.BlockPlaceEvent event) {
        if (!event.isCancelled()) {
            if (event.getBlock().getType().equals(Material.SPAWNER)) {
                if (getProperty(SILK_SPAWNERS_ENABLED_TAG, true)) {
                    ItemStack item = event.getItemInHand();
                    ItemMeta meta = item.getItemMeta();
                    PersistentDataContainer pdc = meta.getPersistentDataContainer();
                    if (pdc.has(spawnTypeKey, PersistentDataType.STRING) && pdc.has(spawnRangeKey, PersistentDataType.INTEGER) && pdc.has(spawnCountKey, PersistentDataType.INTEGER)) {
                        String storedTypeValue = pdc.get(spawnTypeKey, PersistentDataType.STRING);
                        int storedRangeValue = pdc.get(spawnRangeKey, PersistentDataType.INTEGER);
                        int storedCountValue = pdc.get(spawnCountKey, PersistentDataType.INTEGER);
                        EntityType type = EntityType.valueOf(storedTypeValue);
                        if (type != null) {
                            BlockState blockState = event.getBlock().getState();
                            CreatureSpawner spawner = ((CreatureSpawner) blockState);
                            spawner.setSpawnedType(type);
                            spawner.setSpawnRange(storedRangeValue);
                            spawner.setSpawnCount(storedCountValue);
                            blockState.update();
                        } else {
                            event.getPlayer().sendMessage(ChatColor.RED + "An error has occurred.");
                            event.setCancelled(true);
                        }
                    }
                }
            }
        }
    }
}
