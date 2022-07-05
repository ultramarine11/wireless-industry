package ru.wirelesstools.container;

import ic2.core.ContainerFullInv;
import net.minecraft.entity.player.EntityPlayer;
import ru.wirelesstools.tiles.TileXPTransmitter;

import java.util.List;

public class ContainerXPTransmitter extends ContainerFullInv<TileXPTransmitter> {
    
    public ContainerXPTransmitter(EntityPlayer player, TileXPTransmitter base) {
        super(player, base, 166);
    }
    
    public List<String> getNetworkedFields() {
        List<String> ret = super.getNetworkedFields();
        ret.add("sendingXPMode");
        ret.add("isOn");
        ret.add("amountXPTransmit");
        ret.add("storedXP");
        ret.add("playersCount");
        ret.add("energy");
        return ret;
    }
}
