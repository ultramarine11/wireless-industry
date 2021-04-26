package ru.wirelesstools.proxy;

import cpw.mods.fml.common.event.FMLServerStartingEvent;
import cpw.mods.fml.common.network.IGuiHandler;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import ru.wirelesstools.command.CommandClearOwner;
import ru.wirelesstools.fluidmachines.TileExpGen;
import ru.wirelesstools.fluidmachines.TileXPGenPublic;
import ru.wirelesstools.tiles.TileEntityWirelessCharger;
import ru.wirelesstools.tiles.TileVajraChargerElectric;
import ru.wirelesstools.tiles.TileWPBasePersonal;
import ru.wirelesstools.tiles.TileWirelessMachinesChargerBase;
import ru.wirelesstools.tiles.TileWirelessStorageBasePersonal;
import ru.wirelesstools.tiles.WirelessQuantumGeneratorBase;

public class ServerProxy implements IGuiHandler {

	public static void Init() {

	}
	
	public void serverStart(FMLServerStartingEvent event) {
		
		event.registerServerCommand(new CommandClearOwner());
	}

	public Object getServerGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
		TileEntity te = world.getTileEntity(x, y, z);

		if (te != null) {

			if (te instanceof TileWPBasePersonal) {

				return ((TileWPBasePersonal) te).getGuiContainer(player.inventory);
			}

			if (te instanceof TileWirelessStorageBasePersonal) {

				return ((TileWirelessStorageBasePersonal) te).getGuiContainer(player.inventory);
			}

			if (te instanceof TileExpGen) {

				return ((TileExpGen) te).getGuiContainer(player.inventory);
			}

			if (te instanceof TileVajraChargerElectric) {

				return ((TileVajraChargerElectric) te).getGuiContainer(player.inventory);
			}

			if (te instanceof TileEntityWirelessCharger) {

				return ((TileEntityWirelessCharger) te).getGuiContainer(player.inventory);
			}

			if (te instanceof TileWirelessMachinesChargerBase) {

				return ((TileWirelessMachinesChargerBase) te).getGuiContainer(player.inventory);
			}
			
			if (te instanceof WirelessQuantumGeneratorBase) {

				return ((WirelessQuantumGeneratorBase) te).getGuiContainer(player.inventory);
			}

		}

		return null;
	}

	public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {

		return null;
	}

}
