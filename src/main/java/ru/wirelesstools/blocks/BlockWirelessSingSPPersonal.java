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
import ru.wirelesstools.tiles.TileWirelessSingSPPersonal;

public class BlockWirelessSingSPPersonal extends BlockContainer {

	private IIcon[][] icons;

	public BlockWirelessSingSPPersonal(String unlocalizedName, Material mat) {
		super(mat);
		this.setBlockName(unlocalizedName);
		this.setCreativeTab(MainWI.tabwi);
		setHardness(3.0F);
	}

	public void registerBlockIcons(IIconRegister reg) {
		this.icons = new IIcon[1][3];
		this.icons[0][0] = reg.registerIcon(Reference.PathTex + "all_bottom");
		this.icons[0][1] = reg.registerIcon(Reference.PathTex + "ssp_top");
		this.icons[0][2] = reg.registerIcon(Reference.PathTex + "wireless_singularsp_side");

	}

	public IIcon getIcon(int side, int metadata) {
		switch (side) {
		case 0:
			return this.icons[0][0];
		case 1:
			return this.icons[0][1];
		case 2:
			return this.icons[0][2];
		case 3:
			return this.icons[0][2];
		case 4:
			return this.icons[0][2];
		case 5:
			return this.icons[0][2];
		}

		return this.icons[1][1];
	}

	@Override
	public TileEntity createNewTileEntity(World p_149915_1_, int p_149915_2_) {

		return new TileWirelessSingSPPersonal();
	}
	
	public void onBlockPlacedBy(World world, int x, int y, int z, EntityLivingBase entity, ItemStack itemStack) {
		TileWirelessSingSPPersonal tile = (TileWirelessSingSPPersonal)world.getTileEntity(x, y, z);
		if (entity instanceof EntityPlayer) {
			EntityPlayer player = (EntityPlayer) entity;
			tile.setPlayerProfile(player.getGameProfile());
		}
	}

	public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int side, float subX,
			float subY, float subZ) {
		if (!world.isRemote) {

			TileWirelessSingSPPersonal te = (TileWirelessSingSPPersonal) world.getTileEntity(x, y, z);

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
