package ru.wirelesstools.item.armor;

import com.mojang.authlib.GameProfile;
import net.minecraft.item.ItemStack;

public interface IPrivateArmor {
	
	void clearOwner(ItemStack stack);
	
	GameProfile getArmorOwner(ItemStack stack);

}
