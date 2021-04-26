package ru.wirelesstools;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.TickEvent;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class TickHandlerLV {

	public TickHandlerLV() {

		FMLCommonHandler.instance().bus().register(this);

	}

	@SideOnly(Side.CLIENT)
	@SubscribeEvent
	public void renderTick(TickEvent.RenderTickEvent event) {
		if (event.phase != TickEvent.Phase.START) {
			ClientTickHandlerLV.onTickRender();

		}

	}
}
