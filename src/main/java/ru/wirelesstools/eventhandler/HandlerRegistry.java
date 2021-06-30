package ru.wirelesstools.eventhandler;

import cpw.mods.fml.common.FMLCommonHandler;
import net.minecraftforge.common.MinecraftForge;

public class HandlerRegistry {

    public static void register() {
        WIEventHandler handler = new WIEventHandler();
        MinecraftForge.EVENT_BUS.register(handler);
        FMLCommonHandler.instance().bus().register(handler);
    }
}
