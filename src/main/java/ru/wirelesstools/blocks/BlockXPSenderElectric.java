package ru.wirelesstools.blocks;

import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;
import ru.wirelesstools.MainWI;
import ru.wirelesstools.Reference;
import ru.wirelesstools.tiles.TileXPSender;

public class BlockXPSenderElectric extends BlockContainer {

	private IIcon[] icons;

	public BlockXPSenderElectric(String name) {
		super(Material.rock);
		this.setBlockName(name);
		this.setCreativeTab(MainWI.tabwi);
		this.setHardness(3.0F);
		this.setResistance(5.0F);

	}

	public IIcon getIcon(int side, int metadata) {
		// 0=bottom, 1=top, 2,3,4,5 = sides
		switch (side) {

		case 0:
			return this.icons[0];
		case 1:
			return this.icons[0];
		case 2:
			return this.icons[2];
		case 3:
			return this.icons[2];
		case 4:
			return this.icons[2];
		case 5:
			return this.icons[2];

		}

		return this.icons[1];
	}

	public void registerBlockIcons(IIconRegister reg) {
		this.icons = new IIcon[3];
		this.icons[0] = reg.registerIcon(Reference.PathTex + "blockXPMachine"); // bottom
		this.icons[1] = reg.registerIcon(Reference.PathTex + "blockXPMachine"); // top
		this.icons[2] = reg.registerIcon(Reference.PathTex + "xpsender_side_all"); // sides
	}

	@Override
	public TileEntity createNewTileEntity(World p_149915_1_, int p_149915_2_) {

		return new TileXPSender();
	}

	public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int side, float subX,
			float subY, float subZ) {
		if (!world.isRemote) {
			if(!player.isSneaking()) {
				
				player.openGui(MainWI.instance, 1, world, x, y, z);
			}
		}
		return true;
	}

}
