package net.netcoding.nifty.common.minecraft.material;

import net.netcoding.nifty.common.minecraft.inventory.item.ItemStack;

public interface MaterialData extends Cloneable {

	MaterialData clone();

	byte getData();

	default Material getItemType() {
		return Material.getMaterial(this.getItemTypeId());
	}

	int getItemTypeId();

	void setData(byte data);

	default ItemStack toItemStack() {
		return ItemStack.of(this.getItemType(), 1, this.getData());
	}

	default ItemStack toItemStack(int amount) {
		return ItemStack.of(this.getItemType(), (amount < 1 ? 1 : amount), this.getData());
	}

}