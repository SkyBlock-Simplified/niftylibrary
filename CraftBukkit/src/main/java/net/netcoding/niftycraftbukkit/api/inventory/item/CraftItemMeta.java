package net.netcoding.niftycraftbukkit.api.inventory.item;

import net.netcoding.niftybukkit._new_.minecraft.inventory.ItemFlag;
import net.netcoding.niftybukkit._new_.minecraft.inventory.item.enchantment.Enchantment;
import net.netcoding.niftybukkit._new_.minecraft.inventory.item.meta.ItemMeta;
import net.netcoding.niftycore.util.ListUtil;
import net.netcoding.niftycore.util.concurrent.ConcurrentMap;
import net.netcoding.niftycore.util.concurrent.ConcurrentSet;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class CraftItemMeta implements ItemMeta {

	private final org.bukkit.inventory.meta.ItemMeta bukkitMeta;

	public CraftItemMeta(org.bukkit.inventory.meta.ItemMeta bukkitMeta) {
		this.bukkitMeta = bukkitMeta;
	}

	@Override
	public boolean addEnchant(Enchantment enchantment, int level, boolean ignoreLevelRestriction) {
		return this.getBukkitMeta().addEnchant(org.bukkit.enchantments.Enchantment.getByName(enchantment.getName()), level, ignoreLevelRestriction);
	}

	@Override
	public void addItemFlags(Collection<? extends ItemFlag> flags) {
		for (ItemFlag flag : flags)
			this.getBukkitMeta().addItemFlags(org.bukkit.inventory.ItemFlag.valueOf(flag.name()));
	}

	@SuppressWarnings("CloneDoesntCallSuperClone")
	@Override
	public ItemMeta clone() {
		return new CraftItemMeta(this.getBukkitMeta().clone());
	}

	public final org.bukkit.inventory.meta.ItemMeta getBukkitMeta() {
		return this.bukkitMeta;
	}

	@Override
	public String getDisplayName() {
		return this.getBukkitMeta().getDisplayName();
	}

	@Override
	public Map<Enchantment, Integer> getEnchants() {
		ConcurrentMap<Enchantment, Integer> enchants = new ConcurrentMap<>();
		this.getBukkitMeta().getEnchants().entrySet().stream().forEach(entry -> enchants.put(Enchantment.getByName(entry.getKey().getName()), entry.getValue()));
		return enchants;
	}

	@Override
	public Set<ItemFlag> getItemFlags() {
		ConcurrentSet<ItemFlag> flags = new ConcurrentSet<>();
		this.getBukkitMeta().getItemFlags().stream().forEach(flag -> flags.add(ItemFlag.valueOf(flag.name())));
		return flags;
	}

	@Override
	public List<String> getLore() {
		return this.getBukkitMeta().getLore();
	}

	@Override
	public boolean removeEnchant(Enchantment enchantment) {
		return this.getBukkitMeta().removeEnchant(org.bukkit.enchantments.Enchantment.getByName(enchantment.getName()));
	}

	@Override
	public void removeItemFlags(ItemFlag... flags) {
		for (ItemFlag flag : flags)
			this.getBukkitMeta().removeItemFlags(org.bukkit.inventory.ItemFlag.valueOf(flag.name()));
	}

	@Override
	public void setDisplayName(String displayName) {
		this.getBukkitMeta().setDisplayName(displayName);
	}

	@Override
	public void setLore(List<String> lore) {
		this.getBukkitMeta().setLore(ListUtil.isEmpty(lore) ? new ArrayList<>() : lore);
	}

	@Override
	public final String toString() {
		return this.getBukkitMeta().toString();
	}

}