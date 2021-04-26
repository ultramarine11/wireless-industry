package ru.wirelesstools.item.armor;

import com.mojang.authlib.GameProfile;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public interface IPrivateArmor {
	
	public void clearOwner(ItemStack stack);
	
	public GameProfile getArmorOwner(ItemStack stack);

}
