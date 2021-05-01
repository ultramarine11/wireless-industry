package ru.wirelesstools;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;

public class CreativeTabWI extends CreativeTabs {

	public CreativeTabWI() {
		super("WirelessSP");

	}

	@Override
	public Item getTabIconItem() {

		return Item.getItemFromBlock(MainWI.wirelessspsppersonal);
	}

}
