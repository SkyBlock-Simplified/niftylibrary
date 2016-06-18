package net.netcoding.niftycraftbukkit.api.inventory.item.enchantment;

import net.netcoding.niftybukkit._new_.api.inventory.item.enchantment.EnchantmentDatabase;
import net.netcoding.niftybukkit._new_.minecraft.inventory.item.enchantment.Enchantment;
import net.netcoding.niftycraftbukkit.minecraft.inventory.item.enchantment.CraftEnchantment;

public final class CraftEnchantmentDatabase extends EnchantmentDatabase {

	private static CraftEnchantmentDatabase INSTANCE;

	private CraftEnchantmentDatabase() { }

	public static CraftEnchantmentDatabase getInstance() {
		if (INSTANCE == null)
			INSTANCE = new CraftEnchantmentDatabase();

		return INSTANCE;
	}

	@Override
	protected final void registerEnchantments() {
		for (org.bukkit.enchantments.Enchantment enchantment : org.bukkit.enchantments.Enchantment.values())
			Enchantment.registerEnchantment(new CraftEnchantment(enchantment));
	}

}