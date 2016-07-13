package net.netcoding.nifty.craftbukkit.minecraft.inventory.item.meta;

import net.netcoding.nifty.common.minecraft.inventory.item.enchantment.Enchantment;
import net.netcoding.nifty.common.minecraft.inventory.item.meta.EnchantmentBookMeta;
import net.netcoding.nifty.core.util.concurrent.Concurrent;
import net.netcoding.nifty.craftbukkit.util.CraftConverter;

import java.util.Map;

public final class CraftEnchantedBookMeta extends CraftItemMeta implements EnchantmentBookMeta {

	public CraftEnchantedBookMeta(org.bukkit.inventory.meta.EnchantmentStorageMeta enchantmentStorageMeta) {
		super(enchantmentStorageMeta);
	}

	@Override
	public boolean addStoredEnchant(Enchantment enchantment, int level, boolean ignoreLevelRestriction) {
		return this.getHandle().addStoredEnchant(CraftConverter.toBukkitEnchant(enchantment), level, ignoreLevelRestriction);
	}

	@Override
	public EnchantmentBookMeta clone() {
		return new CraftEnchantedBookMeta(this.getHandle().clone());
	}

	@Override
	public org.bukkit.inventory.meta.EnchantmentStorageMeta getHandle() {
		return (org.bukkit.inventory.meta.EnchantmentStorageMeta)super.getHandle();
	}

	@Override
	public Map<Enchantment, Integer> getStoredEnchants() {
		return this.getHandle().getStoredEnchants().entrySet().stream().collect(Concurrent.toMap(entry -> CraftConverter.fromBukkitEnchant(entry.getKey()), Map.Entry::getValue));
	}

	@Override
	public boolean removeStoredEnchant(Enchantment enchantment) {
		return this.getHandle().removeStoredEnchant(CraftConverter.toBukkitEnchant(enchantment));
	}

}