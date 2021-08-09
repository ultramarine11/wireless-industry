package ru.wirelesstools.itemblock;

import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.StatCollector;

import java.util.List;

public class ItemBlockWQGen extends ItemBlock {

	public ItemBlockWQGen(Block p_i45328_1_) {
		super(p_i45328_1_);
		
	}

	public void addInformation(ItemStack itemStack, EntityPlayer player, List info, boolean b) {

		info.add(StatCollector.translateToLocal("info.tooltip.machinescharger.about"));
	}
	
	@Override
	public EnumRarity getRarity(ItemStack p_77613_1_) {
		
		return EnumRarity.epic;
	}

}
