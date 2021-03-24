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
package main.java.treebreaker.plugin.features;

import main.java.treebreaker.plugin.utils.Utils;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Daniel Allen
 */
public class MobAutofill implements TabCompleter {

    int[] positions;
    TabCompleter alternate;

    public MobAutofill() {
        this.positions = new int[]{0};
    }

    public MobAutofill(int... pos) {
        this.positions = pos;
    }

    public MobAutofill(TabCompleter alternate, int... pos) {
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
                    if (Utils.startsWithIgnoreCase("ALL",args[pos]) || args[pos] == null || args[pos].length() == 0) {
                        list.add("ALL");
                    }
                    if (Utils.startsWithIgnoreCase("PLAYER",args[pos]) || args[pos] == null || args[pos].length() == 0) {
                        list.add("PLAYER");
                    }
                    for (EntityType e : EntityType.values()) {
                        if (args[pos] == null || args[pos].length() == 0 || Utils.startsWithIgnoreCase(e.toString(),args[pos])) {
                            list.add(e.toString());
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