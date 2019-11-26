package main.java.dp.plugin.pvp;

import java.util.Map;
import java.util.TreeMap;
import static main.java.dp.plugin.Main.thisPlugin;
import main.java.dp.plugin.chestSelection.ChestMenu;
import static main.java.dp.plugin.pvp.PvPQueue.playerClasses;
import main.java.dp.plugin.pvp.classKits.Kit;
import main.java.dp.plugin.pvp.classKits.Kits;
import main.java.dp.plugin.pvp.classKits.Kits;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

/*
    @author Daniel Allen
    24-Nov-2019
 */
public class ClassCommand {

    public static boolean run(CommandSender sender) {
        if (sender instanceof Player && (((Player) sender).getWorld().getName().equalsIgnoreCase("creative") || ((Player) sender).isOp())) {
            final Player player = (Player) sender;
            ChestMenu menu = new ChestMenu("Class Selection", 9, new ChestMenu.OptionClickEventHandler() {
                @Override
                public void onOptionClick(ChestMenu.OptionClickEvent event) {
                    for (Kit k : Kits.kits) {
                        if (event.getPosition() == k.getPosition()) {
                            k.applyToPlayer(player, true);
                            if (playerClasses.containsKey(player.getUniqueId().toString())) {
                                playerClasses.replace(player.getUniqueId().toString(), k);
                            } else {
                                playerClasses.put(player.getUniqueId().toString(), k);
                            }
                            break;
                        }
                    }
                    player.setItemOnCursor(new ItemStack(Material.AIR));
                }

                @Override
                public void onClose(ChestMenu.CloseEvent event) {

                }
            }, thisPlugin);
            for(Kit k : Kits.kits){
                menu.setOption(k.getPosition(), k.getLabelItem(), k.getLabelItem().getItemMeta().getDisplayName(), "");
            }
            menu.openInventory(player);
            return true;
        }
        return false;
    }
}
