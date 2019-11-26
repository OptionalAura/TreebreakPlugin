package main.java.dp.plugin.commands.tabs;

import java.util.ArrayList;
import java.util.List;
import main.java.dp.plugin.Main;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.configuration.file.FileConfiguration;

/*
    @author Daniel Allen
    16-Aug-2019
 */
public class DPEnchantTabCompletion implements TabCompleter {

    @Override
    public List<String> onTabComplete(CommandSender cs, Command cmd, String alias, String[] args) {
        if (cmd.getName().equalsIgnoreCase("dpEnchant")) {
            List<String> results = new ArrayList<>();
            if (args.length == 1) {
                results.add("max");
                if (args[0].isEmpty()) {
                    for (int i = 0; i < 10; i++) {
                        results.add("" + i);
                    }
                } else {
                    if (args[0].replaceAll("[^0-9]", "").isEmpty()) {

                    } else if (Integer.parseInt(args[0]) < 32767) {
                        int multiple = Integer.parseInt(args[0]);
                        results.add(multiple + "");
                        for (int i = 0; i < 10; i++) {
                            results.add(multiple + "" + i);
                        }
                    } else {
                        results.add("32767");
                    }
                }
            } else if (args.length == 2) {
                results.add("true");
                results.add("false");
            }
            return results;
        }
        return null;
    }
}
