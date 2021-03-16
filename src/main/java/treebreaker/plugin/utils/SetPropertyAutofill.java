/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package main.java.treebreaker.plugin.utils;

import java.util.ArrayList;
import java.util.List;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

/**
 *
 * @author dsato
 */
public class SetPropertyAutofill implements TabCompleter {

    int[] positions;
    TabCompleter alternate;

    public SetPropertyAutofill() {
        this.positions = new int[]{0};
    }

    public SetPropertyAutofill(int... pos) {
        this.positions = pos;
    }

    public SetPropertyAutofill(TabCompleter alternate, int... pos) {
        this.positions = pos;
        this.alternate = alternate;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        List<String> list = new ArrayList<>();
        boolean skipUse = true;
        if (sender instanceof Player) {
            for (int pos : positions) {
                if (args.length == pos + 1) {
                    skipUse = false;
                    for (String str : Utils.getAllPropertyKeys()) {
                        if (args[pos] == null || args[pos].length() == 0 || Utils.startsWithIgnoreCase(str,args[pos])) {
                            list.add(str);
                        }
                    }
                }
            }
        }
        if (skipUse && alternate != null) {
            return alternate.onTabComplete(sender, command, alias, args);
        }
        return list;
    }
}