package net.netcoding.nifty.common.minecraft.event.inventory;

import net.netcoding.nifty.common.minecraft.inventory.Inventory;
import net.netcoding.nifty.common.minecraft.inventory.InventoryType;
import net.netcoding.nifty.common.minecraft.inventory.item.ItemStack;

public interface InventoryClickEvent extends InventoryInteractEvent {

	Inventory.Action getAction();

	Inventory.ClickType getClick();

	ItemStack getCurrentItem();

	ItemStack getCursor();

	int getHotbarButton();

	int getRawSlot();

	int getSlot();

	InventoryType.SlotType getSlotType();

	default boolean isLeftClick() {
		return this.getClick().isLeftClick();
	}

	default boolean isRightClick() {
		return this.getClick().isRightClick();
	}

	default boolean isShiftClick() {
		return this.getClick().isShiftClick();
	}

	void setCurrentItem(ItemStack item);

	void setCursor(ItemStack item);

}