package ru.wirelesstools.container;

import ic2.core.ContainerFullInv;
import net.minecraft.entity.player.EntityPlayer;
import ru.wirelesstools.tiles.TileLiquidMatterCollector;

import java.util.List;

public class ContainerLiquidMatterCollector extends ContainerFullInv<TileLiquidMatterCollector> {

    public ContainerLiquidMatterCollector(EntityPlayer player, TileLiquidMatterCollector base) {
        super(player, base, 166);
    }

    public List<String> getNetworkedFields() {
        List<String> ret = super.getNetworkedFields();
        ret.add("fluidTank");
        ret.add("isActive");
        return ret;
    }

}
