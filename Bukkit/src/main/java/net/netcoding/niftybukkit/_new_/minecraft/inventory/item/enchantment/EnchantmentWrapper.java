package net.netcoding.niftybukkit._new_.minecraft.inventory.item.enchantment;

import net.netcoding.niftybukkit._new_.minecraft.inventory.item.ItemStack;

final class EnchantmentWrapper extends Enchantment {

	private final String key;
	private final String name;
	private final EnchantmentTarget target;

	EnchantmentWrapper(int id, String key, String name, EnchantmentTarget target) {
		super(id);
		this.key = key;
		this.name = name;
		this.target = target;
		ORIGINAL.put(this.getId(), this);
	}

	public boolean canEnchant(ItemStack item) {
		return this.getEnchantment().canEnchant(item);
	}

	public boolean conflictsWith(Enchantment other) {
		return this.getEnchantment().conflictsWith(other);
	}

	private Enchantment getEnchantment() {
		return Enchantment.getById(this.getId());
	}

	public int getMaxLevel() {
		return this.getEnchantment().getMaxLevel();
	}

	@Override
	public String getName() {
		return this.name;
	}

	@Override
	public String getKey() {
		return this.key;
	}

	@Override
	public EnchantmentTarget getTarget() {
		return this.target;
	}

}