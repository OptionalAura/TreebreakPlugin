package main.java.treebreaker.plugin;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;
import main.java.treebreaker.plugin.features.ColoredNames;
import main.java.treebreaker.plugin.features.DeathMarkers;
import main.java.treebreaker.plugin.features.SilkSpawners;
import main.java.treebreaker.plugin.features.SleepFixes;
import main.java.treebreaker.plugin.features.TreeBreaker;
import main.java.treebreaker.plugin.misc.ActionBarAPI;
import main.java.treebreaker.plugin.misc.Events;
import main.java.treebreaker.plugin.utils.Utils;
import static main.java.treebreaker.plugin.utils.Utils.getProperty;
import static main.java.treebreaker.plugin.utils.Utils.setProperty;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitScheduler;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.event.Listener;
import org.bukkit.scheduler.BukkitRunnable;

/*
    @author Daniel Allen
    13-Aug-2019
 */
public class Main extends JavaPlugin implements Listener {

    public static boolean enabled = true;
    public static BukkitScheduler bsc;
    public static Plugin thisPlugin;
    //public static FileLock lock;
    private static double version = 1.1;
    public static File settingsFile;
    public static FileConfiguration settingsConfig;

    public static void saveSettings() {
        settingsConfig.set("colors." + ColoredNames.ALLOW_CHAT_COLORS_TAG, getProperty(ColoredNames.ALLOW_CHAT_COLORS_TAG, true));
        settingsConfig.set("colors." + ColoredNames.ALLOW_ITEM_COLORS_TAG, getProperty(ColoredNames.ALLOW_ITEM_COLORS_TAG, true));

        settingsConfig.set("silkspawners." + SilkSpawners.SILK_SPAWNERS_ENABLED_TAG, getProperty(SilkSpawners.SILK_SPAWNERS_ENABLED_TAG, true));
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
        settingsConfig.set("deathmarkers." + DeathMarkers.DEATH_LOCATION_MESSAGE_TAG, getProperty(DeathMarkers.DEATH_LOCATION_MESSAGE_TAG, true));

        settingsConfig.set("sleeping." + SleepFixes.ONE_SLEEPING_PLAYER_TAG, getProperty(SleepFixes.ONE_SLEEPING_PLAYER_TAG, true));
    }

    public static void loadSettings() {
        setProperty(ColoredNames.ALLOW_CHAT_COLORS_TAG, settingsConfig.getBoolean("colors." + ColoredNames.ALLOW_CHAT_COLORS_TAG, true));
        setProperty(ColoredNames.ALLOW_ITEM_COLORS_TAG, settingsConfig.getBoolean("colors." + ColoredNames.ALLOW_ITEM_COLORS_TAG, true));

        setProperty(SilkSpawners.SILK_SPAWNERS_ENABLED_TAG, settingsConfig.getBoolean("silkspawners." + SilkSpawners.SILK_SPAWNERS_ENABLED_TAG, true));
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
        setProperty(DeathMarkers.DEATH_LOCATION_MESSAGE_TAG, settingsConfig.getBoolean("deathmarkers." + DeathMarkers.DEATH_LOCATION_MESSAGE_TAG, true));

        setProperty(SleepFixes.ONE_SLEEPING_PLAYER_TAG, settingsConfig.getBoolean("sleeping." + SleepFixes.ONE_SLEEPING_PLAYER_TAG, true));
    }

    @Override
    public void onLoad() {

    }
    private static String updateMessage = "";

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
        Bukkit.getPluginManager().registerEvents(new SleepFixes(), this);
        Bukkit.getPluginManager().registerEvents(new Events(), this);

        ActionBarAPI.load();
        try {
            String version = this.getDescription().getVersion();
            if (version.matches("^[0-9.]+$")) {
                this.version = Double.parseDouble(version);
            }
        } catch (Exception e) {

        }
        try {
            URL updateCheckURL = new URL("https://raw.githubusercontent.com/OptionalAura/TreebreakPlugin/master/src/plugin.yml");
            BufferedReader br = new BufferedReader(new InputStreamReader(updateCheckURL.openStream()));
            String line;
            while ((line = br.readLine()) != null) {
                if (line.startsWith("version: ")) {
                    line = Utils.stringAfter(line, "version: ");
                    if (line.replaceAll("[^0-9.]", "").matches("^[0-9.]+$") && Double.parseDouble(line.replaceAll("[^0-9.]", "")) > this.version) {
                        updateAvailable = true;
                        updateMessage = "There is an update available for " + this.getDescription().getName() + "(v. " + this.getDescription().getVersion() + " -> v. " + line.replaceAll("[^0-9.]", "") + ")";
                        Bukkit.getConsoleSender().sendMessage(ChatColor.GREEN + updateMessage + ChatColor.RESET);
                    }
                }
            }
            br.close();
        } catch (IOException | NumberFormatException e) {
            Bukkit.getConsoleSender().sendMessage(ChatColor.DARK_RED + "Error detecting updates for Treebreaker:" + ChatColor.RESET);
            Bukkit.getConsoleSender().sendMessage(ChatColor.DARK_RED + e.getLocalizedMessage() + ChatColor.RESET);
        }
        if (tickCounter != null) {
            tickCounter.cancel();
        }
        tickCounter = new BukkitRunnable() {
            @Override
            public void run() {
                tick++;
            }
        };
        tick = 0;
        tickCounter.runTaskTimerAsynchronously(thisPlugin, 1, 1);
    }

    private static BukkitRunnable tickCounter;

    public static BukkitScheduler getScheduler() {
        return bsc;
    }

    @Override
    public void onDisable() {
        if (tickCounter != null) {
            tickCounter.cancel();
        }
        saveSettings();
        try {
            settingsConfig.save(settingsFile);
        } catch (IOException ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        }
        Bukkit.getConsoleSender().sendMessage("Plugin Disabled");
    }
    private static boolean updateAvailable = false;

    public static boolean isUpdateAvailable() {
        return updateAvailable;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (cmd.getName().equalsIgnoreCase("deathLocation")) {
            return DeathMarkers.onCommand(sender, cmd, label, args);
        } else if (cmd.getName().equalsIgnoreCase("treebreak") && sender.isOp()) {
            try {
                URL updateCheckURL = new URL("https://raw.githubusercontent.com/OptionalAura/TreebreakPlugin/master/src/plugin.yml");
                BufferedReader br = new BufferedReader(new InputStreamReader(updateCheckURL.openStream()));
                String line;
                while ((line = br.readLine()) != null) {
                    if (line.startsWith("version: ")) {
                        line = Utils.stringAfter(line, "version: ");
                        if (line.replaceAll("[^0-9.]", "").matches("^[0-9.]+$") && Double.parseDouble(line.replaceAll("[^0-9.]", "")) > this.version) {
                            updateAvailable = true;
                            updateMessage = "There is an update available for " + this.getDescription().getName() + "(v. " + this.getDescription().getVersion() + " -> v. " + line.replaceAll("[^0-9.]", "") + ")";
                            sender.sendMessage(ChatColor.GREEN + updateMessage + ChatColor.RESET);
                        } else {
                            sender.sendMessage("Using " + this.getDescription().getName() + " version " + this.getDescription().getVersion());
                        }
                    }
                }
                br.close();
            } catch (IOException | NumberFormatException e) {
                Bukkit.getConsoleSender().sendMessage(ChatColor.DARK_RED + "Error detecting updates for Treebreaker:" + ChatColor.RESET);
                Bukkit.getConsoleSender().sendMessage(ChatColor.DARK_RED + e.getLocalizedMessage() + ChatColor.RESET);
            }
        }
        return true;
    }

    private static long tick = 0;

    public static long getCurrentTick() {
        return tick;
    }

    public static String getUpdateMessage() {
        return updateMessage;
    }
}
