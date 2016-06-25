package net.netcoding.nifty.common.minecraft.event.inventory;

import net.netcoding.nifty.common.minecraft.inventory.ClickType;
import net.netcoding.nifty.common.minecraft.inventory.InventoryAction;
import net.netcoding.nifty.common.minecraft.inventory.InventoryType;
import net.netcoding.nifty.common.minecraft.inventory.item.ItemStack;

public interface InventoryClickEvent extends InventoryInteractEvent {

	InventoryAction getAction();

	ClickType getClick();

	ItemStack getCurrentItem();

	ItemStack getCursor();

	int getHotbarButton();

	int getRawSlot();

	int getSlot();

	InventoryType.SlotType getSlotType();

	boolean isLeftClick();

	boolean isRightClick();

	boolean isShiftClick();

	void setCurrentItem(ItemStack item);

	void setCursor(ItemStack item);

}