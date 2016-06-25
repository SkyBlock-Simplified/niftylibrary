package net.netcoding.nifty.common.minecraft.inventory.item.meta;

import net.netcoding.nifty.common.minecraft.inventory.ItemFlag;
import net.netcoding.nifty.common.minecraft.inventory.item.enchantment.Enchantment;
import net.netcoding.nifty.core.util.ListUtil;
import net.netcoding.nifty.core.util.misc.Serializable;
import net.netcoding.nifty.core.util.StringUtil;
import net.netcoding.nifty.core.util.concurrent.ConcurrentMap;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

public interface ItemMeta extends Cloneable, Serializable {

	default boolean addEnchant(Enchantment enchantment, int level) {
		return this.addEnchant(enchantment, level, false);
	}

	boolean addEnchant(Enchantment enchantment, int level, boolean ignoreLevelRestriction);

	default void addItemFlags(ItemFlag... flags) {
		this.addItemFlags(Arrays.asList(flags));
	}

	void addItemFlags(Collection<? extends ItemFlag> flags);

	ItemMeta clone();

	String getDisplayName();

	default int getEnchantLevel(Enchantment enchantment) {
		return this.hasEnchant(enchantment) ? this.getEnchants().get(enchantment) : 0;
	}

	Map<Enchantment, Integer> getEnchants();

	Set<ItemFlag> getItemFlags();

	List<String> getLore();

	default boolean hasConflictingEnchant(Enchantment enchantment) {
		if (!this.getEnchants().isEmpty()) {
			for (Enchantment enchant : this.getEnchants().keySet()) {
				if (enchant.conflictsWith(enchantment))
					return true;
			}
		}

		return false;
	}

	default boolean hasDisplayName() {
		return StringUtil.notEmpty(this.getDisplayName());
	}

	default boolean hasEnchant(Enchantment enchantment) {
		return this.getEnchants().containsKey(enchantment);
	}

	default boolean hasEnchants() {
		return !this.getEnchants().isEmpty();
	}

	default boolean hasItemFlag(ItemFlag flag) {
		return this.getItemFlags().contains(flag);
	}

	default boolean hasLore() {
		return ListUtil.notEmpty(this.getLore());
	}

	boolean removeEnchant(Enchantment enchantment);

	void removeItemFlags(ItemFlag... flags);

	default Map<String, Object> serialize() {
		ConcurrentMap<String, Object> result = new ConcurrentMap<>();
		result.put("name", this.getDisplayName());
		result.put("lore", this.getLore());

		if (this.hasEnchants())
			result.put("enchants", this.getEnchants());

		if (!this.getItemFlags().isEmpty())
			result.put("flags", this.getItemFlags());

		return result;
	}

	void setDisplayName(String displayName);

	default void setLore(String... lore) {
		this.setLore(Arrays.asList(lore));
	}

	void setLore(List<String> lore);

}