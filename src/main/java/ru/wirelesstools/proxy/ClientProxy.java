package ru.wirelesstools.proxy;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;
import net.minecraftforge.client.event.TextureStitchEvent;
import net.minecraftforge.common.MinecraftForge;
import ru.wirelesstools.MainWI;
import ru.wirelesstools.fluidmachines.TextureHooks;
import ru.wirelesstools.fluidmachines.TileExpGen;
import ru.wirelesstools.fluidmachines.TileXPGenPublic;
import ru.wirelesstools.gui.GuiExpGen;
import ru.wirelesstools.gui.GuiVajraCharger;
import ru.wirelesstools.gui.GuiWPPersonal;
import ru.wirelesstools.gui.GuiWSBPersonal;
import ru.wirelesstools.gui.GuiWirelessMachinesCharger;
import ru.wirelesstools.gui.GuiWirelessQGen;
import ru.wirelesstools.tiles.TileEntityWirelessCharger;
import ru.wirelesstools.tiles.TileVajraChargerElectric;
import ru.wirelesstools.tiles.TileWPBasePersonal;
import ru.wirelesstools.tiles.TileWirelessMachinesChargerBase;
import ru.wirelesstools.tiles.TileWirelessStorageBasePersonal;
import ru.wirelesstools.tiles.TileXPSenderElectric;
import ru.wirelesstools.tiles.WirelessQuantumGeneratorBase;

public class ClientProxy extends ServerProxy {

	@SideOnly(Side.CLIENT)
	public static void Init() {

		MinecraftForge.EVENT_BUS.register(new TextureHooks());

	}

	public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
		TileEntity te = world.getTileEntity(x, y, z);

		if (te != null) {

			if (te instanceof TileWPBasePersonal) {

				return new GuiWPPersonal(player.inventory, (TileWPBasePersonal) te);

			}

			if (te instanceof TileWirelessStorageBasePersonal) {

				return new GuiWSBPersonal(player.inventory, (TileWirelessStorageBasePersonal) te);

			}

			if (te instanceof TileExpGen) {

				return new GuiExpGen(player.inventory, (TileExpGen) te);

			}

			if (te instanceof TileVajraChargerElectric) {

				return new GuiVajraCharger(player.inventory, (TileVajraChargerElectric) te);

			}

			if (te instanceof TileEntityWirelessCharger) {

				return ((TileEntityWirelessCharger) te).getGui(player, false);

			}

			if (te instanceof TileWirelessMachinesChargerBase) {

				return new GuiWirelessMachinesCharger(player.inventory, (TileWirelessMachinesChargerBase) te);
			}

			if (te instanceof WirelessQuantumGeneratorBase) {

				return new GuiWirelessQGen(player.inventory, (WirelessQuantumGeneratorBase) te);
			}
			
			if (te instanceof TileXPSenderElectric) {
				
				return ((TileXPSenderElectric) te).getGui(player, false);
			}

		}

		return null;
	}

}
