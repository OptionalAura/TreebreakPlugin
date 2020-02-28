/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package main.java.treebreaker.plugin.features;

import java.util.HashMap;
import static main.java.treebreaker.plugin.utils.Utils.getProperty;
import static main.java.treebreaker.plugin.utils.Utils.setProperty;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import main.java.treebreaker.plugin.utils.Criteria;
import main.java.treebreaker.plugin.utils.Utils;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.Damageable;

/**
 *
 * @author Daniel Allen
 */
public class TreeBreaker implements Listener {

    public static final String TREE_BREAK_ENABLED_TAG = "tree_break_enabled",
            MAX_TREES_BROKEN_TAG = "max_tree_break",
            CONNECTS_DIAGONALLY_TAG = "connects_diagonally",
            DROPS_ITEMS_IN_CREATIVE_TAG = "drops_items_in_creative",
            USES_AXE_DURABILITY_TAG = "uses_axe_durability",
            SNEAK_TO_BREAK_NORMALLY_TAG = "sneak_to_break_normally";

    public static long getMaxNum() {
        return getProperty(MAX_TREES_BROKEN_TAG, 2048L);
    }

    public static void setMaxNum(int max) {
        setProperty(MAX_TREES_BROKEN_TAG, max);
    }

    public static void setConnectsDiagonally(boolean b) {
        setProperty(CONNECTS_DIAGONALLY_TAG, b);
    }

    public static boolean connectsDiagonally() {
        return (boolean) getProperty(CONNECTS_DIAGONALLY_TAG, true);
    }

    private static final Material[] AXES = {
        Material.WOODEN_AXE,
        Material.STONE_AXE,
        Material.IRON_AXE,
        Material.GOLDEN_AXE,
        Material.DIAMOND_AXE
    };

    private static final Material[] LOGS = {
        Material.ACACIA_LOG,
        Material.BIRCH_LOG,
        Material.DARK_OAK_LOG,
        Material.JUNGLE_LOG,
        Material.OAK_LOG,
        Material.SPRUCE_LOG
    };

    private static final Material[] ACACIA_TREE_PARTS = {
        Material.ACACIA_LOG,
        Material.ACACIA_LEAVES
    };
    private static final Material[] BIRCH_TREE_PARTS = {
        Material.BIRCH_LOG,
        Material.BIRCH_LEAVES
    };
    private static final Material[] DARK_OAK_TREE_PARTS = {
        Material.DARK_OAK_LOG,
        Material.DARK_OAK_LEAVES
    };
    private static final Material[] JUNGLE_TREE_PARTS = {
        Material.JUNGLE_LOG,
        Material.JUNGLE_LEAVES
    };
    private static final Material[] OAK_TREE_PARTS = {
        Material.OAK_LOG,
        Material.OAK_LEAVES
    };
    private static final Material[] SPRUCE_TREE_PARTS = {
        Material.SPRUCE_LOG,
        Material.SPRUCE_LEAVES
    };

    private static final int NO_IDENTIFIER = -1;
    private static final int ACACIA_IDENTIFIER = 0;
    private static final int BIRCH_IDENTIFIER = 1;
    private static final int DARK_OAK_IDENTIFIER = 2;
    private static final int JUNGLE_IDENTIFIER = 3;
    private static final int OAK_IDENTIFIER = 4;
    private static final int SPRUCE_IDENTIFIER = 5;

    private static final int getIdentifier(Material m) {
        if (Utils.contains(ACACIA_TREE_PARTS, m)) {
            return ACACIA_IDENTIFIER;
        }
        if (Utils.contains(BIRCH_TREE_PARTS, m)) {
            return BIRCH_IDENTIFIER;
        }
        if (Utils.contains(DARK_OAK_TREE_PARTS, m)) {
            return DARK_OAK_IDENTIFIER;
        }
        if (Utils.contains(JUNGLE_TREE_PARTS, m)) {
            return JUNGLE_IDENTIFIER;
        }
        if (Utils.contains(OAK_TREE_PARTS, m)) {
            return OAK_IDENTIFIER;
        }
        if (Utils.contains(SPRUCE_TREE_PARTS, m)) {
            return SPRUCE_IDENTIFIER;
        }
        return NO_IDENTIFIER;
    }

    private static final Criteria<Material> IS_SAME_TREE_TYPE = new Criteria<Material>() {
        @Override
        public boolean check(Material... ts) {
            if (ts.length < 1) {
                return false;
            }
            if (ts[0].equals(Material.AIR)) {
                return false;
            }
            int treeType = getIdentifier(ts[0]);
            for (int i = 0; i < ts.length; i++) {
                if (ts[i].equals(Material.AIR)) {
                    return false;
                }
                if (getIdentifier(ts[i]) != treeType) {
                    return false;
                }
                if (!Utils.contains(ACACIA_TREE_PARTS, ts[i])) {
                    if (!Utils.contains(BIRCH_TREE_PARTS, ts[i])) {
                        if (!Utils.contains(DARK_OAK_TREE_PARTS, ts[i])) {
                            if (!Utils.contains(JUNGLE_TREE_PARTS, ts[i])) {
                                if (!Utils.contains(OAK_TREE_PARTS, ts[i])) {
                                    if (!Utils.contains(SPRUCE_TREE_PARTS, ts[i])) {
                                        return false;
                                    }
                                }
                            }
                        }
                    }
                }
            }
            return true;
        }
    };

    int availableID = 0;

    HashMap<Integer, Long> taskIDs = new HashMap<>();

    private int getAvailableTaskID() {
        int avail = availableID;
        taskIDs.put(avail, 0L);
        availableID++;
        if (availableID <= Integer.MAX_VALUE) {
            availableID = 0;
        }
        return avail;
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onBlockBreak(BlockBreakEvent e) {
        if (getProperty(TREE_BREAK_ENABLED_TAG, true) && getMaxNum() != 0) {
            Block broken = e.getBlock();
            if ((!e.getPlayer().isSneaking() || !getProperty(SNEAK_TO_BREAK_NORMALLY_TAG, true)) && Utils.contains(AXES, e.getPlayer().getInventory().getItemInMainHand().getType()) && Utils.contains(LOGS, broken.getType())) {
                e.setCancelled(true);
                int taskID = getAvailableTaskID();
                breakBlockRecursively(broken, e.getPlayer(), taskID);
                taskIDs.remove(taskID);
            }
        }
    }

    public void breakBlockRecursively(Block b, Player p, int taskID) {
        long maxNum = getMaxNum();
        Material m = b.getType();
        if (taskIDs.containsKey(taskID)) {
            long curNum = taskIDs.get(taskID);
            if (curNum <= maxNum || maxNum == -1) {
                if (getIdentifier(m) != NO_IDENTIFIER) {
                    if (p.getGameMode().equals(GameMode.CREATIVE)) {
                        if (getProperty(DROPS_ITEMS_IN_CREATIVE_TAG, false)) {
                            b.breakNaturally();
                        } else {
                            b.getDrops();
                            b.setType(Material.AIR);
                        }
                    } else {
                        if (getProperty(USES_AXE_DURABILITY_TAG, false)) {
                            ItemStack itemInHand = p.getInventory().getItemInMainHand();
                            if (Utils.contains(AXES, itemInHand.getType())) {
                                b.breakNaturally();
                            }
                        } else {
                            b.breakNaturally();
                        }
                        if (getProperty(USES_AXE_DURABILITY_TAG, false)) {
                            ItemStack itemInHand = p.getInventory().getItemInMainHand();
                            if (Utils.contains(AXES, itemInHand.getType()) && itemInHand.hasItemMeta() && !itemInHand.getItemMeta().isUnbreakable()) {
                                ((Damageable) itemInHand).setDamage(((Damageable) itemInHand).getDamage() + 1);
                            }
                        }
                    }

                    taskIDs.put(taskID, curNum + 1);

                    if (connectsDiagonally()) {
                        for (int z = -1; z <= 1; z++) {
                            for (int y = -1; y <= 1; y++) {
                                for (int x = -1; x <= 1; x++) {
                                    if (x != 0 || y != 0 || z != 0) {
                                        Location cur = b.getLocation().clone().add(x, y, z);
                                        if (IS_SAME_TREE_TYPE.check(m, cur.getBlock().getType())) {
                                            breakBlockRecursively(cur.getBlock(), p, taskID);
                                        }
                                    }
                                }
                            }
                        }
                    } else {
                        Location up = b.getLocation().clone().add(0, 1, 0);
                        Location down = b.getLocation().clone().add(0, -1, 0);

                        Location north = b.getLocation().clone().add(0, 0, 1);
                        Location south = b.getLocation().clone().add(0, 0, -1);

                        Location east = b.getLocation().clone().add(1, 0, 0);
                        Location west = b.getLocation().clone().add(-1, 0, 0);

                        if (IS_SAME_TREE_TYPE.check(b.getType(), up.getBlock().getType())) {
                            breakBlockRecursively(up.getBlock(), p, taskID);
                        }
                        if (IS_SAME_TREE_TYPE.check(b.getType(), down.getBlock().getType())) {
                            breakBlockRecursively(down.getBlock(), p, taskID);
                        }
                        if (IS_SAME_TREE_TYPE.check(b.getType(), north.getBlock().getType())) {
                            breakBlockRecursively(north.getBlock(), p, taskID);
                        }
                        if (IS_SAME_TREE_TYPE.check(b.getType(), south.getBlock().getType())) {
                            breakBlockRecursively(south.getBlock(), p, taskID);
                        }
                        if (IS_SAME_TREE_TYPE.check(b.getType(), east.getBlock().getType())) {
                            breakBlockRecursively(east.getBlock(), p, taskID);
                        }
                        if (IS_SAME_TREE_TYPE.check(b.getType(), west.getBlock().getType())) {
                            breakBlockRecursively(west.getBlock(), p, taskID);
                        }
                    }
                }
            }
        }
    }
}
