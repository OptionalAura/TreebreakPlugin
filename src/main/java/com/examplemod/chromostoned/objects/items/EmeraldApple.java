package com.examplemod.chromostoned.objects.items;

import com.examplemod.chromostoned.TestingMod;
import com.examplemod.chromostoned.init.ItemInit;
import com.examplemod.chromostoned.util.interfaces.IHasModel;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.MobEffects;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionEffect;
import net.minecraft.world.World;

public class EmeraldApple extends ItemFood implements IHasModel
{
	public EmeraldApple(String name, int amount, float saturation, boolean isWolfFood)
	{
		super(amount, saturation, isWolfFood);
		setUnlocalizedName(name);
		setRegistryName(name);
		setCreativeTab(TestingMod.EXAMPLETAB);
		setAlwaysEdible();
		
		ItemInit.ITEMS.add(this);
	}
	
	@Override
	public boolean hasEffect(ItemStack stack) 
	{
		return true;
	}
	
	@Override
	public ItemStack onItemUseFinish(ItemStack stack, World worldIn, EntityLivingBase entityLiving) 
	{
		entityLiving.addPotionEffect(new PotionEffect(MobEffects.REGENERATION, 3000, 5, false, false));
		entityLiving.addPotionEffect(new PotionEffect(MobEffects.RESISTANCE, 3000, 5, false, false));
		entityLiving.addPotionEffect(new PotionEffect(MobEffects.FIRE_RESISTANCE, 3000, 5, false, false));
		entityLiving.addPotionEffect(new PotionEffect(MobEffects.ABSORPTION, 3000, 10, false, false));
		return super.onItemUseFinish(stack, worldIn, entityLiving);
	}
	
	@Override
	public void registerModels() 
	{
			TestingMod.proxy.registerItemRenderer(this, 0, "inventory");
		
	}
}
