package ru.wirelesstools.container;

import ic2.core.ContainerFullInv;
import net.minecraft.entity.player.EntityPlayer;
import ru.wirelesstools.tiles.WirelessQuantumGeneratorBase;

import java.util.List;

public class ContainerWirelessQuantumGen extends ContainerFullInv<WirelessQuantumGeneratorBase> {

    public ContainerWirelessQuantumGen(EntityPlayer player, WirelessQuantumGeneratorBase base) {
        super(player, base, 188);
    }

    public List<String> getNetworkedFields() {
        List<String> ret = super.getNetworkedFields();
        ret.add("owner");
        ret.add("chargingStatus");
        ret.add("machinesCountInChunk");
        ret.add("modeTransmitting");
        return ret;
    }
}
