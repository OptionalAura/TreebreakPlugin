package com.examplemod.chromostoned.tabs;

import com.examplemod.chromostoned.init.ItemInit;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;

public class ExampleTab extends CreativeTabs
{

	public ExampleTab(String label) 
	{
		super("exampletab");
		this.setBackgroundImageName("exampletab.png");
	}
	
	@Override
	public ItemStack getTabIconItem()
	{
		return new ItemStack(ItemInit.OLD_IRON_INGOT);
	}

}
