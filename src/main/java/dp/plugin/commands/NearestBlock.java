package main.java.dp.plugin.commands;

import java.util.ArrayList;
import main.java.dp.plugin.Main;
import static main.java.dp.plugin.Main.bsc;
import static main.java.dp.plugin.utils.ErrorWriter.printErrorToConsole;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class NearestBlock {

    public static boolean NearestBlock(CommandSender sender, Command cmd, String cmdLabel, String[] args) {
        if(!sender.isOp()){
            sender.sendMessage("§cYou must have OP to access this command.");
            return true;
        }
        BlockFinder finder = new BlockFinder(sender, cmd, cmdLabel, args);
        try{
            bsc.runTaskAsynchronously(Main.thisPlugin, finder);
        }catch(Exception ex){
            printErrorToConsole(ex);
            return false;
        }
        return true;
    }
}

class BlockFinder implements Runnable {

    Material targetBlock;
    String inputBlockString;
    CommandSender sender;
    double maxDistance;
    boolean hasBlock = false;
    long millisStart;
    long millisEnd;

    public BlockFinder(CommandSender sender, Command cmd, String cmdLabel, String[] args) {
        /*
        ARGUMENTS:
        args[0] : target block
        args[1] : range
         */
        millisStart = System.currentTimeMillis();
        this.sender = sender;
        this.inputBlockString = args[0];
        if(args.length == 2)
            this.maxDistance = Double.parseDouble((args[1].replaceAll("[^0-9.]", "").isEmpty() ? "50" : args[1].replaceAll("[^0-9.]", "")));
        else if(args.length <= 2)
            this.maxDistance = 10;
    }

    @Override
    public void run() {
        synchronized (this) {
            ArrayList<String> locationsFound = new ArrayList<>();
            this.targetBlock = Material.getMaterial(inputBlockString.toUpperCase());
            //this.targetBlock = Material.STONE;
            Location playerPos = ((Player) sender).getLocation();
            World worldIn = playerPos.getWorld();
            if(targetBlock == null){
                sender.sendMessage("§o\"" + inputBlockString + "\"§r is not a valid block");
                return;
            }else if(!targetBlock.isBlock()){
                sender.sendMessage("§o\"" + inputBlockString + "\"§r is not a valid block");
                return;
            }
            double minX = (playerPos.getX()-maxDistance/2);
            double minY = Math.max(0,playerPos.getY()-maxDistance/2);
            double minZ = (playerPos.getZ()-maxDistance/2);


            double maxX = (playerPos.getX()+maxDistance/2);
            double maxY = Math.min(255,playerPos.getY()+maxDistance/2);
            double maxZ = (playerPos.getZ()+maxDistance/2);


            sender.sendMessage("Searching from §a§l(" + ((int)minX) + ", " + ((int)minY) + ", " + ((int)minZ) + ")§r to §a§l(" + ((int)maxX) + ", " + ((int)maxY) + ", " + ((int)maxZ) + ")§r...");
            TextComponent msg = new TextComponent(ChatColor.WHITE+"Found blocks at: ");

            for (double x = -maxDistance / 2; x < maxDistance / 2; x++) {
                for (double z = -maxDistance / 2; z < maxDistance; z++) {
                    for (double y = -maxDistance; y < maxDistance / 2; y++) {
                        double searchX = playerPos.getX() + x;
                        double searchY = Math.min(Math.max(0, playerPos.getY() + y), 255);
                        double searchZ = playerPos.getZ() + z;
                        try {
                            if (worldIn.getBlockAt((int) searchX, (int) searchY, (int) searchZ).getType() == targetBlock) {
                                hasBlock = true;
                                TextComponent cur = new TextComponent(("§b§l(" + ((int)searchX) + ", " + ((int)searchY) + ", " + ((int)searchZ) + ")§r"));
                                cur.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder(ChatColor.GOLD+"Click to safely teleport").create()));
                                cur.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, ("/tp " + sender.getName() + " " + ((int)searchX) + " " + ((int)searchY) + " " + ((int)searchZ))));

                                msg.addExtra(cur);
                            }
                        } catch (Exception ex) {

                        }
                    }
                }
            }
            millisEnd = System.currentTimeMillis();
            long taken = (millisEnd-millisStart)/1000;
            if (hasBlock) {
                String outputString = "";
                int max = Math.min(locationsFound.size(), 20);
                for (int i = 0; i < max; i++) {
                    //outputString += locationsFound.get(i) + (i == max - 1 ? "" : ", ");
                }
                sender.sendMessage("Search complete. Took §l"+taken+" seconds.");
                sender.spigot().sendMessage(msg);
            }else{
                sender.sendMessage("No blocks were found.");
            }

        }
    }
}
