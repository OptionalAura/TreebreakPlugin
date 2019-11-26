package main.java.dp.plugin.commands.tabs;

import java.util.ArrayList;
import java.util.List;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;

/*
    @author Daniel Allen
    17-Aug-2019
 */
public class NearestBlockTabCompletion implements TabCompleter {

    @Override
    public List<String> onTabComplete(CommandSender cs, Command cmd, String alias, String[] args) {
        List<String> recommendations = new ArrayList<>();
        if (cmd.getName().equalsIgnoreCase("findblock")) {
            if (args.length == 1) {
                for (Material m : Material.values()) {
                    if (!m.isBlock()) {
                        continue;
                    }
                    if (m.name().toLowerCase().startsWith(args[0].toLowerCase())) {
                        recommendations.add(m.name().toLowerCase());
                    }
                }
            } else if (args.length == 2) {
                if (args[1].isEmpty()) {
                    for (int i = 0; i < 10; i++) {
                        recommendations.add("" + i);
                    }
                } else {
                    if (!args[1].replaceAll("[^0-9]", "").isEmpty() && Integer.parseInt(args[1]) < 100) {
                        int multiple = Integer.parseInt(args[1]);
                        recommendations.add(multiple + "");
                        for (int i = 0; i < 10; i++) {
                            if (multiple * 10 + i <= 100) {
                                recommendations.add(multiple + "" + i);
                            }
                        }
                    } else if (Integer.parseInt(args[0]) > 100) {
                        recommendations.add("100");
                    }
                }
            }
        }
        return recommendations;
    }
}
