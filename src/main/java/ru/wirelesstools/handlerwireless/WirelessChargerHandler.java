package ru.wirelesstools.handlerwireless;

import cofh.api.energy.IEnergyContainerItem;
import ic2.api.item.IElectricItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.world.World;
import ru.wirelesstools.tiles.IWirelessCharger;
import ru.wirelesstools.utils.WirelessUtil;

import java.util.List;

public class WirelessChargerHandler implements IWirelessChargerHandler {

    @Override
    public int checkPlayersAround(boolean isPrivate, IWirelessCharger tile, int radius, World world) {
        AxisAlignedBB axisalignedbb = AxisAlignedBB.getBoundingBox(tile.getXCoord() - radius,
                tile.getYCoord() - radius, tile.getZCoord() - radius,
                tile.getXCoord() + radius + 1, tile.getYCoord() + radius + 1,
                tile.getZCoord() + radius + 1);
        List<EntityPlayer> list = world.getEntitiesWithinAABB(EntityPlayer.class, axisalignedbb);
        if (isPrivate) {
            if (tile.getOwnerCharger() != null) {
                for (EntityPlayer localplayer : list) {
                    if (localplayer != null) {
                        if (!localplayer.getGameProfile().equals(tile.getOwnerCharger()))
                            continue;

                        this.checkPlayerInventory(localplayer, tile);
                        return 1;
                    }
                }
            }
        } else {
            for (EntityPlayer localplayer : list) {
                if (localplayer != null)
                    this.checkPlayerInventory(localplayer, tile);
            }
            return list.size();
        }
        return 0;
    }

    private void checkPlayerInventory(EntityPlayer player, IWirelessCharger tile) {
        for (ItemStack currentstackarmor : player.inventory.armorInventory) {
            if (currentstackarmor == null)
                continue;

            if (currentstackarmor.getItem() instanceof IElectricItem) {
                if (tile.getCurrentEnergyInCharger() > 0.0)
                    WirelessUtil.chargeItemEU(tile, currentstackarmor);
            }

            if (currentstackarmor.getItem() instanceof IEnergyContainerItem) {
                if (tile.getCurrentEnergyInCharger() > 0.0)
                    WirelessUtil.chargeItemRF(tile, currentstackarmor);
            }
        }

        for (ItemStack currentstackinventory : player.inventory.mainInventory) {
            if (currentstackinventory == null)
                continue;

            if (currentstackinventory.getItem() instanceof IElectricItem) {
                if (tile.getCurrentEnergyInCharger() > 0.0)
                    WirelessUtil.chargeItemEU(tile, currentstackinventory);
            }

            if (currentstackinventory.getItem() instanceof IEnergyContainerItem) {
                if (tile.getCurrentEnergyInCharger() > 0.0)
                    WirelessUtil.chargeItemRF(tile, currentstackinventory);
            }
        }
    }

}
