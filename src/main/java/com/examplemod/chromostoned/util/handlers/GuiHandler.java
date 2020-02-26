package com.examplemod.chromostoned.util.handlers;

import com.examplemod.chromostoned.objects.blocks.emerald_chest.ContainerEmeraldChest;
import com.examplemod.chromostoned.objects.blocks.emerald_chest.GuiEmeraldChest;
import com.examplemod.chromostoned.objects.blocks.emerald_chest.TileEntityEmeraldChest;
import com.examplemod.chromostoned.util.Reference;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.IGuiHandler;

public class GuiHandler implements IGuiHandler
{
	
	@Override
	public Object getServerGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) 
	{
		if(ID == Reference.GUI_EMERALD_CHEST) 
		{
			return new ContainerEmeraldChest(player.inventory, (TileEntityEmeraldChest)world.getTileEntity(new BlockPos(x, y, z)), player);
		}
		return null;
	}
	
	@Override
	public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) 
	{
		if(ID == Reference.GUI_EMERALD_CHEST) 
		{
			return new GuiEmeraldChest(player.inventory, (TileEntityEmeraldChest)world.getTileEntity(new BlockPos(x, y, z)), player);
		}
		return null;
	}
	
	public static void registerGuis()
	{
		
	}
	
}
