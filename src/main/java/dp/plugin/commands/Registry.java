package main.java.dp.plugin.commands;

import main.java.dp.plugin.pvp.ClassCommand;
import java.util.List;
import java.util.ListIterator;
import static main.java.dp.plugin.commands.NearestBlock.NearestBlock;
import static main.java.dp.plugin.commands.SortCommand.SortStorage;
import static main.java.dp.plugin.pvp.PvPQueue.joinQueue;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/*
    @author Daniel Allen
    14-Aug-2019
 */
public class Registry {

    public static boolean onCommand(CommandSender player, Command cmd, String cmdLabel, String[] args) {
        List<String> aliases = cmd.getAliases();
        ListIterator<String> iterator = aliases.listIterator();
        while (iterator.hasNext()) {
            iterator.set(iterator.next().toLowerCase());
        }
        if (cmd.getName().equalsIgnoreCase("sayAs")) {
            return Sayas.run(player, cmd, cmdLabel, args);
        } else if (cmd.getName().equalsIgnoreCase("dpEnchant")) {
            return DpEnchant.run(player, cmd, cmdLabel, args);
        } else if (cmd.getName().equalsIgnoreCase("findBlock")) {
            if (player instanceof Player) {
                return NearestBlock(player, cmd, cmdLabel, args);
            }
        } else if (cmd.getName().equalsIgnoreCase("sort")) {
            return SortStorage(player, cmd, cmdLabel, args);
        } else if (cmd.getName().equalsIgnoreCase("lore") && player.isOp()) {
            return LoreCreator.run(player, cmd, cmdLabel, args);
        } else if (cmd.getName().equalsIgnoreCase("class")) {
            return ClassCommand.run(player);
        } else if (cmd.getName().equalsIgnoreCase("pvp")) {
            if (player instanceof Player) {
                joinQueue((Player)player);
                return true;
            }
        } else {
            player.sendMessage("No command found...");
        }
        return false;
    }
}
