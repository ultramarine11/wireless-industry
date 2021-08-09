package ru.wirelesstools.utils;

import cofh.api.energy.IEnergyContainerItem;
import cofh.api.energy.IEnergyReceiver;
import ic2.api.energy.EnergyNet;
import ic2.api.energy.tile.IEnergySink;
import ic2.api.item.ElectricItem;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.util.ForgeDirection;
import ru.wirelesstools.config.ConfigWI;
import ru.wirelesstools.tiles.IWirelessCharger;
import ru.wirelesstools.tiles.IWirelessMachineCharger;
import ru.wirelesstools.tiles.TileWirelessMachinesChargerBase;
import ru.wirelesstools.tiles.WirelessQuantumGeneratorBase;

import java.util.ArrayList;

public class WirelessUtil {

    public static void chargeItemEU(IWirelessCharger tile, ItemStack currentItemStackEU) {
        if(ElectricItem.manager.charge(currentItemStackEU, Double.POSITIVE_INFINITY, Integer.MAX_VALUE, true,
                true) > 0) {
            tile.decreaseEnergy(ElectricItem.manager.charge(currentItemStackEU, tile.getCurrentEnergyInCharger(),
                    Integer.MAX_VALUE, false, false));
        }
    }

    public static void chargeItemRF(IWirelessCharger tile, ItemStack currentItemStackRF) {
        IEnergyContainerItem item = (IEnergyContainerItem) currentItemStackRF.getItem();
        if(item.receiveEnergy(currentItemStackRF, Integer.MAX_VALUE, true) > 0) {
            int euFinallySent = item.receiveEnergy(currentItemStackRF,
                    (int) (tile.getCurrentEnergyInCharger() * ConfigWI.EUToRF_Multiplier),
                    false) / ConfigWI.EUToRF_Multiplier;
            tile.decreaseEnergy(euFinallySent);
        }
    }

    private static void sendEnergyToRFReceiver(IWirelessMachineCharger charger, IEnergyReceiver receiver) {
        int demandedenergy = receiver.getMaxEnergyStored(ForgeDirection.UNKNOWN) - receiver.getEnergyStored(ForgeDirection.UNKNOWN);
        if(demandedenergy > 0) {
            double energytosend = charger.getChargerEnergy() * ConfigWI.EUToRF_Multiplier;
            int euFinallySent = receiver.receiveEnergy(ForgeDirection.UNKNOWN, (int) (charger.getChargerEnergy() * ConfigWI.EUToRF_Multiplier),
                    false) / ConfigWI.EUToRF_Multiplier;
            charger.decreaseEnergy(euFinallySent);
        }
    }

    private static void sendEnergyToEnergySink(IWirelessMachineCharger charger, IEnergySink sink) {
        if(sink.getDemandedEnergy() > 0) {
            double sentreal = Math.min(EnergyNet.instance.getPowerFromTier(sink.getSinkTier()),
                    charger.getChargerEnergy());
            sink.injectEnergy(ForgeDirection.UNKNOWN, sentreal, 1);
            charger.decreaseEnergy(sentreal);
        }
    }

    public static boolean iterateIEnergySinkTilesQGenBool(WirelessQuantumGeneratorBase qgen) {
        boolean ret = false;
        if(!qgen.getWorldObj().getChunkFromBlockCoords(qgen.xCoord, qgen.zCoord).chunkTileEntityMap.isEmpty()) {
            for(TileEntity tile : new ArrayList<TileEntity>(qgen.getWorldObj().getChunkFromBlockCoords(qgen.xCoord,
                    qgen.zCoord).chunkTileEntityMap.values())) {
                if(tile instanceof IEnergySink && !(tile instanceof TileWirelessMachinesChargerBase)) {
                    IEnergySink sink = (IEnergySink) tile;
                    if(sink.getDemandedEnergy() > 0) {
                        ret = true;
                        sink.injectEnergy(ForgeDirection.UNKNOWN,
                                EnergyNet.instance.getPowerFromTier(sink.getSinkTier()), 1);
                    }
                } else if(tile instanceof IEnergyReceiver && !(tile instanceof IEnergySink)) {
                    IEnergyReceiver receiver = (IEnergyReceiver) tile;
                    int demandedenergy = receiver.getMaxEnergyStored(ForgeDirection.UNKNOWN) - receiver.getEnergyStored(ForgeDirection.UNKNOWN);
                    if(demandedenergy > 0) {
                        ret = true;
                        receiver.receiveEnergy(ForgeDirection.UNKNOWN, Integer.MAX_VALUE, false);
                    }
                }
            }
        }
        return ret;
    }

    public static void iterateIEnergySinkTiles(TileWirelessMachinesChargerBase charger) {
        if(!charger.getWorldObj().getChunkFromBlockCoords(charger.xCoord, charger.zCoord).chunkTileEntityMap
                .isEmpty()) {
            for(TileEntity tile : new ArrayList<TileEntity>(charger.getWorldObj()
                    .getChunkFromBlockCoords(charger.xCoord, charger.zCoord).chunkTileEntityMap.values())) {
                if(tile instanceof IEnergySink && !(tile instanceof TileWirelessMachinesChargerBase)) {
                    IEnergySink sink = (IEnergySink) tile;
                    WirelessUtil.sendEnergyToEnergySink(charger, sink);
                } else if(tile instanceof IEnergyReceiver && !(tile instanceof IEnergySink) && !(tile instanceof TileWirelessMachinesChargerBase)) {
                    IEnergyReceiver receiver = (IEnergyReceiver) tile;
                    WirelessUtil.sendEnergyToRFReceiver(charger, receiver);
                }
            }
        }
    }

}
