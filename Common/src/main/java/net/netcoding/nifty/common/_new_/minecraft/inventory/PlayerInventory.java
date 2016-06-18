package net.netcoding.nifty.common._new_.minecraft.inventory;

import net.netcoding.nifty.common._new_.minecraft.entity.living.HumanEntity;
import net.netcoding.nifty.common._new_.minecraft.inventory.item.ItemStack;

public interface PlayerInventory extends Inventory {

	ItemStack[] getArmorContents();

	ItemStack getBoots();

	ItemStack getChestplate();

	ItemStack[] getExtraContents();

	int getHeldItemSlot();

	ItemStack getHelmet();

	HumanEntity getHolder();

	ItemStack getItemInHand();

	default ItemStack getItemInMainHand() {
		return this.getItemInHand();
	}

	ItemStack getItemInOffHand();

	ItemStack getLeggings();

	void setItem(int slot, ItemStack itemStack);

	void setArmorContents(ItemStack[] armorContents);

	void setExtraContents(ItemStack[] extraContents);

	void setBoots(ItemStack itemStack);

	void setChestplate(ItemStack itemStack);

	void setHelmet(ItemStack itemStack);

	void setItemInHand(ItemStack itemStack);

	default void setItemInMainHand(ItemStack itemStack) {
		this.setItemInHand(itemStack);
	}

	void setItemInOffHand(ItemStack itemStack);

	void setLeggings(ItemStack itemStack);

	void setHeldItemSlot(int var1);

	/** @deprecated */
	@Deprecated
	int clear(int var1, int var2);

}