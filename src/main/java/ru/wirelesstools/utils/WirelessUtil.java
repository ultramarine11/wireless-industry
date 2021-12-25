package ru.wirelesstools.utils;

import cofh.api.energy.IEnergyContainerItem;
import cofh.api.energy.IEnergyReceiver;
import ic2.api.energy.tile.IEnergySink;
import ic2.api.item.ElectricItem;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.ChunkPosition;
import net.minecraftforge.common.util.ForgeDirection;
import ru.wirelesstools.config.ConfigWI;
import ru.wirelesstools.tiles.IWirelessCharger;
import ru.wirelesstools.tiles.IWirelessMachineCharger;
import ru.wirelesstools.tiles.TileWirelessMachinesChargerBase;
import ru.wirelesstools.tiles.WirelessQuantumGeneratorBase;

import java.util.Map;

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

    private static void sendRFToReceiver(IWirelessMachineCharger charger, IEnergyReceiver receiver) {
        if((receiver.getMaxEnergyStored(ForgeDirection.UNKNOWN) - receiver.getEnergyStored(ForgeDirection.UNKNOWN)) > 0
                && charger.getChargerEnergyRF() > 0) {
            // int sentRF = receiver.receiveEnergy(ForgeDirection.UNKNOWN, charger.getChargerEnergyRF(), false);
            charger.decreaseEnergyRF(receiver.receiveEnergy(ForgeDirection.UNKNOWN, charger.getChargerEnergyRF(), false));
        }
    }

    private static void sendEUToEnergySink(IWirelessMachineCharger charger, IEnergySink sink) {
        double demEnergy = sink.getDemandedEnergy();
        if(demEnergy > 0.0) {
            double sentreal = Math.min(demEnergy, charger.getChargerEnergy());
            sink.injectEnergy(ForgeDirection.UNKNOWN, sentreal, 1);
            charger.decreaseEnergy(sentreal);
        }
    }

    public static boolean iterateEnergyTilesQGen(WirelessQuantumGeneratorBase qgen) {
        boolean ret = false;
        Map<ChunkPosition, TileEntity> teMap = qgen.getWorldObj().getChunkFromBlockCoords(qgen.xCoord, qgen.zCoord).chunkTileEntityMap;
        if(!teMap.isEmpty()) {
            for(TileEntity tile : teMap.values()) {
                if(tile instanceof IEnergySink && !(tile instanceof TileWirelessMachinesChargerBase)) {
                    IEnergySink sink = (IEnergySink) tile;
                    double demEnergy = sink.getDemandedEnergy();
                    if(demEnergy > 0.0) {
                        ret = true;
                        sink.injectEnergy(ForgeDirection.UNKNOWN, demEnergy, 1);
                    }
                } else if(tile instanceof IEnergyReceiver && !(tile instanceof IEnergySink)) {
                    IEnergyReceiver receiver = (IEnergyReceiver) tile;
                    int demandedenergy = receiver.getMaxEnergyStored(ForgeDirection.UNKNOWN) - receiver.getEnergyStored(ForgeDirection.UNKNOWN);
                    if(demandedenergy > 0) {
                        ret = true;
                        receiver.receiveEnergy(ForgeDirection.UNKNOWN, demandedenergy, false);
                    }
                }
            }
        }
        return ret;
    }

    public static void iterateEnergyTiles(TileWirelessMachinesChargerBase charger, boolean chargeEU, boolean chargeRF) {
        Map<ChunkPosition, TileEntity> teMap = charger.getWorldObj().getChunkFromBlockCoords(charger.xCoord, charger.zCoord).chunkTileEntityMap;
        if(!teMap.isEmpty()) {
            for(TileEntity tile : teMap.values()) {
                if(tile instanceof IEnergySink && !(tile instanceof TileWirelessMachinesChargerBase)) {
                    if(chargeEU) {
                        WirelessUtil.sendEUToEnergySink(charger, (IEnergySink) tile);
                    }
                } else if(tile instanceof IEnergyReceiver && !(tile instanceof IEnergySink)) {
                    if(chargeRF) {
                        WirelessUtil.sendRFToReceiver(charger, (IEnergyReceiver) tile);
                    }
                }
            }
        }
    }

}
