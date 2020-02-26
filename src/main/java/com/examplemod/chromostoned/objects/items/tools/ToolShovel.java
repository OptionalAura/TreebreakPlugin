package com.examplemod.chromostoned.objects.items.tools;

import com.examplemod.chromostoned.TestingMod;
import com.examplemod.chromostoned.init.ItemInit;
import com.examplemod.chromostoned.util.interfaces.IHasModel;

import net.minecraft.item.ItemSpade;
import net.minecraft.item.ItemSword;

public class ToolShovel extends ItemSpade implements IHasModel 
{

	public ToolShovel(String name, ToolMaterial material) {
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
