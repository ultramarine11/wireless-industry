package ru.wirelesstools.packets;

import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.WorldServer;
import net.minecraftforge.common.DimensionManager;
import ru.wirelesstools.fluidmachines.TileExpGen;

import java.io.DataInputStream;
import java.io.DataOutputStream;

public class PacketPlayerStandOn extends IPacketWI {

	public int dimID;
	public int x;
	public int y;
	public int z;
	public boolean active;

	@Override
	public void read(DataInputStream bytes) throws Throwable {
		this.dimID = bytes.readInt();
		this.x = bytes.readInt();
		this.y = bytes.readInt();
		this.z = bytes.readInt();
		this.active = bytes.readBoolean();

	}

	@Override
	public void write(DataOutputStream bytes) throws Throwable {
		bytes.writeInt(this.dimID);
		bytes.writeInt(this.x);
		bytes.writeInt(this.y);
		bytes.writeInt(this.z);
		bytes.writeBoolean(this.active);

	}

	public void execute() {
		try {
			WorldServer ws = DimensionManager.getWorld(this.dimID);
			if (ws != null) {
				TileEntity te = ws.getTileEntity(this.x, this.y, this.z);
				if (te != null && te instanceof TileExpGen)
					((TileExpGen) te).setActive(this.active);
			}
		} catch (Throwable t) {
			t.printStackTrace();
		}
	}

}
