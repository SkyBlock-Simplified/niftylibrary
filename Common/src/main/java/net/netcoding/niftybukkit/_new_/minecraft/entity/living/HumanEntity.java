package net.netcoding.niftybukkit._new_.minecraft.entity.living;

import net.netcoding.niftybukkit._new_.minecraft.GameMode;
import net.netcoding.niftybukkit._new_.minecraft.entity.AnimalTamer;
import net.netcoding.niftybukkit._new_.minecraft.inventory.Inventory;
import net.netcoding.niftybukkit._new_.minecraft.inventory.InventoryHolder;
import net.netcoding.niftybukkit._new_.minecraft.inventory.PlayerInventory;
import net.netcoding.niftybukkit._new_.minecraft.inventory.item.ItemStack;
import net.netcoding.niftybukkit._new_.minecraft.permission.Permissible;
import net.netcoding.niftybukkit._new_.minecraft.region.Location;
import org.bukkit.inventory.InventoryView;

public interface HumanEntity extends AnimalTamer, InventoryHolder, LivingEntity, Permissible {

	void closeInventory();

	Inventory getEnderChest();

	GameMode getGameMode();

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