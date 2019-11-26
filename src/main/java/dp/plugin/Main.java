package main.java.dp.plugin;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.channels.FileLock;
import main.java.dp.plugin.features.ColoredNames;
import main.java.dp.plugin.features.EnchToBook;
import main.java.dp.plugin.commands.tabs.DPEnchantTabCompletion;
import main.java.dp.plugin.features.AllowEnchants;
import java.util.Arrays;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;
import main.java.dp.plugin.commands.Registry;
import main.java.dp.plugin.commands.tabs.NearestBlockTabCompletion;
import main.java.dp.plugin.data.DataSerializable;
import main.java.dp.plugin.features.FastTransfer;
import main.java.dp.plugin.maps.EnchantmentData;
import main.java.dp.plugin.maps.Groups;
import main.java.dp.plugin.pvp.PvPListeners;
import main.java.dp.plugin.pvp.PvPQueue;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitScheduler;

/*
    @author Daniel Allen
    13-Aug-2019
 */
public class Main extends JavaPlugin {

    public static BukkitScheduler bsc;
    public static Plugin thisPlugin;
    //public static FileLock lock;

    @Override
    public void onLoad() {
        Bukkit.getConsoleSender().sendMessage("§1------------------------------");
        Bukkit.getConsoleSender().sendMessage("§9  Loading Daniel's Plugin...  ");
        Bukkit.getConsoleSender().sendMessage("§1------------------------------");

    }

    @Override
    public void onEnable() {
        bsc = this.getServer().getScheduler();
        thisPlugin = getPlugin(Main.class);
        Bukkit.getConsoleSender().sendMessage("Loaded Daniel's Plugin.");

        Bukkit.getPluginManager().registerEvents(new EnchToBook(), this);
        Bukkit.getPluginManager().registerEvents(new AllowEnchants(), this);
        Bukkit.getPluginManager().registerEvents(new ColoredNames(), this);
        Bukkit.getPluginManager().registerEvents(new PvPQueue(), this);
        Bukkit.getPluginManager().registerEvents(new PvPListeners(), this);
        try {
            getCommand("dpEnchant").setTabCompleter(new DPEnchantTabCompletion());
            getCommand("findblock").setTabCompleter(new NearestBlockTabCompletion());
        } catch (NullPointerException ex) {
            Bukkit.getConsoleSender().sendMessage("§cError: " + ex.getMessage());
            try {
                Bukkit.getLogger().log(Level.SEVERE, Arrays.toString(ex.getStackTrace()));
            } catch (Exception except) {
                Bukkit.getConsoleSender().sendMessage("§cError: " + except.getMessage());
            }
        }
        for (World w : Main.thisPlugin.getServer().getWorlds()) {
            Bukkit.getConsoleSender().sendMessage("§9  Found world: " + w.getName());
        }
    }

    public static BukkitScheduler getScheduler() {
        return bsc;
    }

    @Override
    public void onDisable() {
        Bukkit.getConsoleSender().sendMessage("§cDaniel's Plugin Disabled.");
    }

    @Override
    public boolean onCommand(CommandSender player, Command cmd, String cmdLabel, String[] args) {
        return Registry.onCommand(player, cmd, cmdLabel, args);
    }
}
