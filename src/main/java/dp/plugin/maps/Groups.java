package main.java.dp.plugin.maps;

import java.util.ArrayList;
import java.util.List;
import org.bukkit.Material;

/*
    @author Daniel Allen
    21-Aug-2019
 */
public class Groups {

    public static Group GROUP_WEAPONS = new Group("Weapons", 0);
    public static Group GROUP_ARMOR = new Group("Armor", 1);
    public static Group GROUP_TOOLS = new Group("Tools", 2);
    public static Group GROUP_UTILS = new Group("Utils", 3);
    public static Group GROUP_DYES = new Group("Dyes", 4);
    public static Group GROUP_CROPS = new Group("Crops", 5);
    public static Group GROUP_ORES = new Group("Ores", 6);
    public static Group GROUP_FOOD = new Group("Food", 7);
    public static Group GROUP_BREWING = new Group("Brewing", 8);
    public static Group GROUP_POTIONS = new Group("Potions", 9);
    public static Group GROUP_WOOD = new Group("Wood", 10);
    public static Group GROUP_REDSTONE = new Group("Redstone", 11);
    public static Group GROUP_OTHER = new Group("Other", 12);

    public static List<Group> groups = new ArrayList<>();

    public static Group getGroup(Material m) {
        for (Group g : groups) {
            if (g == Groups.GROUP_OTHER) {
                continue;
            }
            if (g.has(m)) {
                return g;
            }
        }
        return GROUP_OTHER;
    }

    static {
        GROUP_WEAPONS.addAll(
                Material.DIAMOND_SWORD,
                Material.WOODEN_SWORD,
                Material.STONE_SWORD,
                Material.IRON_SWORD,
                Material.GOLDEN_SWORD,
                Material.BOW,
                Material.CROSSBOW,
                Material.ARROW,
                Material.TIPPED_ARROW,
                Material.TRIDENT,
                Material.SHIELD,
                Material.TOTEM_OF_UNDYING);
        GROUP_WEAPONS.setDesc("Swords, Bows, Shields, etc...");

        GROUP_ARMOR.addAll(
                Material.DIAMOND_CHESTPLATE,
                Material.LEATHER_BOOTS,
                Material.LEATHER_LEGGINGS,
                Material.LEATHER_CHESTPLATE,
                Material.LEATHER_HELMET,
                Material.CHAINMAIL_BOOTS,
                Material.CHAINMAIL_LEGGINGS,
                Material.CHAINMAIL_CHESTPLATE,
                Material.CHAINMAIL_HELMET,
                Material.IRON_BOOTS,
                Material.IRON_LEGGINGS,
                Material.IRON_CHESTPLATE,
                Material.IRON_HELMET,
                Material.GOLDEN_BOOTS,
                Material.GOLDEN_LEGGINGS,
                Material.GOLDEN_CHESTPLATE,
                Material.GOLDEN_HELMET,
                Material.DIAMOND_BOOTS,
                Material.DIAMOND_LEGGINGS,
                Material.DIAMOND_HELMET,
                Material.DIAMOND_HORSE_ARMOR,
                Material.GOLDEN_HORSE_ARMOR,
                Material.IRON_HORSE_ARMOR
        );
        GROUP_ARMOR.setDesc("Human/Horse Armor");

        GROUP_TOOLS.addAll(
                Material.DIAMOND_PICKAXE,
                Material.WOODEN_AXE,
                Material.WOODEN_HOE,
                Material.WOODEN_SHOVEL,
                Material.WOODEN_PICKAXE,
                Material.STONE_AXE,
                Material.STONE_HOE,
                Material.STONE_SHOVEL,
                Material.STONE_PICKAXE,
                Material.IRON_AXE,
                Material.IRON_HOE,
                Material.IRON_SHOVEL,
                Material.IRON_PICKAXE,
                Material.GOLDEN_AXE,
                Material.GOLDEN_HOE,
                Material.GOLDEN_SHOVEL,
                Material.GOLDEN_PICKAXE,
                Material.DIAMOND_AXE,
                Material.DIAMOND_HOE,
                Material.DIAMOND_SHOVEL,
                Material.FLINT_AND_STEEL,
                Material.FISHING_ROD,
                Material.SHEARS
        );
        GROUP_TOOLS.setDesc("Pickaxes, Shovels, Hoes, Axes, Shears, etc...");

        GROUP_UTILS.addAll(
                Material.BUCKET,
                Material.COMPASS,
                Material.CLOCK,
                Material.LEAD,
                Material.NAME_TAG,
                Material.LAVA_BUCKET,
                Material.WATER_BUCKET,
                Material.MILK_BUCKET,
                Material.WRITABLE_BOOK,
                Material.WRITTEN_BOOK,
                Material.MAP,
                Material.FILLED_MAP
        );
        GROUP_UTILS.setDesc("Lava Buckets, Water Buckets, Clocks, Nametags, etc...");

        GROUP_DYES.addAll(
                Material.INK_SAC,
                Material.RED_DYE,
                Material.GREEN_DYE,
                Material.PURPLE_DYE,
                Material.CYAN_DYE,
                Material.LIGHT_GRAY_DYE,
                Material.GRAY_DYE,
                Material.PINK_DYE,
                Material.LIME_DYE,
                Material.YELLOW_DYE,
                Material.LIGHT_BLUE_DYE,
                Material.MAGENTA_DYE,
                Material.ORANGE_DYE,
                Material.BONE_MEAL,
                Material.BLACK_DYE,
                Material.BROWN_DYE,
                Material.BLUE_DYE,
                Material.WHITE_DYE
        );
        GROUP_DYES.setDesc("Dyes and Dye Ingredients");

        GROUP_CROPS.addAll(
                Material.WHEAT_SEEDS,
                Material.SUGAR_CANE,
                Material.KELP,
                Material.BAMBOO,
                Material.COCOA_BEANS,
                Material.PUMPKIN_SEEDS,
                Material.MELON_SEEDS,
                Material.NETHER_WART,
                Material.BEETROOT_SEEDS,
                Material.OAK_SAPLING,
                Material.SPRUCE_SAPLING,
                Material.BIRCH_SAPLING,
                Material.JUNGLE_SAPLING,
                Material.ACACIA_SAPLING,
                Material.DARK_OAK_SAPLING
        );
        GROUP_CROPS.setDesc("Seeds, Saplings, etc...");

        GROUP_ORES.addAll(
                Material.DIAMOND_ORE,
                Material.NETHER_QUARTZ_ORE,
                Material.QUARTZ,
                Material.LAPIS_ORE,
                Material.LAPIS_LAZULI,
                Material.LAPIS_BLOCK,
                Material.COAL_ORE,
                Material.COAL,
                Material.COAL_BLOCK,
                Material.IRON_ORE,
                Material.IRON_INGOT,
                Material.IRON_BLOCK,
                Material.IRON_NUGGET,
                Material.GOLD_ORE,
                Material.GOLD_INGOT,
                Material.GOLD_BLOCK,
                Material.GOLD_NUGGET,
                Material.DIAMOND,
                Material.DIAMOND_BLOCK,
                Material.EMERALD_ORE,
                Material.EMERALD,
                Material.EMERALD_BLOCK,
                Material.REDSTONE_ORE
        );
        GROUP_ORES.setDesc("Ores, Ingots, Gems, and Nuggets");
        GROUP_FOOD.addAll(
                Material.COOKED_BEEF,
                Material.APPLE,
                Material.MUSHROOM_STEW,
                Material.BREAD,
                Material.PORKCHOP,
                Material.COOKED_PORKCHOP,
                Material.GOLDEN_APPLE,
                Material.ENCHANTED_GOLDEN_APPLE,
                Material.COD,
                Material.COOKED_COD,
                Material.TROPICAL_FISH,
                Material.SALMON,
                Material.COOKED_SALMON,
                Material.CAKE,
                Material.COOKIE,
                Material.MELON_SLICE,
                Material.DRIED_KELP,
                Material.BEEF,
                Material.CHICKEN,
                Material.COOKED_CHICKEN,
                Material.CARROT,
                Material.POTATO,
                Material.POISONOUS_POTATO,
                Material.PUMPKIN_PIE,
                Material.RABBIT,
                Material.COOKED_RABBIT,
                Material.RABBIT_STEW,
                Material.MUTTON,
                Material.COOKED_MUTTON,
                Material.BEETROOT,
                Material.BEETROOT_SOUP,
                Material.SWEET_BERRIES,
                Material.BAKED_POTATO,
                Material.SUSPICIOUS_STEW
        );
        GROUP_FOOD.setDesc("Raw and Cooked Meat, Bread, Carrots, Potatoes, etc...");

        GROUP_BREWING.addAll(
                Material.BREWING_STAND,
                Material.GHAST_TEAR,
                Material.FERMENTED_SPIDER_EYE,
                Material.BLAZE_POWDER,
                Material.MAGMA_CREAM,
                Material.CAULDRON,
                Material.GLISTERING_MELON_SLICE,
                Material.GOLDEN_CARROT,
                Material.RABBIT_FOOT,
                Material.PHANTOM_MEMBRANE,
                Material.DRAGON_BREATH
        );
        GROUP_BREWING.setDesc("Brewing Materials and Ingredients");

        GROUP_POTIONS.addAll(
                Material.POTION,
                Material.SPLASH_POTION,
                Material.LINGERING_POTION
        );
        GROUP_POTIONS.setDesc("Potions and Empty Bottles");

        GROUP_WOOD.addAll(
                Material.OAK_PLANKS,
                Material.OAK_LOG,
                Material.OAK_WOOD,
                Material.STRIPPED_OAK_LOG,
                Material.STRIPPED_OAK_WOOD,
                Material.SPRUCE_LOG,
                Material.SPRUCE_PLANKS,
                Material.SPRUCE_SLAB,
                Material.SPRUCE_WOOD,
                Material.STRIPPED_SPRUCE_LOG,
                Material.STRIPPED_SPRUCE_WOOD,
                Material.BIRCH_LOG,
                Material.BIRCH_PLANKS,
                Material.BIRCH_SLAB,
                Material.BIRCH_WOOD,
                Material.STRIPPED_BIRCH_LOG,
                Material.STRIPPED_BIRCH_WOOD,
                Material.JUNGLE_LOG,
                Material.JUNGLE_PLANKS,
                Material.JUNGLE_SLAB,
                Material.JUNGLE_WOOD,
                Material.STRIPPED_JUNGLE_LOG,
                Material.STRIPPED_JUNGLE_WOOD,
                Material.ACACIA_LOG,
                Material.ACACIA_PLANKS,
                Material.ACACIA_SLAB,
                Material.ACACIA_WOOD,
                Material.STRIPPED_ACACIA_LOG,
                Material.STRIPPED_ACACIA_WOOD,
                Material.DARK_OAK_LOG,
                Material.DARK_OAK_PLANKS,
                Material.DARK_OAK_SLAB,
                Material.DARK_OAK_WOOD,
                Material.STRIPPED_DARK_OAK_LOG,
                Material.STRIPPED_DARK_OAK_WOOD
        );
        GROUP_WOOD.setDesc("Logs, Planks, and Slabs");

        GROUP_REDSTONE.addAll(
                Material.REDSTONE,
                Material.REDSTONE_BLOCK,
                Material.REDSTONE_LAMP,
                Material.REDSTONE_TORCH,
                Material.REDSTONE_WALL_TORCH,
                Material.REDSTONE_WIRE,
                Material.DISPENSER,
                Material.NOTE_BLOCK,
                Material.PISTON,
                Material.STICKY_PISTON,
                Material.TNT,
                Material.LEVER,
                Material.TRAPPED_CHEST,
                Material.STONE_PRESSURE_PLATE,
                Material.OAK_PRESSURE_PLATE,
                Material.SPRUCE_PRESSURE_PLATE,
                Material.BIRCH_PRESSURE_PLATE,
                Material.JUNGLE_PRESSURE_PLATE,
                Material.ACACIA_PRESSURE_PLATE,
                Material.DARK_OAK_PRESSURE_PLATE,
                Material.HEAVY_WEIGHTED_PRESSURE_PLATE,
                Material.LIGHT_WEIGHTED_PRESSURE_PLATE,
                Material.STONE_BUTTON,
                Material.OAK_BUTTON,
                Material.SPRUCE_BUTTON,
                Material.BIRCH_BUTTON,
                Material.JUNGLE_BUTTON,
                Material.ACACIA_BUTTON,
                Material.DARK_OAK_BUTTON,
                Material.OAK_TRAPDOOR,
                Material.SPRUCE_TRAPDOOR,
                Material.BIRCH_TRAPDOOR,
                Material.JUNGLE_TRAPDOOR,
                Material.ACACIA_TRAPDOOR,
                Material.DARK_OAK_TRAPDOOR,
                Material.IRON_TRAPDOOR,
                Material.TRIPWIRE_HOOK,
                Material.DAYLIGHT_DETECTOR,
                Material.HOPPER,
                Material.DROPPER,
                Material.OBSERVER,
                Material.IRON_DOOR,
                Material.REPEATER,
                Material.COMPARATOR,
                Material.REDSTONE_TORCH
        );

        GROUP_OTHER.addAll(
                Material.GRASS_BLOCK
        );

        groups.add(GROUP_WEAPONS);
        groups.add(GROUP_ARMOR);
        groups.add(GROUP_TOOLS);
        groups.add(GROUP_FOOD);
        groups.add(GROUP_POTIONS);
        groups.add(GROUP_UTILS);
        groups.add(GROUP_CROPS);
        groups.add(GROUP_REDSTONE);
        groups.add(GROUP_BREWING);
        groups.add(GROUP_DYES);
        groups.add(GROUP_ORES);
        groups.add(GROUP_WOOD);
        groups.add(GROUP_OTHER);
    }
}
