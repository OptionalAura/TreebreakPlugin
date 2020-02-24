package com.examplemod.chromostoned.objects.items;

import com.examplemod.chromostoned.TestingMod;
import com.examplemod.chromostoned.init.ItemInit;
import com.examplemod.chromostoned.util.interfaces.IHasModel;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class OldCoal extends Item implements IHasModel
{
	public OldCoal(String name)
	{
		setUnlocalizedName(name);
		setRegistryName(name);
		setCreativeTab(TestingMod.EXAMPLETAB);
		
		ItemInit.ITEMS.add(this);
		
	}
	
	@Override
	public int getItemBurnTime(ItemStack itemStack)
	{
		return 500;
	}
	
	@Override
	public void registerModels()
	{
		TestingMod.proxy.registerItemRenderer(this, 0, "inventory");
	}
}
