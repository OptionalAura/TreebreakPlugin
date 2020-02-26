package com.examplemod.chromostoned.objects.items;

import com.examplemod.chromostoned.TestingMod;
import com.examplemod.chromostoned.init.ItemInit;
import com.examplemod.chromostoned.util.interfaces.IHasModel;

import net.minecraft.item.Item;
import net.minecraft.item.ItemFood;

public class OldApple extends ItemFood implements IHasModel
{
	
	public OldApple(String name, int amount, float saturation, boolean isWolfFood)
	{
		super(amount, saturation, isWolfFood);
		setUnlocalizedName(name);
		setRegistryName(name);
		setCreativeTab(TestingMod.EXAMPLETAB);
		
		ItemInit.ITEMS.add(this);
		}
	
	@Override
	public void registerModels()
	{
		TestingMod.proxy.registerItemRenderer(this, 0, "inventory");
	}
}
