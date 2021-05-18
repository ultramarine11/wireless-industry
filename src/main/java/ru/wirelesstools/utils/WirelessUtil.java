package ru.wirelesstools.utils;

import java.util.Collection;

import cofh.api.energy.IEnergyContainerItem;
import ic2.api.energy.EnergyNet;
import ic2.api.energy.tile.IEnergySink;
import ic2.api.item.ElectricItem;
import ic2.core.block.machine.tileentity.TileEntityElectricMachine;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.util.ForgeDirection;
import ru.wirelesstools.MainWI;
import ru.wirelesstools.config.ConfigWI;
import ru.wirelesstools.tiles.IWirelessCharger;
import ru.wirelesstools.tiles.IWirelessMachineCharger;
import ru.wirelesstools.tiles.TileWirelessMachinesChargerBase;
import ru.wirelesstools.tiles.WirelessQuantumGeneratorBase;

public class WirelessUtil {

	public static void chargeItemEU(EntityPlayer player, IWirelessCharger tile, ItemStack currentItemStackEU) {
		if (ElectricItem.manager.charge(currentItemStackEU, Double.POSITIVE_INFINITY, Integer.MAX_VALUE, true,
				true) > 0) {
			tile.decreaseEnergy(ElectricItem.manager.charge(currentItemStackEU, tile.getCurrentEnergyInCharger(),
					Integer.MAX_VALUE, false, false));
		}
	}

	public static void chargeItemRF(EntityPlayer player, IWirelessCharger tile, ItemStack currentItemStackRF) {
		IEnergyContainerItem item = (IEnergyContainerItem) currentItemStackRF.getItem();
		int amountRfCanBeReceived = item.receiveEnergy(currentItemStackRF, Integer.MAX_VALUE, true);
		if (amountRfCanBeReceived > 0) {
			double chargeOfTileRF = tile.getCurrentEnergyInCharger() * ConfigWI.EuToRfmultiplier;

			double realSentEnergyRF = Math.min(chargeOfTileRF, amountRfCanBeReceived);
			double drainedFromTile = realSentEnergyRF / ConfigWI.EuToRfmultiplier;

			item.receiveEnergy(currentItemStackRF, (int) realSentEnergyRF, false);
			tile.decreaseEnergy(drainedFromTile);
		}
	}

	private static void sendEnergyToEnergySink(IWirelessMachineCharger charger, IEnergySink sink) {
		if (sink.getDemandedEnergy() > 0) {
			double sentreal = Math.min(EnergyNet.instance.getPowerFromTier(sink.getSinkTier()),
					charger.getChargerEnergy());
			sink.injectEnergy(ForgeDirection.UNKNOWN, sentreal, 1);
			charger.decreaseEnergy(sentreal);
		}
	}

	public static boolean iterateIEnergySinkTilesQGenBool(WirelessQuantumGeneratorBase qgen) {
		boolean ret = false;
		if (!qgen.getWorldObj().getChunkFromBlockCoords(qgen.xCoord, qgen.zCoord).chunkTileEntityMap.isEmpty()) {
			for (TileEntity tile : (Collection<TileEntity>) qgen.getWorldObj().getChunkFromBlockCoords(qgen.xCoord,
					qgen.zCoord).chunkTileEntityMap.values()) {
				if (tile != null && tile instanceof IEnergySink && !(tile instanceof TileWirelessMachinesChargerBase)) {
					IEnergySink sink = (IEnergySink) tile;
					if (sink.getDemandedEnergy() > 0) {
						ret = true;
						sink.injectEnergy(ForgeDirection.UNKNOWN,
								EnergyNet.instance.getPowerFromTier(sink.getSinkTier()), 1);
					}

				}

			}

		}

		return ret;
	}

	public static void iterateIEnergySinkTiles(TileWirelessMachinesChargerBase charger) {
		if (!charger.getWorldObj().getChunkFromBlockCoords(charger.xCoord, charger.zCoord).chunkTileEntityMap
				.isEmpty()) {
			for (TileEntity tile : (Collection<TileEntity>) charger.getWorldObj()
					.getChunkFromBlockCoords(charger.xCoord, charger.zCoord).chunkTileEntityMap.values()) {
				if (tile != null && tile instanceof IEnergySink && !(tile instanceof TileWirelessMachinesChargerBase)) {
					IEnergySink tileenergysink = (IEnergySink) tile;
					WirelessUtil.sendEnergyToEnergySink(charger, tileenergysink);
				}

			}

		}

	}

}
