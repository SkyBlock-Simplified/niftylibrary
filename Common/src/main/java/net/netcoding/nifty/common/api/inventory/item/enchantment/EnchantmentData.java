package net.netcoding.nifty.common.api.inventory.item.enchantment;

import net.netcoding.nifty.common.minecraft.inventory.item.ItemStack;
import net.netcoding.nifty.common.minecraft.inventory.item.enchantment.Enchantment;
import net.netcoding.nifty.common.minecraft.material.Material;
import net.netcoding.nifty.common.Nifty;

import java.util.List;

public final class EnchantmentData {

	private final Enchantment enchantment;
	private int userLevel = 1;

	public EnchantmentData(Enchantment enchantment) {
		this.enchantment = enchantment;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null) return false;
		if (obj == this) return true;
		if (!(obj instanceof EnchantmentData)) return false;
		EnchantmentData detail = (EnchantmentData)obj;
		return detail.getEnchantment().equals(this.getEnchantment());
	}

	public boolean apply(ItemStack stack) {
		return this.apply(stack, false);
	}

	public boolean apply(ItemStack stack, boolean unsafe) {
		int level = this.getUserLevel();

		if (unsafe)
			stack.addUnsafeEnchant(this.getEnchantment(), level);
		else {
			level = (level < this.getStartLevel() ? this.getStartLevel() : level);
			level = (level > this.getMaxLevel() ? this.getMaxLevel() : level);
			stack.addEnchant(this.getEnchantment(), level);
		}

		return stack.hasEnchant(this.getEnchantment()) && stack.getEnchantLevel(this.getEnchantment()) == level;
	}

	public boolean canEnchant(ItemStack stack) {
		return !(stack == null || Material.AIR == stack.getType()) && this.getEnchantment().canEnchant(stack);
	}

	public Enchantment getEnchantment() {
		return this.enchantment;
	}

	public int getId() {
		return this.getEnchantment().getId();
	}

	public int getMaxLevel() {
		return this.getEnchantment().getMaxLevel();
	}

	public String getName() {
		return this.getEnchantment().getName();
	}

	public List<String> getNames() {
		return Nifty.getEnchantmentDatabase().names(this);
	}

	public int getStartLevel() {
		return this.getEnchantment().getStartLevel();
	}

	public int getUserLevel() {
		return this.userLevel;
	}

	@Override
	public int hashCode() {
		return this.getEnchantment().hashCode();
	}

	public void setUserLevel(int value) {
		value = (value < Short.MIN_VALUE ? Short.MIN_VALUE : value);
		value = (value > Short.MAX_VALUE ? Short.MAX_VALUE : value);
		this.userLevel = value;
	}

}