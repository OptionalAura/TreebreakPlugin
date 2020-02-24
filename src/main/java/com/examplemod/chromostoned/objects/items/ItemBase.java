package com.examplemod.chromostoned.objects.items;

import com.examplemod.chromostoned.TestingMod;
import com.examplemod.chromostoned.init.ItemInit;
import com.examplemod.chromostoned.util.interfaces.IHasModel;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;

public class ItemBase extends Item implements IHasModel 
{
	public ItemBase(String name)
	{
		setUnlocalizedName(name);
		setRegistryName(name);
		setCreativeTab(CreativeTabs.MATERIALS);
		
		ItemInit.ITEMS.add(this);
		
	}
	
	@Override
	public void registerModels()
	{
		TestingMod.proxy.registerItemRenderer(this, 0, "inventory");
	}
}
