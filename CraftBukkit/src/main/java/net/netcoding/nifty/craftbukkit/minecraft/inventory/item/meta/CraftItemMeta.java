package net.netcoding.nifty.craftbukkit.minecraft.inventory.item.meta;

import net.netcoding.nifty.common.minecraft.inventory.ItemFlag;
import net.netcoding.nifty.common.minecraft.inventory.item.enchantment.Enchantment;
import net.netcoding.nifty.common.minecraft.inventory.item.meta.ItemMeta;
import net.netcoding.nifty.core.util.ListUtil;
import net.netcoding.nifty.core.util.concurrent.Concurrent;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class CraftItemMeta implements ItemMeta {

	private final org.bukkit.inventory.meta.ItemMeta bukkitMeta;

	public CraftItemMeta(CraftItemMeta craftMeta) {
		this(craftMeta.getHandle());
	}

	public CraftItemMeta(org.bukkit.inventory.meta.ItemMeta bukkitMeta) {
		this.bukkitMeta = bukkitMeta;
	}

	@Override
	public boolean addEnchant(Enchantment enchantment, int level, boolean ignoreLevelRestriction) {
		return this.getHandle().addEnchant(org.bukkit.enchantments.Enchantment.getByName(enchantment.getName()), level, ignoreLevelRestriction);
	}

	@Override
	public void addItemFlags(Collection<? extends ItemFlag> flags) {
		flags.stream().forEach(flag -> this.getHandle().addItemFlags(org.bukkit.inventory.ItemFlag.valueOf(flag.name())));
	}

	@Override
	public ItemMeta clone() {
		return new CraftItemMeta(this.getHandle().clone());
	}

	public org.bukkit.inventory.meta.ItemMeta getHandle() {
		return this.bukkitMeta;
	}

	@Override
	public String getDisplayName() {
		return this.getHandle().getDisplayName();
	}

	@Override
	public Map<Enchantment, Integer> getEnchants() {
		return this.getHandle().getEnchants().entrySet().stream().collect(Concurrent.toMap(entry -> Enchantment.getByName(entry.getKey().getName()), Map.Entry::getValue));
	}

	@Override
	public Set<ItemFlag> getItemFlags() {
		return this.getHandle().getItemFlags().stream().map(flag -> ItemFlag.valueOf(flag.name())).collect(Concurrent.toSet());
	}

	@Override
	public List<String> getLore() {
		return this.getHandle().getLore();
	}

	@Override
	public boolean removeEnchant(Enchantment enchantment) {
		return this.getHandle().removeEnchant(org.bukkit.enchantments.Enchantment.getByName(enchantment.getName()));
	}

	@Override
	public void removeItemFlags(Collection<? extends ItemFlag> flags) {
		flags.stream().forEach(flag -> this.getHandle().removeItemFlags(org.bukkit.inventory.ItemFlag.valueOf(flag.name())));
	}

	@Override
	public void setDisplayName(String displayName) {
		this.getHandle().setDisplayName(displayName);
	}

	@Override
	public void setLore(List<String> lore) {
		this.getHandle().setLore(ListUtil.isEmpty(lore) ? new ArrayList<>() : lore);
	}

	@Override
	public final String toString() {
		return this.getHandle().toString();
	}

}