package net.netcoding.nifty.common.minecraft.inventory.item;

import com.google.common.base.Preconditions;
import net.netcoding.nifty.common.Nifty;
import net.netcoding.nifty.common.api.nbt.NbtCompound;
import net.netcoding.nifty.common.minecraft.inventory.ItemFlag;
import net.netcoding.nifty.common.minecraft.inventory.item.enchantment.Enchantment;
import net.netcoding.nifty.common.minecraft.inventory.item.meta.ItemMeta;
import net.netcoding.nifty.common.minecraft.material.Material;
import net.netcoding.nifty.common.minecraft.material.MaterialData;
import net.netcoding.nifty.common.reflection.MinecraftProtocol;
import net.netcoding.nifty.core.api.builder.BuilderCore;
import net.netcoding.nifty.core.util.NumberUtil;
import net.netcoding.nifty.core.util.concurrent.Concurrent;
import net.netcoding.nifty.core.util.concurrent.ConcurrentMap;
import net.netcoding.nifty.core.util.misc.Serializable;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

public interface ItemStack extends Cloneable, Serializable {

	void addEnchant(Enchantment enchantment, int level);

	default void addEnchants(Map<Enchantment, Integer> enchantments) {
		Preconditions.checkArgument(enchantments != null, "Enchantments cannot be NULL!");

		for (Map.Entry<Enchantment, Integer> entry : enchantments.entrySet())
			this.addEnchant(entry.getKey(), entry.getValue());
	}

	default void addGlow() {
		if (this.hasGlow())
			return;

		if (MinecraftProtocol.isPost1_7())
			this.addUnsafeEnchant(Enchantment.DURABILITY, 0);

		if (MinecraftProtocol.isPre1_8())
			this.getNbt().put("ench", Nifty.getNbtFactory().createList());
		else {
			int enchants = 1;

			if (this.getNbt().containsKey("HideFlags"))
				enchants |= this.getNbt().<Integer>get("HideFlags");

			this.getNbt().put("HideFlags", enchants);
			ItemMeta meta = this.getItemMeta();

			if (!meta.hasItemFlag(ItemFlag.HIDE_ENCHANTS))
				meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);

			this.setItemMeta(meta);
		}
	}

	void addUnsafeEnchant(Enchantment enchantment, int level);

	default void addUnsafeEnchants(Map<Enchantment, Integer> enchantments) {
		Preconditions.checkArgument(enchantments != null, "Enchantments cannot be NULL!");

		for (Map.Entry<Enchantment, Integer> entry : enchantments.entrySet())
			this.addUnsafeEnchant(entry.getKey(), entry.getValue());
	}

	static Builder builder() {
		return Nifty.getBuilderManager().createBuilder(ItemStack.class);
	}

	ItemStack clone();

	@SuppressWarnings("unchecked")
	static ItemStack deserialize(Map<String, Object> map) {
		Material type = Material.getMaterial((String)map.get("type"));
		short damage = 0;
		int amount = 1;

		if (map.containsKey("damage"))
			damage = NumberUtil.to(map.get("damage"), Short.class);

		if (map.containsKey("amount"))
			amount = NumberUtil.to(map.get("damage"), Integer.class);

		ItemStack result = ItemStack.of(type, amount, damage);

		if (map.get("meta") instanceof Map) {
			Map<String, Object> meta = (Map<String, Object>)map.get("meta");
			ItemMeta itemMeta = result.getItemMeta();
			itemMeta.setDisplayName(meta.get("displayName").toString());
			itemMeta.setLore((List<String>)meta.get("lore"));

			if (meta.containsKey("enchants")) {
				Map<String, Integer> enchants = (Map<String, Integer>)map.get("enchants");
				enchants.entrySet().stream().forEach(entry -> result.addUnsafeEnchant(Enchantment.getByName(entry.getKey()), entry.getValue()));
			}

			if (meta.containsKey("flags"))
				itemMeta.addItemFlags((Set<ItemFlag>)meta.get("flags"));
		}

		return result;
	}

	int getAmount();

	MaterialData getData();

	short getDurability();

	int getEnchantLevel(Enchantment enchantment);

	Map<Enchantment, Integer> getEnchants();

	default ItemMeta getItemMeta() {
		return this.getItemMeta(false);
	}

	ItemMeta getItemMeta(boolean unformatted);

	default int getMaxStackSize() {
		Material material = this.getType();
		return (material != null ? material.getMaxStackSize() : -1);
	}

	NbtCompound getNbt();

	default Material getType() {
		return Material.getMaterial(this.getTypeId());
	}

	int getTypeId();

	boolean hasEnchant(Enchantment enchantment);

	default boolean hasGlow() {
		if (MinecraftProtocol.isPost1_7()) {
			if (this.getEnchants().containsKey(Enchantment.DURABILITY)) {
				if (this.getEnchants().get(Enchantment.DURABILITY) == 0) {
					if (this.getNbt().containsKey("HideFlags")) {
						if (this.getItemMeta().hasItemFlag(ItemFlag.HIDE_ENCHANTS))
							return true;
					}
				}
			}
		} else {
			if (this.getNbt().containsKey("ench"))
				return true;
		}

		return false;
	}

	boolean hasItemMeta();

	@SuppressWarnings("ObjectEquality")
	default boolean isSimilar(ItemStack stack) {
		if (this == stack)
			return true;
		else if (stack != null) {
			if (this.getTypeId() == stack.getTypeId()) {
				if (this.getDurability() == stack.getDurability()) {
					if (this.hasItemMeta() == stack.hasItemMeta())
						return !this.hasItemMeta() || Nifty.getItemFactory().equals(this.getItemMeta(), stack.getItemMeta());
				}
			}
		}

		return false;
	}

	static ItemStack of(int id) {
		return of(id, 1);
	}

	static ItemStack of(int id, int amount) {
		return of(id, amount, (short)0);
	}

	static ItemStack of(int id, int amount, short durability) {
		return of(Material.getMaterial(id), amount, durability);
	}

	static ItemStack of(Material material) {
		return of(material, 1);
	}

	static ItemStack of(Material material, int amount) {
		return of(material, amount, (short)0);
	}

	static ItemStack of(Material material, int amount, short durability) {
		return builder().type(material).amount(amount).durability(durability).build();
	}

	static ItemStack of(ItemStack item) {
		return builder().fromItem(item).build();
	}

	int removeEnchant(Enchantment enchantment);

	default void removeGlow() {
		if (!this.hasGlow())
			return;

		if (MinecraftProtocol.isPost1_7())
			this.removeEnchant(Enchantment.DURABILITY);

		if (MinecraftProtocol.isPre1_8())
			this.getNbt().remove("ench");
		else {
			this.getNbt().remove("HideFlags");
			ItemMeta meta = this.getItemMeta();

			if (meta.hasItemFlag(ItemFlag.HIDE_ENCHANTS))
				meta.removeItemFlags(ItemFlag.HIDE_ENCHANTS);

			this.setItemMeta(meta);
		}
	}

	@Override
	default Map<String, Object> serialize() {
		ConcurrentMap<String, Object> result = Concurrent.newMap();
		result.put("id", this.getType().name() + (this.getDurability() > 0 ? ":" + this.getDurability() : ""));
		result.put("amount", this.getAmount());
		result.put("meta", this.getItemMeta().serialize());
		return result;
	}

	void setAmount(int amount);

	void setData(MaterialData data);

	void setDurability(short durability);

	default void setGlowing() {
		this.setGlowing(true);
	}

	default void setGlowing(boolean value) {
		if (value)
			this.addGlow();
		else
			this.removeGlow();
	}

	boolean setItemMeta(ItemMeta meta);

	void setType(Material material);

	void setTypeId(int type);

	interface Builder extends BuilderCore<ItemStack> {

		Builder amount(int amount);

		Builder clearEnchants();

		Builder clearLore();

		Builder clearNbt();

		Builder durability(short durability);

		Builder enchant(Enchantment enchantment, int level);

		Builder fromItem(ItemStack item);

		Builder glow(boolean value);

		default Builder lore(String... lore) {
			return this.lore(Arrays.asList(lore));
		}

		Builder lore(Collection<String> lore);

		Builder name(String displayName);

		Builder nbt(String key, Object value);

		Builder nbtPath(String path, Object value);

		Builder type(Material material);

		Builder type(int id);

	}

}