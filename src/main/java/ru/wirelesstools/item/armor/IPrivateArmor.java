package ru.wirelesstools.item.armor;

import com.mojang.authlib.GameProfile;
import ic2.core.util.StackUtil;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTUtil;

public interface IPrivateArmor {
	
	default void clearOwner(ItemStack stack) {
		NBTTagCompound nbt = StackUtil.getOrCreateNbtData(stack);
		nbt.removeTag("ownerGameProfile");
	}
	
	default GameProfile getArmorOwner(ItemStack stack) {
		NBTTagCompound nbt = StackUtil.getOrCreateNbtData(stack);
		return NBTUtil.func_152459_a(nbt.getCompoundTag("ownerGameProfile"));
	}

}
