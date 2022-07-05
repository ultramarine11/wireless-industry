package ru.wirelesstools.itemblock;

import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.StatCollector;

import java.util.List;

public class ItemBlockLiquidMatterCollector extends ItemBlock {

    public ItemBlockLiquidMatterCollector(Block p_i45328_1_) {
        super(p_i45328_1_);
    }

    public void addInformation(ItemStack itemStack, EntityPlayer player, List info, boolean b) {
        info.add(StatCollector.translateToLocal("info.tooltip.liquidmattercollector.about"));
        info.add(StatCollector.translateToLocal("info.tooltip.liquidmattercollector.savecontents"));
    }
}
