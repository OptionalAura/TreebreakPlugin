package main.java.dp.plugin.commands;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

/*
    @author Daniel Allen
    14-Aug-2019
 */
public class Sayas {

    public static boolean run(CommandSender player, Command cmd, String cmdLabel, String[] args) {
        if (args.length > 1) {
            String targetExecutor = args[0];
            if(Bukkit.getPlayer(targetExecutor).isValid())
                targetExecutor = Bukkit.getPlayer(targetExecutor).getDisplayName();
            String text = "";
            for (int i = 1; i < args.length; i++) {
                text += args[i] + (i == args.length - 1 ? "" : " ");
            }
            Bukkit.broadcastMessage(ChatColor.DARK_AQUA + "[" + targetExecutor + "]" + ChatColor.DARK_GRAY + ": " + ChatColor.WHITE + text);

        } else {
            return false;
        }
        return true;
    }
}
