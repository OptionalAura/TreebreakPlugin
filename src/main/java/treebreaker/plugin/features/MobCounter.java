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

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;

import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.Comparator;

/**
 *
 * @author Daniel Allen
 */
public class MobCounter {
    private static final Comparator<AbstractMap.SimpleEntry<EntityType, Integer>> MOB_COUNT_COMPARATOR = new Comparator<AbstractMap.SimpleEntry<EntityType, Integer>>(){
        @Override
        public int compare(AbstractMap.SimpleEntry<EntityType, Integer> o1, AbstractMap.SimpleEntry<EntityType, Integer> o2) {
            
            return o1.getValue().compareTo(o2.getValue());
        }
        
    };
    public static boolean run(CommandSender sender, String[] args) {
        if (args.length == 0 || args[0] == null || args[0].length() == 0) {
            sender.sendMessage("Count of \"all:\" " + getCountOf("all"));
        } else {
            String lastFlag = "";
            
            boolean advanced = false;
            boolean verbose = false;
            boolean sort = false;
            
            int groupCount = 0;
            
            for(int i = 0; i < args.length; i++){
                String arg = args[i];
                if(lastFlag.length() == 0){
                    switch(arg.toLowerCase()){
                        case "\\a":
                            advanced = true;
                            break;
                        case "\\g":
                            lastFlag = "g";
                            break;
                        case "\\v":
                            verbose = true;
                            break;
                        case "\\s":
                            sort = true;
                            break;
                    }
                } else {
                    switch(lastFlag){
                        case "g":
                            try{
                                groupCount = Integer.parseInt(arg);
                            } catch (Exception e){
                                return false;
                            }
                            break;
                    }
                }
            }
            if(verbose){
                sender.sendMessage("Count of: ");
                
                ArrayList<AbstractMap.SimpleEntry<EntityType, Integer>> mobArray = new ArrayList<>(EntityType.values().length);
                for (int i = 0; i < EntityType.values().length; i++) {
                    AbstractMap.SimpleEntry<EntityType, Integer> entry = new AbstractMap.SimpleEntry<EntityType, Integer>(EntityType.values()[i], getCountOf(EntityType.values()[i].toString()));
                    mobArray.set(i, entry);
                }
                mobArray.sort(MOB_COUNT_COMPARATOR);
                for(int i = 0; i < mobArray.size(); i++){
                    sender.sendMessage("        - " + mobArray.get(i).getKey().toString() + ": " + mobArray.get(i).getValue());
                }
            } else {
                sender.sendMessage("Count of \"" + args[0] + ":\" " + getCountOf(args[0]));
            }
        }
        return true;
    }

    private static int getCountOf(String name) {
        int count = 0;
        switch (name.toLowerCase().trim()) {
            case "all":
                for (World w : Bukkit.getWorlds()) {
                    for (Entity e : w.getEntities()) {
                        count++;
                    }
                }
                break;
            case "player":
                for (Player p : Bukkit.getOnlinePlayers()) {
                    count++;
                }
                break;
            default:
                for (World w : Bukkit.getWorlds()) {
                    for (Entity e : w.getEntities()) {
                        if (e.getType().toString().equalsIgnoreCase(name)) {
                            count++;
                        }
                    }
                }
                break;
        }
        return count;
    }
}
