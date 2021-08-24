package ru.wirelesstools.proxy;

import cpw.mods.fml.common.event.FMLServerStartingEvent;
import cpw.mods.fml.common.network.IGuiHandler;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import ru.wirelesstools.command.CommandChangeOwnerArmor;
import ru.wirelesstools.command.CommandClearOwner;
import ru.wirelesstools.fluidmachines.TileExpGen;
import ru.wirelesstools.recipes.Recipes;
import ru.wirelesstools.tiles.*;

public class ServerProxy implements IGuiHandler {

    public void initRecipes() {
       // PFPConvertorTile.init();
        Recipes.initPFPRecipes();
    }

    public static void Init() {
    }

    public void serverStart(FMLServerStartingEvent event) {
        event.registerServerCommand(new CommandClearOwner());
        event.registerServerCommand(new CommandChangeOwnerArmor());
    }

    public Object getServerGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
        TileEntity te = world.getTileEntity(x, y, z);
        /*if(te != null) {*/

            if(te instanceof TileWPBasePersonal) {

                return ((TileWPBasePersonal) te).getGuiContainer(player.inventory);
            }

            if(te instanceof TileWirelessStorageBasePersonal) {

                return ((TileWirelessStorageBasePersonal) te).getGuiContainer(player.inventory);
            }

            if(te instanceof TileExpGen) {

                return ((TileExpGen) te).getGuiContainer(player.inventory);
            }

            if(te instanceof TileVajraChargerElectric) {

                return ((TileVajraChargerElectric) te).getGuiContainer(player.inventory);
            }

            if(te instanceof TileEntityWirelessCharger) {

                return ((TileEntityWirelessCharger) te).getGuiContainer(player);
            }

            if(te instanceof TileWirelessMachinesChargerBase) {

                return ((TileWirelessMachinesChargerBase) te).getGuiContainer(player);
            }

            if(te instanceof WirelessQuantumGeneratorBase) {

                return ((WirelessQuantumGeneratorBase) te).getGuiContainer(player.inventory);
            }

            if(te instanceof TileXPSenderElectric) {

                return ((TileXPSenderElectric) te).getGuiContainer(player);
            }

            if(te instanceof TileLiquidMatterCollector) {

                return ((TileLiquidMatterCollector) te).getGuiContainer(player);
            }

            if(te instanceof PFPConvertorTile) {

                return ((PFPConvertorTile) te).getGuiContainer(player);
            }

        /*}*/

        return null;
    }

    public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {

        return null;
    }

}
