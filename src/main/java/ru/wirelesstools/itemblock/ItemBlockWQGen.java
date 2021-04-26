package ru.wirelesstools.itemblock;

import net.minecraft.block.Block;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;

public class ItemBlockWQGen extends ItemBlock {

	public ItemBlockWQGen(Block p_i45328_1_) {
		super(p_i45328_1_);
		
	}
	
	@Override
	public EnumRarity getRarity(ItemStack p_77613_1_) {
		
		return EnumRarity.epic;
	}

}
