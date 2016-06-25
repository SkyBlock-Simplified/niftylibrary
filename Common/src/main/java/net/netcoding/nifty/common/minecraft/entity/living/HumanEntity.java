package net.netcoding.nifty.common.minecraft.entity.living;

import net.netcoding.nifty.common.minecraft.GameMode;
import net.netcoding.nifty.common.minecraft.entity.AnimalTamer;
import net.netcoding.nifty.common.minecraft.inventory.Inventory;
import net.netcoding.nifty.common.minecraft.inventory.InventoryHolder;
import net.netcoding.nifty.common.minecraft.inventory.InventoryView;
import net.netcoding.nifty.common.minecraft.inventory.types.PlayerInventory;
import net.netcoding.nifty.common.minecraft.inventory.item.ItemStack;
import net.netcoding.nifty.common.minecraft.permission.Permissible;
import net.netcoding.nifty.common.minecraft.region.Location;

/**
 * Represents a human entity, such as an NPC or a player
 */
public interface HumanEntity extends AnimalTamer, InventoryHolder, LivingEntity, Permissible {

	void closeInventory();

	Inventory getEnderChest();

	GameMode getGameMode();

	@Override
	PlayerInventory getInventory();

	default ItemStack getItemInHand() {
		return this.getInventory().getItemInHand();
	}

	ItemStack getItemOnCursor();

	int getExpToLevel();

	int getSleepTicks();

	boolean isBlocking();

	boolean isSleeping();

	InventoryView getOpenInventory();

	InventoryView openEnchanting(Location var1, boolean var2);

	InventoryView openInventory(Inventory inventory);

	void openInventory(InventoryView view);

	//InventoryView openMerchant(Villager var1, boolean var2);

	InventoryView openWorkbench(Location var1, boolean var2);

	void setGameMode(GameMode gameMode);

	default void setItemInHand(ItemStack itemStack) {
		this.getInventory().setItemInHand(itemStack);
	}

	void setItemOnCursor(ItemStack itemStack);

	boolean setWindowProperty(InventoryView.Property var1, int var2);

}