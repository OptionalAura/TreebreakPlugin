package main.java.treebreaker.plugin;

import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import main.java.treebreaker.plugin.features.ColoredNames;
import main.java.treebreaker.plugin.features.DeathMarkers;
import main.java.treebreaker.plugin.features.SilkSpawners;
import main.java.treebreaker.plugin.features.TreeBreaker;
import main.java.treebreaker.plugin.misc.ActionBarAPI;
import main.java.treebreaker.plugin.utils.Scheduler;
import static main.java.treebreaker.plugin.utils.Utils.getProperty;
import static main.java.treebreaker.plugin.utils.Utils.setProperty;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitScheduler;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

/*
    @author Daniel Allen
    13-Aug-2019
 */
public class Main extends JavaPlugin {

    public static boolean enabled = true;
    public static BukkitScheduler bsc;
    public static Plugin thisPlugin;
    //public static FileLock lock;

    public static File settingsFile;
    public static FileConfiguration settingsConfig;
    
    public static void saveSettings() {
        settingsConfig.set("colors." + ColoredNames.ALLOW_CHAT_COLORS_TAG, getProperty(ColoredNames.ALLOW_CHAT_COLORS_TAG, true));
        settingsConfig.set("colors." + ColoredNames.ALLOW_ITEM_COLORS_TAG, getProperty(ColoredNames.ALLOW_ITEM_COLORS_TAG, true));
        
        settingsConfig.set("silkspawners." + SilkSpawners.DROP_SPAWNERS_IN_CREATIVE_TAG, getProperty(SilkSpawners.DROP_SPAWNERS_IN_CREATIVE_TAG, true));
        
        settingsConfig.set("treecutter." + TreeBreaker.TREE_BREAK_ENABLED_TAG, getProperty(TreeBreaker.TREE_BREAK_ENABLED_TAG, true));
        settingsConfig.set("treecutter." + TreeBreaker.MAX_TREES_BROKEN_TAG, getProperty(TreeBreaker.MAX_TREES_BROKEN_TAG, 2048));
        settingsConfig.set("treecutter." + TreeBreaker.CONNECTS_DIAGONALLY_TAG, getProperty(TreeBreaker.CONNECTS_DIAGONALLY_TAG, true));
        settingsConfig.set("treecutter." + TreeBreaker.DROPS_ITEMS_IN_CREATIVE_TAG, getProperty(TreeBreaker.DROPS_ITEMS_IN_CREATIVE_TAG, false));
        settingsConfig.set("treecutter." + TreeBreaker.SNEAK_TO_BREAK_NORMALLY_TAG, getProperty(TreeBreaker.SNEAK_TO_BREAK_NORMALLY_TAG, true));
        //settingsConfig.set("treecutter." + TreeBreaker.USES_AXE_DURABILITY_TAG, getProperty(TreeBreaker.USES_AXE_DURABILITY_TAG, true));
        
        settingsConfig.set("deathmarkers." + DeathMarkers.DEATH_COMPASS_ENABLED_TAG, getProperty(DeathMarkers.DEATH_COMPASS_ENABLED_TAG, true));
        settingsConfig.set("deathmarkers." + DeathMarkers.DEATH_MARKER_ENABLED_TAG, getProperty(DeathMarkers.DEATH_MARKER_ENABLED_TAG, true));
        settingsConfig.set("deathmarkers." + DeathMarkers.DEATH_MARKER_TIME_TAG, getProperty(DeathMarkers.DEATH_MARKER_TIME_TAG, 300));
    }

    public static void loadSettings() {
        setProperty(ColoredNames.ALLOW_CHAT_COLORS_TAG, settingsConfig.getBoolean("colors." + ColoredNames.ALLOW_CHAT_COLORS_TAG, true));
        setProperty(ColoredNames.ALLOW_ITEM_COLORS_TAG, settingsConfig.getBoolean("colors." + ColoredNames.ALLOW_ITEM_COLORS_TAG, true));

        setProperty(SilkSpawners.DROP_SPAWNERS_IN_CREATIVE_TAG, settingsConfig.getBoolean("silkspawners." + SilkSpawners.DROP_SPAWNERS_IN_CREATIVE_TAG, true));

        setProperty(TreeBreaker.TREE_BREAK_ENABLED_TAG, settingsConfig.getBoolean("treecutter." + TreeBreaker.TREE_BREAK_ENABLED_TAG, true));
        setProperty(TreeBreaker.MAX_TREES_BROKEN_TAG, settingsConfig.getLong("treecutter." + TreeBreaker.MAX_TREES_BROKEN_TAG, 2048));
        setProperty(TreeBreaker.CONNECTS_DIAGONALLY_TAG, settingsConfig.getBoolean("treecutter." + TreeBreaker.CONNECTS_DIAGONALLY_TAG, true));
        setProperty(TreeBreaker.DROPS_ITEMS_IN_CREATIVE_TAG, settingsConfig.getBoolean("treecutter." + TreeBreaker.DROPS_ITEMS_IN_CREATIVE_TAG, false));
        setProperty(TreeBreaker.SNEAK_TO_BREAK_NORMALLY_TAG, settingsConfig.getBoolean("treecutter." + TreeBreaker.SNEAK_TO_BREAK_NORMALLY_TAG, true));
        //setProperty(TreeBreaker.USES_AXE_DURABILITY_TAG, settingsConfig.getBoolean("treecutter." + TreeBreaker.USES_AXE_DURABILITY_TAG, true));
        
        setProperty(DeathMarkers.DEATH_COMPASS_ENABLED_TAG, settingsConfig.getBoolean("deathmarkers." + DeathMarkers.DEATH_COMPASS_ENABLED_TAG, true));
        setProperty(DeathMarkers.DEATH_MARKER_ENABLED_TAG, settingsConfig.getBoolean("deathmarkers." + DeathMarkers.DEATH_MARKER_ENABLED_TAG, true));
        setProperty(DeathMarkers.DEATH_MARKER_TIME_TAG, settingsConfig.getLong("deathmarkers." + DeathMarkers.DEATH_MARKER_TIME_TAG, 300));
    }

    @Override
    public void onLoad() {

    }

    @Override
    public void onEnable() {
        bsc = this.getServer().getScheduler();
        thisPlugin = getPlugin(Main.class);

        settingsFile = new File(getDataFolder(), "settings.yml");
        settingsConfig = new YamlConfiguration();

        if (!settingsFile.exists()) {
            try {
                settingsFile.getParentFile().mkdirs();
                settingsFile.createNewFile();
            } catch (IOException ex) {
                Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        try {
            settingsConfig.load(settingsFile);
        } catch (IOException | InvalidConfigurationException ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        loadSettings();

        saveSettings();
        try {
            settingsConfig.save(settingsFile);
        } catch (IOException ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        Bukkit.getPluginManager().registerEvents(new TreeBreaker(), this);
        Bukkit.getPluginManager().registerEvents(new SilkSpawners(), this);
        Bukkit.getPluginManager().registerEvents(new ColoredNames(), this);
        Bukkit.getPluginManager().registerEvents(new DeathMarkers(), this);
        Scheduler.setTimerTickSpeed(1000);
        ActionBarAPI.load();
    }

    public static BukkitScheduler getScheduler() {
        return bsc;
    }

    @Override
    public void onDisable() {
        Scheduler.stopTimer();
        saveSettings();
        try {
            settingsConfig.save(settingsFile);
        } catch (IOException ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        }
        Bukkit.getConsoleSender().sendMessage("Plugin Disabled");
    }
}
