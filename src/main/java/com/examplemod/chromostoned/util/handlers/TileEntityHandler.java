package com.examplemod.chromostoned.util.handlers;

import com.examplemod.chromostoned.objects.blocks.emerald_chest.TileEntityEmeraldChest;
import com.examplemod.chromostoned.util.Reference;

import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class TileEntityHandler 
{
	
	public static void registerTileEntities()
	{
		GameRegistry.registerTileEntity(TileEntityEmeraldChest.class, new ResourceLocation(Reference.MOD_ID + ":emerald_chest"));
	}

}
