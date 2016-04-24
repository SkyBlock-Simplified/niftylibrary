package net.netcoding.niftybukkit.minecraft.items.enchantments;

import net.netcoding.niftybukkit.NiftyBukkit;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;

import java.util.List;

@SuppressWarnings("deprecation")
public class EnchantmentData {

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
			stack.addUnsafeEnchantment(this.getEnchantment(), level);
		else {
			level = (level < this.getStartLevel() ? this.getStartLevel() : level);
			level = (level > this.getMaxLevel() ? this.getMaxLevel() : level);
			stack.addEnchantment(this.getEnchantment(), level);
		}

		return stack.containsEnchantment(this.getEnchantment()) && stack.getEnchantmentLevel(this.getEnchantment()) == level;
	}

	public boolean canEnchant(ItemStack stack) {
		return !(stack == null || Material.AIR == stack.getType()) && this.getEnchantment().canEnchantItem(stack);
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
		return NiftyBukkit.getEnchantmentDatabase().names(this);
	}

	public int getUserLevel() {
		return this.userLevel;
	}

	public int getStartLevel() {
		return this.getEnchantment().getStartLevel();
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