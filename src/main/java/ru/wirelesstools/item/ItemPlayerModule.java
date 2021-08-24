package ru.wirelesstools.item;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import ic2.core.util.StackUtil;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTUtil;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.StatCollector;
import ru.wirelesstools.MainWI;
import ru.wirelesstools.Reference;
import ru.wirelesstools.utils.MiscUtils;

import java.util.List;

public class ItemPlayerModule extends Item {

	public ItemPlayerModule() {
		this.setCreativeTab(MainWI.tabwi);
		this.setUnlocalizedName("playermodule");
		this.setTextureName(Reference.PathTex + "module_player");
		this.setMaxStackSize(1);
	}

	@SideOnly(value = Side.CLIENT)
	public void addInformation(ItemStack stack, EntityPlayer player, List info, boolean b) {
		NBTTagCompound nbt = StackUtil.getOrCreateNbtData(stack);
		if (NBTUtil.func_152459_a(nbt.getCompoundTag("playerModulegameprofile")) == null) {
			info.add(EnumChatFormatting.RED + StatCollector.translateToLocal("info.playermodule.empty"));
		} else if (NBTUtil.func_152459_a(nbt.getCompoundTag("playerModulegameprofile")) != null) {
			info.add(EnumChatFormatting.GREEN + StatCollector.translateToLocal("info.playermodule.owner.is") + ": "
					+ NBTUtil.func_152459_a(nbt.getCompoundTag("playerModulegameprofile")).getName());
		}
		info.add(StatCollector.translateToLocal("info.playermodule.howto.use"));
		info.add(EnumChatFormatting.ITALIC + StatCollector.translateToLocal("info.hint.wip"));
	}

	@Override
	public boolean itemInteractionForEntity(ItemStack stack, EntityPlayer player, EntityLivingBase entityLivingTarget) {
		if (entityLivingTarget.worldObj.isRemote) {
			return false;
		}
		NBTTagCompound nbt = StackUtil.getOrCreateNbtData(stack);
		if (player.isSneaking()) {
			if (entityLivingTarget instanceof EntityPlayer) {
				EntityPlayer playerTarget = (EntityPlayer) entityLivingTarget;
				NBTTagCompound ownerNbt = new NBTTagCompound();
				NBTUtil.func_152460_a(ownerNbt, playerTarget.getGameProfile());
				nbt.setTag("playerModulegameprofile", ownerNbt);
				MiscUtils.sendChatMessageColoredMulti(player, "chat.message.module.player.set", EnumChatFormatting.DARK_GREEN,
						new ChatComponentText(": " + playerTarget.getGameProfile().getName()));
				return true;
			} else {
				MiscUtils.sendColoredMessageToPlayer(player, "chat.message.module.only.player", EnumChatFormatting.LIGHT_PURPLE);
			}
		} else {
			MiscUtils.sendColoredMessageToPlayer(player, "chat.message.module.sneak", EnumChatFormatting.RED);
		}
		return false;
	}

}
