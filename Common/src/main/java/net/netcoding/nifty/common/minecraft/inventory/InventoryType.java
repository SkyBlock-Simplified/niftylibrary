package net.netcoding.nifty.common.minecraft.inventory;

public enum InventoryType {

	ANVIL(3, "Repairing"),
	BEACON(1, "container.beacon"),
	BREWING(5, "Brewing"),
	CHEST(27, "Chest"),
	CRAFTING(5, "Crafting"),
	CREATIVE(9, "Creative"),
	DISPENSER(9, "Dispenser"),
	DROPPER(9, "Dropper"),
	ENCHANTING(2, "Enchanting"),
	ENDER_CHEST(27, "Ender Chest"),
	FURNACE(3, "Furnace"),
	HOPPER(5, "Item Hopper"),
	MERCHANT(3, "Villager"),
	PLAYER(41, "Player"),
	WORKBENCH(10, "Crafting");

	private final int size;
	private final String title;

	InventoryType(int defaultSize, String defaultTitle) {
		this.size = defaultSize;
		this.title = defaultTitle;
	}

	public int getDefaultSize() {
		return this.size;
	}

	public String getDefaultTitle() {
		return this.title;
	}

	public enum SlotType {

		RESULT,
		CRAFTING,
		ARMOR,
		CONTAINER,
		QUICKBAR,
		OUTSIDE,
		FUEL

	}

}