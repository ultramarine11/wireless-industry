package ru.wirelesstools.packets;

import java.io.DataInputStream;
import java.io.DataOutputStream;

public abstract class IPacketWI {

	public abstract void read(DataInputStream nameDataInputStream) throws Throwable;

	public abstract void write(DataOutputStream nameDataOutputStream) throws Throwable;

	public void execute() {
	}

}
