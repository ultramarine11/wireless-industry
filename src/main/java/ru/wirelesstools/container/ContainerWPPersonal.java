package ru.wirelesstools.container;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.ICrafting;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import ru.wirelesstools.tiles.TileWPBasePersonal;

public class ContainerWPPersonal extends Container {
	protected TileWPBasePersonal tileentity;

	public ContainerWPPersonal(InventoryPlayer inventoryplayer, TileWPBasePersonal tileEntity1) {
		this.tileentity = tileEntity1;
		tileEntity1.openInventory();

		for (int i = 0; i < 3; i++) {
			for (int m = 0; m < 9; m++) {
				addSlotToContainer(new Slot(inventoryplayer, m + i * 9 + 9, 17 + m * 18, 86 + i * 18));
			}
		}

		for (int j = 0; j < 9; j++) {
			addSlotToContainer(new Slot(inventoryplayer, j, 17 + j * 18, 144));
		}

	}

	public void onCraftGuiOpened(ICrafting icrafting) {
		super.addCraftingToCrafters(icrafting);
		icrafting.sendProgressBarUpdate(this, 0, this.tileentity.sunIsUp ? 1 : 0);
		icrafting.sendProgressBarUpdate(this, 1, this.tileentity.skyIsVisible ? 1 : 0);
		icrafting.sendProgressBarUpdate(this, 2, this.tileentity.generating & 0xFFFF);
		icrafting.sendProgressBarUpdate(this, 3, this.tileentity.isconnected ? 1 : 0);
		icrafting.sendProgressBarUpdate(this, 4, (int) this.tileentity.storage & 0xFFFF);

		icrafting.sendProgressBarUpdate(this, 6, (int) this.tileentity.storage >>> 16);
		icrafting.sendProgressBarUpdate(this, 7, this.tileentity.generating >>> 16);

		icrafting.sendProgressBarUpdate(this, 8, this.tileentity.channel);

	}

	public void detectAndSendChanges() {
		super.detectAndSendChanges();
		for (int i = 0; i < this.crafters.size(); i++) {
			ICrafting icrafting = (ICrafting) this.crafters.get(i);
			icrafting.sendProgressBarUpdate(this, 0, this.tileentity.sunIsUp ? 1 : 0);
			icrafting.sendProgressBarUpdate(this, 1, this.tileentity.skyIsVisible ? 1 : 0);
			icrafting.sendProgressBarUpdate(this, 2, this.tileentity.generating & 0xFFFF);
			icrafting.sendProgressBarUpdate(this, 3, this.tileentity.isconnected ? 1 : 0);

			icrafting.sendProgressBarUpdate(this, 4, (int) this.tileentity.storage & 0xFFFF);
			icrafting.sendProgressBarUpdate(this, 6, (int) this.tileentity.storage >>> 16);

			icrafting.sendProgressBarUpdate(this, 7, this.tileentity.generating >>> 16);
			icrafting.sendProgressBarUpdate(this, 8, this.tileentity.channel);
		}
	}

	public ItemStack transferStackInSlot(EntityPlayer par1EntityPlayer, int par2) {
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

	@Override
	public boolean canInteractWith(EntityPlayer player) {

		return this.tileentity.isUseableByPlayer(player);
	}

	public void onContainerClosed(EntityPlayer entityplayer) {

		this.tileentity.closeInventory();
	}

	public void updateProgressBar(int id, int j) {

		if (id == 0) {

			this.tileentity.sunIsUp = (j != 0);
		}

		if (id == 1) {

			this.tileentity.skyIsVisible = (j != 0);
		}

		if (id == 2) {

			this.tileentity.generating = this.tileentity.generating & 0xFFFF0000 | j;
		}

		if (id == 3) {

			this.tileentity.isconnected = (j != 0);
		}

		if (id == 4) {

			this.tileentity.storage = (int) this.tileentity.storage & 0xFFFF0000 | j;
		}

		if (id == 6) {

			this.tileentity.storage = (int) this.tileentity.storage & 0xFFFF | j << 16;
		}

		if (id == 7) {

			this.tileentity.generating = this.tileentity.generating & 0xFFFF | j << 16;
		}

		if (id == 8) {

			this.tileentity.channel = j;
		}

	}
}
