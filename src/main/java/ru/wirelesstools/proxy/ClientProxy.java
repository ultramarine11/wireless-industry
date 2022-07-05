package ru.wirelesstools.proxy;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import ic2.core.IHasGui;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import ru.wirelesstools.fluidmachines.TextureHooks;
import ru.wirelesstools.fluidmachines.TileExpGen;
import ru.wirelesstools.gui.GuiExpGen;
import ru.wirelesstools.gui.GuiVajraCharger;
import ru.wirelesstools.gui.GuiWPPersonal;
import ru.wirelesstools.gui.GuiWSBPersonal;
import ru.wirelesstools.tiles.*;

public class ClientProxy extends ServerProxy {

    @SideOnly(Side.CLIENT)
    public static void Init() {
        MinecraftForge.EVENT_BUS.register(new TextureHooks());
    }

    public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
        TileEntity te = world.getTileEntity(x, y, z);

        if(te instanceof IHasGui) {
            return ((IHasGui)te).getGui(player, false);
        }

        if(te instanceof TileWPBasePersonal) {
            return new GuiWPPersonal(player.inventory, (TileWPBasePersonal)te);

        }

        if(te instanceof TileWirelessStorageBasePersonal) {
            return new GuiWSBPersonal(player.inventory, (TileWirelessStorageBasePersonal)te);

        }

        if(te instanceof TileExpGen) {
            return new GuiExpGen(player.inventory, (TileExpGen)te);

        }

        if(te instanceof TileVajraChargerElectric) {
            return new GuiVajraCharger(player.inventory, (TileVajraChargerElectric)te);

        }

        return null;
    }

}
