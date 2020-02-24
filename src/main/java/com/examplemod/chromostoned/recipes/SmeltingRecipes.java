package com.examplemod.chromostoned.recipes;

import com.examplemod.chromostoned.init.BlockInit;
import com.examplemod.chromostoned.init.ItemInit;

import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class SmeltingRecipes
{
	public static void init()
	{
		GameRegistry.addSmelting(new ItemStack(Blocks.IRON_BLOCK), new ItemStack(ItemInit.OLD_IRON_INGOT), 0.4F);
	}
}
