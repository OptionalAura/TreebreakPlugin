package com.examplemod.chromostoned.objects.items.tools;

import com.examplemod.chromostoned.TestingMod;
import com.examplemod.chromostoned.init.ItemInit;
import com.examplemod.chromostoned.util.interfaces.IHasModel;

import net.minecraft.item.ItemHoe;
import net.minecraft.item.ItemSword;

public class ToolHoe extends ItemHoe implements IHasModel 
{

	public ToolHoe(String name, ToolMaterial material) {
		super(material);
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
