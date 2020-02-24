package com.examplemod.chromostoned.init;

import java.util.ArrayList;
import java.util.List;

import com.examplemod.chromostoned.objects.items.EmeraldApple;
import com.examplemod.chromostoned.objects.items.ItemBase;
import com.examplemod.chromostoned.objects.items.OldApple;
import com.examplemod.chromostoned.objects.items.OldCoal;
import com.examplemod.chromostoned.objects.items.armor.ArmorBase;
import com.examplemod.chromostoned.objects.items.tools.ToolAxe;
import com.examplemod.chromostoned.objects.items.tools.ToolHoe;
import com.examplemod.chromostoned.objects.items.tools.ToolPickaxe;
import com.examplemod.chromostoned.objects.items.tools.ToolShovel;
import com.examplemod.chromostoned.objects.items.tools.ToolSword;
import com.examplemod.chromostoned.objects.staffs.LargeFireballStaff;
import com.examplemod.chromostoned.util.Reference;

import net.minecraft.init.SoundEvents;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.Item;
import net.minecraft.item.Item.ToolMaterial;
import net.minecraft.item.ItemArmor.ArmorMaterial;
import net.minecraftforge.common.util.EnumHelper;

public class ItemInit 
{
	public static final List<Item> ITEMS = new ArrayList<Item>();
	
	//Materials
	public static final ArmorMaterial ARMOR_EMERALD = EnumHelper.addArmorMaterial("armor_emerald", Reference.MOD_ID + ":emerald", 1500, new int[] {4, 7, 9, 5}, 17, SoundEvents.ITEM_ARMOR_EQUIP_GENERIC, 0.0F);
	public static final ToolMaterial TOOL_EMERALD = EnumHelper.addToolMaterial("tool_emerald", 4, 1500, 5.0f, 3.5f, 19);
	
	//Items
	public static final Item OLD_IRON_INGOT = new ItemBase("old_iron_ingot");
	public static final Item OLD_COAL = new OldCoal("old_coal");
	public static final Item OLD_APPLE = new OldApple("old_apple", 3, 1.0F, false);
	public static final Item EMERALD_APPLE = new EmeraldApple("emerald_apple", 5, 1.2F, false);
	
	
	//Armor
	public static final Item HELMET_EMERALD = new ArmorBase("helmet_emerald", ARMOR_EMERALD, 1, EntityEquipmentSlot.HEAD);
	public static final Item CHESTPLATE_EMERALD = new ArmorBase("chestplate_emerald", ARMOR_EMERALD, 1, EntityEquipmentSlot.CHEST);
	public static final Item LEGGINGS_EMERALD = new ArmorBase("leggings_emerald", ARMOR_EMERALD, 2, EntityEquipmentSlot.LEGS);
	public static final Item BOOTS_EMERALD = new ArmorBase("boots_emerald", ARMOR_EMERALD, 1, EntityEquipmentSlot.FEET);
	
	//Tools
	public static final Item SWORD_EMERALD = new ToolSword("emerald_sword", TOOL_EMERALD);
	public static final Item PICKAXE_EMERALD = new ToolPickaxe("emerald_pickaxe", TOOL_EMERALD);
	public static final Item SHOVEL_RUBY = new ToolShovel("emerald_shovel", TOOL_EMERALD);
	public static final Item AXE_RUBY = new ToolAxe("emerald_axe", TOOL_EMERALD);
	public static final Item HOE_RUBY = new ToolHoe("emerald_hoe", TOOL_EMERALD);
	
	//Staffs
	public static final Item LARGE_FIREBALL_STAFF = new LargeFireballStaff("large_fireball_staff");
	
}
