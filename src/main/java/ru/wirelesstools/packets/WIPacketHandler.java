package ru.wirelesstools.packets;

import com.google.common.collect.Lists;
import cpw.mods.fml.common.network.FMLEmbeddedChannel;
import cpw.mods.fml.common.network.FMLOutboundHandler;
import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.relauncher.Side;

import java.util.EnumMap;
import java.util.List;

public class WIPacketHandler {

	public static List<Class<? extends IPacketWI>> packetTypes = Lists.newArrayList();

	private static EnumMap<Side, FMLEmbeddedChannel> channels;

	public static void load() {
		registerPacketType(PacketGuiPressButton.class);
		registerPacketType(PacketChange.class);
		registerPacketType(PacketPlayerStandOn.class);
		channels = NetworkRegistry.INSTANCE.newChannel("WirelessSP", new WIChannelHandler());

	}

	public static void registerPacketType(Class<? extends IPacketWI> ptype) {
		packetTypes.add(ptype);

	}

	public static void sendToAllPlayers(IPacketWI packet) {
		channels.get(Side.SERVER).attr(FMLOutboundHandler.FML_MESSAGETARGET)
				.set(FMLOutboundHandler.OutboundTarget.ALL);
		channels.get(Side.SERVER).writeOutbound(packet);

	}

	public static void sendToServer(IPacketWI packet) {
		channels.get(Side.CLIENT).attr(FMLOutboundHandler.FML_MESSAGETARGET)
				.set(FMLOutboundHandler.OutboundTarget.TOSERVER);

		channels.get(Side.CLIENT).writeOutbound(packet);

	}

}
