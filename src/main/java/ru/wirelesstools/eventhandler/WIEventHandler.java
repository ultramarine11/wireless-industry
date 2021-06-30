package ru.wirelesstools.eventhandler;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.PlayerEvent;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ChatComponentTranslation;
import net.minecraft.util.StatCollector;
import ru.wirelesstools.command.CommandClearOwner;

public class WIEventHandler {

    @SubscribeEvent
    public void onPlayerJoinEvent(PlayerEvent.PlayerLoggedInEvent event) {
        EntityPlayer player = event.player;
        player.addChatComponentMessage(new ChatComponentTranslation(StatCollector.translateToLocal("chat.message.wi.commands")
        + ": " + "/" + CommandClearOwner.NAME_COMMAND + " "
        + StatCollector.translateToLocal("chat.message.wi.and") + " " + "/" + CommandClearOwner.ALIAS_1));

    }
}
