package main.java.dp.plugin.commands;

import org.bukkit.FluidCollisionMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.util.RayTraceResult;

/*
    @author Daniel Allen
    23-Aug-2019
 */
public class tpLook {

    public static boolean tpLook(final CommandSender player, Command cmd, String cmdLabel, String[] args) {
        if (player instanceof LivingEntity == false) {
            player.sendMessage("Only an instanceof LivingEntity can perform this command.");
        }
        final LivingEntity p = (LivingEntity) player;
        boolean hitsWater = true;
        if (args.length > 0) {
            if (args[0].equalsIgnoreCase("true")) {
                hitsWater = true;
            } else if (args[0].equalsIgnoreCase("false")) {
                hitsWater = false;
            }
        }
        int scanRange = 200;

        RayTraceResult rtr = p.rayTraceBlocks(scanRange, (hitsWater ? FluidCollisionMode.ALWAYS : FluidCollisionMode.NEVER));
        Location endBlock;
        if (rtr == null) {
            p.sendMessage("No block found.");
            return false;
        } else {
            endBlock = rtr.getHitBlock().getLocation().subtract(rtr.getHitPosition());
        }
        return p.teleport(endBlock, PlayerTeleportEvent.TeleportCause.COMMAND);
    }
}
