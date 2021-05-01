package ru.wirelesstools.item.tool;

import java.util.List;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import ic2.api.energy.tile.IEnergySink;
import ic2.core.block.machine.tileentity.TileEntityElectricMachine;
import ic2.core.util.StackUtil;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ChatComponentTranslation;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;
import ru.wirelesstools.tiles.TileWirelessMachinesChargerSelective;

public class WirelessConnector extends Item {

	public WirelessConnector() {
		this.setMaxStackSize(1);

	}

	@SideOnly(value = Side.CLIENT)
	public void addInformation(ItemStack stack, EntityPlayer player, List list, boolean par4) {
		NBTTagCompound nbt = StackUtil.getOrCreateNbtData(stack);
		int xc = nbt.getInteger("xcoord");
		int yc = nbt.getInteger("ycoord");
		int zc = nbt.getInteger("zcoord");
		if (xc != 0 && yc != 0 && zc != 0) {
			list.add(StatCollector.translateToLocal("info.connector.te.coords") + ": " + xc + ", " + yc + ", " + zc);
		} else {
			list.add(StatCollector.translateToLocal("info.connector.invalid.coords"));
		}
	}

	public ItemStack onItemRightClick(ItemStack stack, World world, EntityPlayer player) {
		if (!world.isRemote) {
			NBTTagCompound nbt = StackUtil.getOrCreateNbtData(stack);

		}
		return stack;
	}

	public boolean onItemUseFirst(ItemStack stack, EntityPlayer player, World world, int x, int y, int z, int side,
			float hitX, float hitY, float hitZ) {
		if (!world.isRemote) {
			NBTTagCompound nbt = StackUtil.getOrCreateNbtData(stack);
			TileEntity te = world.getTileEntity(x, y, z);
			/*
			 * if (te != null && te instanceof TileEntityElectricMachine) {
			 * TileEntityElectricMachine tileelectric = (TileEntityElectricMachine) te;
			 * int[] arraycoord = { tileelectric.xCoord, tileelectric.yCoord,
			 * tileelectric.zCoord }; nbt.setIntArray("coordarray", arraycoord);
			 * player.addChatMessage(new
			 * ChatComponentTranslation(EnumChatFormatting.DARK_GRAY +
			 * StatCollector.translateToLocal("chat.message.connector.te.binded"), new
			 * Object[0])); return true; }
			 */
			if (te != null && te instanceof TileWirelessMachinesChargerSelective) {
				TileWirelessMachinesChargerSelective charger = (TileWirelessMachinesChargerSelective) te;
				int[] arraycoordlocal = nbt.getIntArray("coordarray");
				if (arraycoordlocal.length == 3) {
					if (this.checkIsPresentTE(charger, arraycoordlocal[0], arraycoordlocal[1], arraycoordlocal[2])) {
						player.addChatMessage(new ChatComponentTranslation(
								StatCollector.translateToLocal("chat.message.connector.te.already.exists"),
								new Object[0]));
						return true;
					}

					if (charger.setConnectorArrayToThis(arraycoordlocal)) {
						player.addChatMessage(new ChatComponentTranslation(
								StatCollector.translateToLocal("chat.message.connected.successfully"), new Object[0]));
					} else {
						player.addChatMessage(new ChatComponentTranslation(
								StatCollector.translateToLocal("chat.message.connector.no.free.slot"), new Object[0]));
					}
					return true;
				}
			}

			if (te != null && te instanceof TileEntityElectricMachine) {
				TileEntityElectricMachine machine = (TileEntityElectricMachine) te;
				nbt.setInteger("xcoord", machine.xCoord);
				nbt.setInteger("ycoord", machine.yCoord);
				nbt.setInteger("zcoord", machine.zCoord);
				player.addChatMessage(new ChatComponentTranslation(EnumChatFormatting.DARK_GRAY
						+ StatCollector.translateToLocal("chat.message.connector.te.binded"), new Object[0]));
				return true;
			}

			if (te != null && te instanceof TileWirelessMachinesChargerSelective) {
				TileWirelessMachinesChargerSelective charger = (TileWirelessMachinesChargerSelective) te;
				if (charger.tryAddMachineToList(world, nbt.getInteger("xcoord"), nbt.getInteger("ycoord"),
						nbt.getInteger("zcoord"))) {
					player.addChatMessage(new ChatComponentTranslation(
							StatCollector.translateToLocal("chat.message.connected.successfully"), new Object[0]));
				}

			}
		}

		return false;
	}

	private int[] findNextFreeArrayCoord(TileWirelessMachinesChargerSelective te) {
		for (int i = 0; i < 8; i++) {
			int[] arraylocal = te.getCoordArrays(i + 1);
			if (arraylocal.length == 3) {
				if (arraylocal[0] != 0 && arraylocal[1] != 0 && arraylocal[2] != 0)
					continue;
				return arraylocal;
			}
		}
		return new int[0];
	}
	
	private boolean checkIsPresentTEInCharger(TileWirelessMachinesChargerSelective te, int x, int y, int z) {
		
		
		return false;
	}

	private boolean checkIsPresentTE(TileWirelessMachinesChargerSelective te, int x, int y, int z) {
		for (int i = 0; i < 8; i++) {
			int[] arraylocal = te.getCoordArrays(i + 1);
			if (arraylocal.length == 3) {
				if (arraylocal[0] != x || arraylocal[1] != y || arraylocal[2] != z)
					continue;
				return true;
			}
		}
		return false;
	}

}
