package net.netcoding.nifty.common.minecraft.entity.living.human;

import net.netcoding.nifty.common.minecraft.GameMode;
import net.netcoding.nifty.common.minecraft.entity.AnimalTamer;
import net.netcoding.nifty.common.minecraft.entity.living.LivingEntity;
import net.netcoding.nifty.common.minecraft.entity.living.Villager;
import net.netcoding.nifty.common.minecraft.inventory.Inventory;
import net.netcoding.nifty.common.minecraft.inventory.InventoryHolder;
import net.netcoding.nifty.common.minecraft.inventory.InventoryView;
import net.netcoding.nifty.common.minecraft.inventory.type.PlayerInventory;
import net.netcoding.nifty.common.minecraft.inventory.item.ItemStack;
import net.netcoding.nifty.common.minecraft.permission.Permissible;
import net.netcoding.nifty.common.minecraft.region.Location;

/**
 * Represents a human entity, such as an NPC or a player
 */
public interface HumanEntity extends AnimalTamer, InventoryHolder, LivingEntity, Permissible {

	/**
	 * Force-closes the currently open inventory view for the player, if any.
	 */
	void closeInventory();

	/**
	 * Get the player's EnderChest inventory.
	 *
	 * @return The EnderChest of the player.
	 */
	Inventory getEnderChest();

	/**
	 * Gets this human's current {@link GameMode}.
	 *
	 * @return Current game mode.
	 */
	GameMode getGameMode();

	@Override
	PlayerInventory getInventory();

	/**
	 * Returns the ItemStack currently in your hand, can be empty.
	 *
	 * @return The ItemStack of the item you are currently holding.
	 */
	default ItemStack getItemInHand() {
		return this.getInventory().getItemInHand();
	}

	/**
	 * Returns the ItemStack currently on your cursor, can be empty.
	 * <p>
	 * Will always be empty if the player currently has no open window.
	 *
	 * @return The ItemStack of the item you are currently moving around.
	 */
	ItemStack getItemOnCursor();

	/**
	 * Get the total amount of experience required for the player to level up.
	 *
	 * @return The experience required to level up.
	 */
	int getExpToLevel();

	/**
	 * Get the sleep ticks of the player. This value may be capped.
	 *
	 * @return The slumber ticks.
	 */
	int getSleepTicks();

	/**
	 * Check if the player is currently blocking (ie. with a sword).
	 *
	 * @return True if the player is blocking.
	 */
	boolean isBlocking();

	/**
	 * Returns whether this player is slumbering.
	 *
	 * @return True if the player is sleeping.
	 */
	boolean isSleeping();

	/**
	 * Gets the inventory view the player is currently viewing. If they do not
	 * have an inventory window open, it returns their internal crafting view.
	 *
	 * @return The open inventory view.
	 */
	InventoryView getOpenInventory();

	/**
	 * Opens an empty enchanting inventory window with the player's inventory on the bottom.
	 *
	 * @return The newly opened inventory view, or null if it could not be opened.
	 */
	default InventoryView openEnchanting() {
		return this.openEnchanting(this.getLocation(), false);
	}

	/**
	 * Opens an empty enchanting inventory window with the player's inventory on the bottom.
	 *
	 * @param location The location to attach it to. If null, the player's location is used.
	 * @return The newly opened inventory view, or null if it could not be opened.
	 */
	default InventoryView openEnchanting(Location location) {
		return this.openEnchanting(location, false);
	}

	/**
	 * Opens an empty enchanting inventory window with the player's inventory on the bottom.
	 *
	 * @param location The location to attach it to. If null, the player's location is used.
	 * @param force If false, and there is no enchanting table at the location, no inventory will be opened and null will be returned.
	 * @return The newly opened inventory view, or null if it could not be opened.
	 */
	InventoryView openEnchanting(Location location, boolean force);

	/**
	 * Opens an inventory window with the specified inventory on the top and
	 * the player's inventory on the bottom.
	 *
	 * @param inventory The inventory to open.
	 * @return The newly opened inventory view.
	 */
	InventoryView openInventory(Inventory inventory);

	/**
	 * Opens an inventory window to the specified inventory view.
	 *
	 * @param view The view to open.
	 */
	void openInventory(InventoryView view);

	/**
	 * Starts a trade between the player and the villager.
	 *
	 * Note that only one player may trade with a villager at once. You must use
	 * the force parameter for this.
	 *
	 * @param trader The merchant to trade with. Cannot be null.
	 * @return The newly opened inventory view, null if it could not be opened.
	 */
	default InventoryView openMerchant(Villager trader) {
		return this.openMerchant(trader, false);
	}

	/**
	 * Starts a trade between the player and the villager.
	 *
	 * Note that only one player may trade with a villager at once. You must use
	 * the force parameter for this.
	 *
	 * @param trader The merchant to trade with. Cannot be null.
	 * @param force Whether to force the trade even if another player is trading.
	 * @return The newly opened inventory view, null if it could not be opened.
	 */
	InventoryView openMerchant(Villager trader, boolean force);

	/**
	 * Opens an empty workbench inventory window with the player's inventory
	 * on the bottom.
	 *
	 * @return The newly opened inventory view, null if it could not be opened.
	 */
	default InventoryView openWorkbench() {
		return this.openWorkbench(this.getLocation(), false);
	}

	/**
	 * Opens an empty workbench inventory window with the player's inventory
	 * on the bottom.
	 *
	 * @param location The location to attach it to. If null, the player's location is used.
	 * @return The newly opened inventory view, null if it could not be opened.
	 */
	default InventoryView openWorkbench(Location location) {
		return this.openWorkbench(location, false);
	}

	/**
	 * Opens an empty workbench inventory window with the player's inventory
	 * on the bottom.
	 *
	 * @param location The location to attach it to. If null, the player's location is used.
	 * @param force If false, and there is no workbench block at the location, no inventory will be opened and null will be returned.
	 * @return The newly opened inventory view, null if it could not be opened.
	 */
	InventoryView openWorkbench(Location location, boolean force);

	/**
	 * Sets this human's current {@link GameMode}.
	 *
	 * @param mode New game mode.
	 */
	void setGameMode(GameMode mode);

	/**
	 * Sets the item to the given ItemStack, this will replace whatever the user was holding.
	 *
	 * @param item The ItemStack which will end up in the hand.
	 */
	default void setItemInHand(ItemStack item) {
		this.getInventory().setItemInHand(item);
	}

	/**
	 * Sets the item to the given ItemStack, this will replace whatever the user was moving.
	 * <p>
	 * Will always be empty if the player currently has no open window.
	 *
	 * @param item The ItemStack which will end up in the hand.
	 */
	void setItemOnCursor(ItemStack item);

	/**
	 * If the player currently has an inventory window open, this method will
	 * set a property of that window, such as the state of a progress bar.
	 *
	 * @param property The property.
	 * @param value The value to set the property to.
	 * @return True if the property was successfully set.
	 */
	boolean setWindowProperty(InventoryView.Property property, int value);

}