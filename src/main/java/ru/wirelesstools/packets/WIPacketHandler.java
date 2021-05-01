package ru.wirelesstools.packets;

import java.util.EnumMap;
import java.util.List;

import com.google.common.collect.Lists;

import cpw.mods.fml.common.network.FMLEmbeddedChannel;
import cpw.mods.fml.common.network.FMLOutboundHandler;
import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.relauncher.Side;
import io.netty.channel.ChannelHandler;
import net.minecraft.entity.player.EntityPlayer;

public class WIPacketHandler {

	public static List<Class<? extends IPacketWI>> packetTypes = Lists.newArrayList();

	private static EnumMap<Side, FMLEmbeddedChannel> channels;

	public static void load() {
		registerPacketType(PacketGuiPressButton.class);
		registerPacketType(PacketChange.class);
		registerPacketType(PacketPlayerStandOn.class);
		channels = NetworkRegistry.INSTANCE.newChannel("WirelessSP", new ChannelHandler[] { new WIChannelHandler() });

	}

	public static void registerPacketType(Class<? extends IPacketWI> ptype) {
		packetTypes.add(ptype);

	}

	public static void sendToAllPlayers(IPacketWI packet) {
		((FMLEmbeddedChannel) channels.get(Side.SERVER)).attr(FMLOutboundHandler.FML_MESSAGETARGET)
				.set(FMLOutboundHandler.OutboundTarget.ALL);
		((FMLEmbeddedChannel) channels.get(Side.SERVER)).writeOutbound(new Object[] { packet });

	}

	public static void sendToServer(IPacketWI packet) {
		((FMLEmbeddedChannel) channels.get(Side.CLIENT)).attr(FMLOutboundHandler.FML_MESSAGETARGET)
				.set(FMLOutboundHandler.OutboundTarget.TOSERVER);

		((FMLEmbeddedChannel) channels.get(Side.CLIENT)).writeOutbound(new Object[] { packet });

	}

	public static void sendToPlayer(EntityPlayer ep, IPacketWI packet) {

		((FMLEmbeddedChannel) channels.get(Side.SERVER)).attr(FMLOutboundHandler.FML_MESSAGETARGET)
				.set(FMLOutboundHandler.OutboundTarget.PLAYER);

		((FMLEmbeddedChannel) channels.get(Side.SERVER)).attr(FMLOutboundHandler.FML_MESSAGETARGETARGS).set(ep);

		((FMLEmbeddedChannel) channels.get(Side.SERVER)).writeOutbound(new Object[] { packet });

	}

}
