package com.examplemod.chromostoned;

import com.examplemod.chromostoned.proxy.CommonProxy;
import com.examplemod.chromostoned.recipes.CraftingRecipes;
import com.examplemod.chromostoned.recipes.SmeltingRecipes;
import com.examplemod.chromostoned.tabs.ExampleTab;
import com.examplemod.chromostoned.util.Reference;
import com.examplemod.chromostoned.world.gen.WorldGenOres;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.Mod.Instance;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;

@Mod(modid = Reference.MOD_ID, version = Reference.VERSION, name = Reference.NAME)
public class TestingMod 
{
	@Instance
	public static TestingMod instance;
	
	@SidedProxy(clientSide = Reference.CLIENT_PROXY_CLASS, serverSide = Reference.COMMON_PROXY_CLASS)
	public static CommonProxy proxy;
	
	public static final CreativeTabs EXAMPLETAB = new ExampleTab("exampletab");
	
	@EventHandler
	public void preInit(FMLPreInitializationEvent event)
	{
		GameRegistry.registerWorldGenerator(new WorldGenOres(), 4);
	}
	@EventHandler
	public void init(FMLInitializationEvent event)
	{
		SmeltingRecipes.init();
		CraftingRecipes.init();
	}
	@EventHandler
	public void postInit(FMLPostInitializationEvent event)
	{
		
	}
	
	@EventHandler
	public void serverInit(FMLServerStartingEvent event)
	{
		
	}
	
}
