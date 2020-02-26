package com.examplemod.chromostoned.objects.blocks;

import com.examplemod.chromostoned.TestingMod;
import com.examplemod.chromostoned.init.BlockInit;
import com.examplemod.chromostoned.init.ItemInit;
import com.examplemod.chromostoned.util.interfaces.IHasModel;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;

public class BlockBase extends Block implements IHasModel
{
	public BlockBase(String name, Material material)
	{
		
		super(material);
		setUnlocalizedName(name);
		setRegistryName(name);
		setCreativeTab(CreativeTabs.MATERIALS);
		
		BlockInit.BLOCKS.add(this);
		ItemInit.ITEMS.add(new ItemBlock(this).setRegistryName(this.getRegistryName()));
	}
	
	@Override
	public void registerModels()
	{
		TestingMod.proxy.registerItemRenderer(Item.getItemFromBlock(this), 0, "inventory");
	}
}
