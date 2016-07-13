package net.netcoding.nifty.craftbukkit.minecraft.inventory.item.enchantment;

import net.netcoding.nifty.common.minecraft.inventory.item.ItemStack;
import net.netcoding.nifty.common.minecraft.inventory.item.enchantment.Enchantment;
import net.netcoding.nifty.craftbukkit.minecraft.inventory.item.CraftItemStack;

@SuppressWarnings("deprecation")
public final class CraftEnchantment extends Enchantment {

	private final org.bukkit.enchantments.Enchantment enchantment;

	public CraftEnchantment(org.bukkit.enchantments.Enchantment enchantment) {
		super(enchantment.getId());
		this.enchantment = enchantment;
	}

	@Override
	public boolean canEnchant(ItemStack item) {
		return this.getHandle().canEnchantItem(((CraftItemStack)item).getHandle());
	}

	@Override
	public boolean conflictsWith(Enchantment enchantment) {
		return this.getHandle().conflictsWith(org.bukkit.enchantments.Enchantment.getByName(enchantment.getName()));
	}

	private org.bukkit.enchantments.Enchantment getHandle() {
		return this.enchantment;
	}

	@Override
	public int getMaxLevel() {
		return this.getHandle().getMaxLevel();
	}

}