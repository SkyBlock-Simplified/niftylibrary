package net.netcoding.nifty.craftbukkit.minecraft.event.inventory;

import net.netcoding.nifty.common.minecraft.event.inventory.InventoryClickEvent;
import net.netcoding.nifty.common.minecraft.inventory.Inventory;
import net.netcoding.nifty.common.minecraft.inventory.InventoryType;
import net.netcoding.nifty.common.minecraft.inventory.item.ItemStack;
import net.netcoding.nifty.craftbukkit.minecraft.inventory.item.CraftItemStack;
import net.netcoding.nifty.craftbukkit.util.CraftConverter;

@SuppressWarnings("deprecation")
public class CraftInventoryClickEvent extends CraftInventoryInteractEvent implements InventoryClickEvent {

	public CraftInventoryClickEvent(org.bukkit.event.inventory.InventoryClickEvent inventoryClickEvent) {
		super(inventoryClickEvent);
	}

	@Override
	public Inventory.Action getAction() {
		return Inventory.Action.valueOf(this.getHandle().getAction().name());
	}

	@Override
	public Inventory.ClickType getClick() {
		return Inventory.ClickType.valueOf(this.getHandle().getClick().name());
	}

	@Override
	public ItemStack getCurrentItem() {
		return new CraftItemStack(this.getHandle().getCurrentItem());
	}

	@Override
	public ItemStack getCursor() {
		return new CraftItemStack(this.getHandle().getCursor());
	}

	@Override
	public org.bukkit.event.inventory.InventoryClickEvent getHandle() {
		return (org.bukkit.event.inventory.InventoryClickEvent)super.getHandle();
	}

	@Override
	public int getHotbarButton() {
		return this.getHandle().getHotbarButton();
	}

	@Override
	public int getRawSlot() {
		return this.getHandle().getRawSlot();
	}

	@Override
	public int getSlot() {
		return this.getHandle().getSlot();
	}

	@Override
	public InventoryType.SlotType getSlotType() {
		return InventoryType.SlotType.valueOf(this.getHandle().getSlotType().name());
	}

	@Override
	public void setCurrentItem(ItemStack item) {
		this.getHandle().setCurrentItem(CraftConverter.toBukkitItem(item));
	}

	@Override
	public void setCursor(ItemStack item) {
		this.getHandle().setCursor(CraftConverter.toBukkitItem(item));
	}

}