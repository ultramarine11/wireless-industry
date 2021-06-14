package ru.wirelesstools.packets;

import cpw.mods.fml.common.network.FMLIndexedMessageToMessageCodec;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;

public class WIChannelHandler extends FMLIndexedMessageToMessageCodec<IPacketWI> {

	public WIChannelHandler() {

		for (Class<? extends IPacketWI> clazz : WIPacketHandler.packetTypes) {

			addDiscriminator(WIPacketHandler.packetTypes.indexOf(clazz), clazz);
		}

	}

	@Override
	public void encodeInto(ChannelHandlerContext ctx, IPacketWI msg, ByteBuf target) {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		try {
			msg.write(new DataOutputStream(baos));
		} catch (Throwable e) {
			e.printStackTrace();
		}
		target.writeBytes(baos.toByteArray());

	}

	@Override
	public void decodeInto(ChannelHandlerContext ctx, ByteBuf source, IPacketWI msg) {

		byte[] arr = new byte[source.readableBytes()];
		source.readBytes(arr);
		ByteArrayInputStream bais = new ByteArrayInputStream(arr);
		try {
			msg.read(new DataInputStream(bais));
		} catch (Throwable e) {
			e.printStackTrace();
			return;
		}
		msg.execute();

	}

}
