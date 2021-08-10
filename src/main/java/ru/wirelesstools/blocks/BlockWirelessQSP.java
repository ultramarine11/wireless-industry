package ru.wirelesstools.blocks;

import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ChatComponentTranslation;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;
import ru.wirelesstools.MainWI;
import ru.wirelesstools.Reference;
import ru.wirelesstools.tiles.TileWirelessQSP;

public class BlockWirelessQSP extends BlockContainer {

	private IIcon[] icons;

	public BlockWirelessQSP(String unlocalizedName) {
		super(Material.rock);
		this.setBlockName(unlocalizedName);
		this.setCreativeTab(MainWI.tabwi);
		setHardness(3.0F);
	}

	public void registerBlockIcons(IIconRegister reg) {

		this.icons = new IIcon[3];
		this.icons[0] = reg.registerIcon(Reference.PathTex + "all_bottom");
		this.icons[1] = reg.registerIcon(Reference.PathTex + "qsp_top");
		this.icons[2] = reg.registerIcon(Reference.PathTex + "qsp_side");
	}

	public IIcon getIcon(int side, int metadata) {
		switch (side) {

		case 0:
			return this.icons[0];
		case 1:
			return this.icons[1];
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

	@Override
	public TileEntity createNewTileEntity(World p_149915_1_, int p_149915_2_) {

		return new TileWirelessQSP();
	}
	
	public void onBlockPlacedBy(World world, int x, int y, int z, EntityLivingBase entity, ItemStack itemStack) {
		TileWirelessQSP tile = (TileWirelessQSP)world.getTileEntity(x, y, z);
		if (entity instanceof EntityPlayer) {
			EntityPlayer player = (EntityPlayer) entity;
			tile.setPlayerProfile(player.getGameProfile());
		}
	}

	public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int side, float subX,
			float subY, float subZ) {
		if (!world.isRemote) {
			TileWirelessQSP te = (TileWirelessQSP) world.getTileEntity(x, y, z);
			boolean access = te.permitsAccess(player.getGameProfile());
			if (access) {

				player.openGui(MainWI.instance, 1, world, x, y, z);
			}
			else if(player.capabilities.isCreativeMode) {

				player.openGui(MainWI.instance, 1, world, x, y, z);
			}
			else {

				player.addChatMessage(new ChatComponentTranslation("access.solarpanel.notallowed"));
			}

		}

		return true;

	}

}
