package net.netcoding.nifty.craftbukkit.minecraft.inventory;

import net.netcoding.nifty.common.minecraft.inventory.InventoryType;
import net.netcoding.nifty.core.util.StringUtil;
import net.netcoding.nifty.craftbukkit.minecraft.inventory.type.*;

public enum CraftInventoryType {

	ANVIL(InventoryType.ANVIL, CraftAnvilInventory.class, "org.bukkit.inventory.AnvilInventory"),
	BEACON(InventoryType.BEACON, CraftBeaconInventory.class, "org.bukkit.inventory.BeaconInventory"),
	BREWING(InventoryType.BREWING, CraftBrewerInventory.class, "org.bukkit.inventory.BrewerInventory"),
	CHEST(InventoryType.CHEST, CraftInventory.class, "org.bukkit.inventory.Inventory"),
	CRAFTING(InventoryType.CRAFTING, CraftCraftingInventory.class, "org.bukkit.inventory.CraftingInventory"),
	CREATIVE(InventoryType.CREATIVE, CraftPlayerInventory.class, "org.bukkit.inventory.PlayerInventory"), // ASSUMED
	DISPENSER(InventoryType.DISPENSER, CraftInventory.class, "org.bukkit.inventory.Inventory"),
	DOUBLE_CHEST(InventoryType.CHEST, CraftDoubleChestInventory.class, "org.bukkit.inventory.DoubleChestInventory"),
	DROPPER(InventoryType.DROPPER, CraftInventory.class, "org.bukkit.inventory.Inventory"),
	ENCHANTING(InventoryType.ENCHANTING, CraftEnchantingInventory.class, "org.bukkit.inventory.EnchantingInventory"),
	ENDER_CHEST(InventoryType.ENDER_CHEST, CraftInventory.class, "org.bukkit.inventory.Inventory"),
	FURNACE(InventoryType.FURNACE, CraftFurnaceInventory.class, "org.bukkit.inventory.FurnaceInventory"),
	HOPPER(InventoryType.HOPPER, CraftInventory.class, "org.bukkit.inventory.Inventory"),
	HORSE(InventoryType.CHEST, CraftHorseInventory.class, "org.bukkit.inventory.HorseInventory"),
	MERCHANT(InventoryType.MERCHANT, CraftMerchantInventory.class, "org.bukkit.inventory.MerchantInventory"),
	PLAYER(InventoryType.PLAYER, CraftPlayerInventory.class, "org.bukkit.inventory.PlayerInventory"),
	WORKBENCH(InventoryType.WORKBENCH, CraftCraftingInventory.class, "org.bukkit.inventory.CraftingInventory");

	private final InventoryType type;
	private final Class<? extends CraftInventory> clazz;
	private final Class<? extends org.bukkit.inventory.Inventory> bukkitClazz;

	@SuppressWarnings("unchecked")
	CraftInventoryType(InventoryType type, Class<? extends CraftInventory> clazz, String bukkitInventoryPath) {
		this.type = type;
		this.clazz = clazz;
		Class<? extends org.bukkit.inventory.Inventory> bukkitClazz = null;

		if (StringUtil.notEmpty(bukkitInventoryPath)) {
			try {
				bukkitClazz = (Class<? extends org.bukkit.inventory.Inventory>)Class.forName(bukkitInventoryPath);
			} catch (ClassNotFoundException ignore) { }
		}

		this.bukkitClazz = bukkitClazz;
	}

	public static CraftInventoryType getByBukkitClass(Class<? extends org.bukkit.inventory.Inventory> bukkitClazz) {
		for (CraftInventoryType type : values()) {
			if (type.getBukkitClass() == null)
				continue;

			if (type.getBukkitClass().isAssignableFrom(bukkitClazz))
				return type;
		}

		return CraftInventoryType.CHEST;
	}

	public Class<? extends org.bukkit.inventory.Inventory> getBukkitClass() {
		return this.bukkitClazz;
	}

	public Class<? extends CraftInventory> getInventoryClass() {
		return this.clazz;
	}

	public InventoryType getType() {
		return this.type;
	}

}