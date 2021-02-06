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
import main.java.treebreaker.plugin.features.AllEnchant;
import main.java.treebreaker.plugin.features.EZEnchant;
import main.java.treebreaker.plugin.features.MobAutofill;
import main.java.treebreaker.plugin.features.MobCounter;
import main.java.treebreaker.plugin.features.SilkSpawners;
import main.java.treebreaker.plugin.features.SleepFixes;
import main.java.treebreaker.plugin.features.TreeBreaker;
import main.java.treebreaker.plugin.misc.ActionBarAPI;
import main.java.treebreaker.plugin.misc.Events;
import main.java.treebreaker.plugin.misc.Permissions;
import main.java.treebreaker.plugin.misc.UpdateNotifier;
import main.java.treebreaker.plugin.utils.Utils;
import static main.java.treebreaker.plugin.utils.Utils.getProperty;
import static main.java.treebreaker.plugin.utils.Utils.setProperty;
import main.java.treebreaker.plugin.utils.Version;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitScheduler;

/*
    @author Daniel Allen
    13-Aug-2019
 */
public class Main extends JavaPlugin implements Listener {

    public static boolean enabled = true;
    public static BukkitScheduler bsc;
    public static Plugin thisPlugin;
    //public static FileLock lock;
    public static Version version;
    public static File settingsFile;
    public static FileConfiguration settingsConfig;

    public static String updateMessage = "";

    private static BukkitRunnable tickCounter;

    public static boolean updateAvailable = false;

    private static long tick = 0;

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

    public static BukkitScheduler getScheduler() {
        return bsc;
    }

    public static boolean isUpdateAvailable() {
        return updateAvailable;
    }

    public static long getCurrentTick() {
        return tick;
    }

    public static boolean isNumbersOnly(String input, boolean allowDecimals, boolean allowNegative, boolean allowInfinity, boolean allowEmpty) {
        if (input == null) {
            return false;
        }
        if (!allowEmpty && input.isEmpty()) {
            return false;
        }
        if (allowInfinity && (input.equalsIgnoreCase("Infinite") || input.equalsIgnoreCase("Infinity") || input.equalsIgnoreCase("∞"))) {
            return true;
        }
        if (allowDecimals) {
            if (allowNegative) {
                return input.matches("[0-9.-]+$");
            }
            return input.matches("[0-9.]+$");
        } else {
            if (allowNegative) {
                return input.matches("[0-9-]+$");
            }
            return input.matches("[0-9]+$");
        }
    }

    public static String getUpdateMessage() {
        return updateMessage;
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

        Permissions.load();
        ActionBarAPI.load();
        
        Bukkit.getPluginManager().registerEvents(new UpdateNotifier(), this);
        Bukkit.getPluginManager().registerEvents(new TreeBreaker(), this);
        Bukkit.getPluginManager().registerEvents(new SilkSpawners(), this);
        Bukkit.getPluginManager().registerEvents(new ColoredNames(), this);
        Bukkit.getPluginManager().registerEvents(new DeathMarkers(), this);
        Bukkit.getPluginManager().registerEvents(new SleepFixes(), this);
        Bukkit.getPluginManager().registerEvents(new Events(), this);

        
        try {
            String currentVersion = this.getDescription().getVersion();
            if (currentVersion.matches("^[0-9.]+$")) {
                Main.version = new Version(currentVersion);
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
                    Version updatedVersion = new Version(line);
                    if (Main.version.compareTo(updatedVersion) == -1) {
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
        try {
            getCommand("mobCount").setTabCompleter(new MobAutofill(0));
        } catch (Exception e) {
            Bukkit.getConsoleSender().sendMessage("Failed to register tab completion");
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

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (cmd.getName().equalsIgnoreCase("deathLocation") || cmd.getName().equalsIgnoreCase("track")) {
            return DeathMarkers.onCommand(sender, cmd, label, args);
        } else if (cmd.getName().equalsIgnoreCase("setProperty")) {
            if (sender.isOp()) {
                if (args.length == 0 || args[0].equalsIgnoreCase("?") || args[0].isEmpty()) {
                    sender.sendMessage("All properties: " + Utils.getAllProperties());
                } else {
                    if (Utils.hasProperty(args[0])) {
                        if (args.length > 1) {
                            switch (args[1].toUpperCase()) {
                                case "FALSE":
                                    Utils.setProperty(args[0], false);
                                case "TRUE":
                                    Utils.setProperty(args[0], true);
                                    break;
                                default:
                                    if (isNumbersOnly(args[1], false, false, true, false)) {
                                        Utils.setProperty(args[0], Integer.parseInt(args[1]));
                                    } else if (settingsConfig.getDefaults() != null) {
                                        Object def = settingsConfig.getDefaults().get(args[0]);
                                        if (def != null) {
                                            Utils.setProperty(args[0], def);
                                        }
                                    }
                                    break;
                            }
                        } else {
                            sender.sendMessage("Property is currently: " + Utils.getProperty(args[0]).toString());
                        }
                    } else {
                        sender.sendMessage("All properties: " + Utils.getAllProperties());
                    }
                }
            }
        } else if (cmd.getName().equalsIgnoreCase("treebreak") && sender.isOp()) {
            try {
                URL updateCheckURL = new URL("https://raw.githubusercontent.com/OptionalAura/TreebreakPlugin/master/src/plugin.yml");
                try ( BufferedReader br = new BufferedReader(new InputStreamReader(updateCheckURL.openStream()))) {
                    String line;
                    while ((line = br.readLine()) != null) {
                        if (line.startsWith("version: ")) {
                            line = Utils.stringAfter(line, "version: ");
                            Version updatedVersion = new Version(line);
                            if (Main.version.compareTo(updatedVersion) == -1) {
                                updateAvailable = true;
                                updateMessage = "There is an update available for " + this.getDescription().getName() + "(v. " + this.getDescription().getVersion() + " -> v. " + line.replaceAll("[^0-9.]", "") + ")";
                                Bukkit.getConsoleSender().sendMessage(ChatColor.GREEN + updateMessage + ChatColor.RESET);
                            }
                        }
                    }
                }
            } catch (IOException | NumberFormatException e) {
                Bukkit.getConsoleSender().sendMessage(ChatColor.DARK_RED + "Error detecting updates for Treebreaker:" + ChatColor.RESET);
                Bukkit.getConsoleSender().sendMessage(ChatColor.DARK_RED + e.getLocalizedMessage() + ChatColor.RESET);
            }
        } else if (cmd.getName().equalsIgnoreCase("mobCount")) {
            MobCounter.run(sender, args);
            return true;
        } else if (cmd.getName().equalsIgnoreCase("AllEnchant")){
            AllEnchant.run(sender, cmd, label, args);
            return true;
        } else if (cmd.getName().equalsIgnoreCase("EZEnchant")){
            EZEnchant.run(sender, cmd, label, args);
            return true;
        }
        return true;
    }
    
}
