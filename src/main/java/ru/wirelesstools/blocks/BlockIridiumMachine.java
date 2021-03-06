package ru.wirelesstools.blocks;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import ru.wirelesstools.MainWI;
import ru.wirelesstools.Reference;

public class BlockIridiumMachine extends Block {

	public BlockIridiumMachine() {
		super(Material.rock);
		this.setBlockName("blockIridMach");
		this.setCreativeTab(MainWI.tabwi);
		setHardness(2.0F);
		setResistance(5.0F);
		setBlockTextureName(Reference.PathTex + "blockIridiumMachine");
	}
}
