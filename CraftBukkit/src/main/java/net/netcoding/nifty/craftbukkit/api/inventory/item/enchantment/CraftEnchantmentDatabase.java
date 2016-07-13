package net.netcoding.nifty.craftbukkit.api.inventory.item.enchantment;

import net.netcoding.nifty.common.api.inventory.item.enchantment.EnchantmentDatabase;
import net.netcoding.nifty.common.minecraft.inventory.item.enchantment.Enchantment;
import net.netcoding.nifty.craftbukkit.minecraft.inventory.item.enchantment.CraftEnchantment;

public final class CraftEnchantmentDatabase extends EnchantmentDatabase {

	private static final CraftEnchantmentDatabase INSTANCE = new CraftEnchantmentDatabase();

	private CraftEnchantmentDatabase() { }

	public static CraftEnchantmentDatabase getInstance() {
		return INSTANCE;
	}

	@Override
	protected final void registerEnchantments() {
		for (org.bukkit.enchantments.Enchantment enchantment : org.bukkit.enchantments.Enchantment.values())
			Enchantment.registerEnchantment(new CraftEnchantment(enchantment));
	}

}