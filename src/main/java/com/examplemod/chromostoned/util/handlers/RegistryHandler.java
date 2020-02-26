package com.examplemod.chromostoned.util.handlers;

import com.examplemod.chromostoned.TestingMod;
import com.examplemod.chromostoned.init.BlockInit;
import com.examplemod.chromostoned.init.ItemInit;
import com.examplemod.chromostoned.objects.blocks.emerald_chest.RenderEmeraldChest;
import com.examplemod.chromostoned.objects.blocks.emerald_chest.TileEntityEmeraldChest;
import com.examplemod.chromostoned.recipes.CraftingRecipes;
import com.examplemod.chromostoned.recipes.SmeltingRecipes;
import com.examplemod.chromostoned.util.interfaces.IHasModel;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;

@EventBusSubscriber
public class RegistryHandler
{
    @SubscribeEvent
    public static void onItemRegister(RegistryEvent.Register<Item> event)
    {
        event.getRegistry().registerAll(ItemInit.ITEMS.toArray(new Item[0]));
    }
   
    @SubscribeEvent
    public static void onModelRegister(ModelRegistryEvent event)
    {
    	TestingMod.proxy.registerItemRenderer(Item.getItemFromBlock(BlockInit.EMERALD_CHEST), 0, "inventory");
        for(Item item : ItemInit.ITEMS)
        {
            if(item instanceof IHasModel)
            {
                ((IHasModel)item).registerModels();
            }
        }
      //For Loop for Blocks in RegistryHandler.java
        for(Block block : BlockInit.BLOCKS)
                {
                    if(block instanceof IHasModel)
                    {
                        ((IHasModel)block).registerModels();
                    }
                }
    }
  //onBlockRegister method in RegistryHandler.java
    @SubscribeEvent
        public static void onBlockRegister(RegistryEvent.Register<Block> event)
        {
            event.getRegistry().registerAll(BlockInit.BLOCKS.toArray(new Block[0]));
            TileEntityHandler.registerTileEntities();
            ClientRegistry.bindTileEntitySpecialRenderer(TileEntityEmeraldChest.class, new RenderEmeraldChest());
        }
    
   
    public static void preInitRegistries()
    {
       
    }
   
    public static void initRegistries()
    {
    	NetworkRegistry.INSTANCE.registerGuiHandler(TestingMod.instance, new GuiHandler());
        SmeltingRecipes.init();
        CraftingRecipes.init();
    }
   
    public static void postInitRegistries()
    {
       
    }
   
    public static void serverRegistries(FMLServerStartingEvent event)
    {
       
    }
}
