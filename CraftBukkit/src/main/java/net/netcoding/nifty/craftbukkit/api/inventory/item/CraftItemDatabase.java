package net.netcoding.nifty.craftbukkit.api.inventory.item;

import net.netcoding.nifty.common.api.inventory.item.ItemDatabase;
import net.netcoding.nifty.common.minecraft.inventory.item.ItemStack;
import org.bukkit.Material;

public final class CraftItemDatabase extends ItemDatabase {

	private static final CraftItemDatabase INSTANCE = new CraftItemDatabase();

	private CraftItemDatabase() { }

	public org.bukkit.inventory.ItemStack getBukkit(String id) {
		ItemStack item = this.get(id);
		org.bukkit.inventory.ItemStack bukkitItem = new org.bukkit.inventory.ItemStack(Material.valueOf(item.getType().name()));
		bukkitItem.setAmount(item.getAmount());
		bukkitItem.setDurability(item.getDurability());
		return bukkitItem;
	}

	public static CraftItemDatabase getInstance() {
		return INSTANCE;
	}

}