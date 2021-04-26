package ru.wirelesstools.container;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.ICrafting;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import ru.wirelesstools.tiles.WirelessQuantumGeneratorBase;

public class ContainerQGenWireless extends Container {

	protected WirelessQuantumGeneratorBase tile;

	public ContainerQGenWireless(InventoryPlayer inventoryplayer, WirelessQuantumGeneratorBase tile) {
		this.tile = tile;

		for (int i = 0; i < 3; i++) {

			for (int k = 0; k < 9; k++) {
				addSlotToContainer(new Slot((IInventory) inventoryplayer, k + i * 9 + 9, 8 + k * 18, 67 + i * 18));
			}
		}

		for (int j = 0; j < 9; j++) {
			addSlotToContainer(new Slot((IInventory) inventoryplayer, j, 8 + j * 18, 125));
		}
	}

	public void detectAndSendChanges() {
		super.detectAndSendChanges();
		for (int i = 0; i < this.crafters.size(); i++) {
			ICrafting icrafting = (ICrafting) this.crafters.get(i);
			icrafting.sendProgressBarUpdate(this, 1, this.tile.isCharging ? 1 : 0);
		}
	}

	public ItemStack transferStackInSlot(EntityPlayer par1EntityPlayer, int par2) {
		int slot = par2;
		ItemStack stack = null;
		Slot slotObject = (Slot) this.inventorySlots.get(par2);

		if (slotObject != null && slotObject.getHasStack()) {
			ItemStack stackInSlot = slotObject.getStack();
			stack = stackInSlot.copy();

			if (stackInSlot.stackSize == 0) {

				slotObject.putStack(null);
			} else {

				slotObject.onSlotChanged();
			}

			if (stack.stackSize != stackInSlot.stackSize) {

				slotObject.onPickupFromSlot(par1EntityPlayer, stackInSlot);
			} else {

				return null;
			}
		}

		return stack;
	}

	public void updateProgressBar(int id, int j) {

		if (id == 1) {

			this.tile.isCharging = (j != 0);
		}
	}

	@Override
	public boolean canInteractWith(EntityPlayer player) {

		return this.tile.isUseableByPlayer(player);
	}

}
