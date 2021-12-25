package ru.wirelesstools.item.armor;

import com.mojang.authlib.GameProfile;
import ic2.core.util.StackUtil;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTUtil;

public interface IPrivateArmor {
	
	default void clearOwner(ItemStack stack) {
		StackUtil.getOrCreateNbtData(stack).removeTag("ownerGameProfile");
	}
	
	default GameProfile getArmorOwner(ItemStack stack) {
		return NBTUtil.func_152459_a(StackUtil.getOrCreateNbtData(stack).getCompoundTag("ownerGameProfile"));
	}

	default GameProfile setArmorOwner(ItemStack stack, EntityPlayer newOwnerPlayer) {
		GameProfile ownerProfile = newOwnerPlayer.getGameProfile();
		NBTTagCompound ownerNbt = new NBTTagCompound();
		NBTUtil.func_152460_a(ownerNbt, ownerProfile);
		StackUtil.getOrCreateNbtData(stack).setTag("ownerGameProfile", ownerNbt);
		return ownerProfile;
	}

}
