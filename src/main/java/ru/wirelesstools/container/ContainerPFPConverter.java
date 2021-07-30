package ru.wirelesstools.container;

import ic2.core.ContainerFullInv;
import ic2.core.slot.SlotInvSlot;
import net.minecraft.entity.player.EntityPlayer;
import ru.wirelesstools.tiles.PFPConvertorTile;

import java.util.List;

public class ContainerPFPConverter extends ContainerFullInv<PFPConvertorTile> {

    public ContainerPFPConverter(EntityPlayer player, PFPConvertorTile base) {
        super(player, base, 166);
        this.addSlotToContainer(new SlotInvSlot(base.inputSlotA, 0, 54, 23));
        this.addSlotToContainer(new SlotInvSlot(base.outputSlot, 0, 116, 23));
    }

    public List<String> getNetworkedFields() {
        List<String> ret = super.getNetworkedFields();
        ret.add("progress");
        ret.add("energyEU");

        return ret;
    }
}
