package com.examplemod.chromostoned.recipes;

import com.examplemod.chromostoned.init.BlockInit;
import com.examplemod.chromostoned.init.ItemInit;

import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class CraftingRecipes {
	
	public static void init()
	{
		//Silver Block
			ItemStack old_iron_ingot = new ItemStack(ItemInit.OLD_IRON_INGOT, 9);
			ItemStack old_iron_block = new ItemStack(BlockInit.OLD_IRON_BLOCK);
			GameRegistry.addShapelessRecipe(new ResourceLocation("silver_ingot"), null, old_iron_ingot, Ingredient.fromStacks(old_iron_block));
	}

}
