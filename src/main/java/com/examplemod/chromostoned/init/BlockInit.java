package com.examplemod.chromostoned.init;

import java.util.ArrayList;
import java.util.List;

import com.examplemod.chromostoned.objects.blocks.BlockBase;
import com.examplemod.chromostoned.objects.blocks.CandyCane;
import com.examplemod.chromostoned.objects.blocks.CustomBlockBase;
import com.examplemod.chromostoned.objects.blocks.emerald_chest.EmeraldChest;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.item.Item;

public class BlockInit 
{
	public static final List<Block> BLOCKS = new ArrayList<Block>();
	
	
	//to-do: replace the current texture with the true "old" one
	public static final Block OLD_IRON_BLOCK = new BlockBase("old_iron_block", Material.IRON); 
	public static final Block OLD_COBBLESTONE = new CustomBlockBase("old_cobblestone", Material.ROCK);
	public static final Block CANDY_CANE = new CandyCane("candy_cane", Material.GOURD); 
	public static final Block EMERALD_CHEST = new EmeraldChest("emerald_chest", Material.WOOD);
}
