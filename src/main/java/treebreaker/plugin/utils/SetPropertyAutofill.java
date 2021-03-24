/*
 * Copyright (C) 2021 Daniel Allen
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package main.java.treebreaker.plugin.utils;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Daniel Allen
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