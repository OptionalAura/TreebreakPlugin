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
package main.java.treebreaker.plugin.misc;

import org.bukkit.permissions.Permission;
import org.bukkit.plugin.PluginManager;

import java.util.HashMap;

import static org.bukkit.Bukkit.getServer;

/**
 *
 * @author Daniel Allen
 */
public class Permissions {
    static PluginManager pm;
    static HashMap<String, Permission> perms = new HashMap();
    public static void load(){
        pm = getServer().getPluginManager();
        definePerm("TreeEnchants");
        definePerm("guns");
    }
    public static void definePerm(String name){
        if(!perms.containsKey(name)){
            Permission perm = new Permission(name);
            perms.put(name, perm);
            pm.addPermission(perm);
        }
    }
    public static Permission getPerm(String name){
        return perms.get(name);
    }
}
