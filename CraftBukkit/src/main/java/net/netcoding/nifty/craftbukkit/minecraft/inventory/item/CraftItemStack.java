package net.netcoding.nifty.craftbukkit.minecraft.inventory.item;

import com.google.common.base.Preconditions;
import net.netcoding.nifty.common.Nifty;
import net.netcoding.nifty.common.api.nbt.NbtCompound;
import net.netcoding.nifty.common.minecraft.inventory.ItemFlag;
import net.netcoding.nifty.common.minecraft.inventory.item.ItemStack;
import net.netcoding.nifty.common.minecraft.inventory.item.enchantment.Enchantment;
import net.netcoding.nifty.common.minecraft.inventory.item.meta.ItemMeta;
import net.netcoding.nifty.common.minecraft.material.Material;
import net.netcoding.nifty.common.minecraft.material.MaterialData;
import net.netcoding.nifty.core.util.ListUtil;
import net.netcoding.nifty.core.util.RegexUtil;
import net.netcoding.nifty.craftbukkit.api.nbt.CraftNbtFactory;
import net.netcoding.nifty.craftbukkit.minecraft.inventory.item.meta.CraftItemMeta;
import net.netcoding.nifty.craftbukkit.util.CraftConverter;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

@SuppressWarnings("deprecation")
public final class CraftItemStack implements ItemStack {

	private final org.bukkit.inventory.ItemStack bukkitItem;
	private NbtCompound root;
	private ItemMeta meta;

	private CraftItemStack() {
		this(new org.bukkit.inventory.ItemStack(org.bukkit.Material.AIR));
	}

	public CraftItemStack(org.bukkit.inventory.ItemStack bukkitItem) {
		this.bukkitItem = bukkitItem;

		if (Material.AIR != this.getType())
			this.initMeta(bukkitItem.getItemMeta());

		if (this.getAmount() <= 0)
			this.setAmount(1);

		this.initNbt();
	}

	@Override
	public void addEnchant(Enchantment enchantment, int level) {
		this.meta.addEnchant(enchantment, level);
	}

	@Override
	public void addUnsafeEnchant(Enchantment enchantment, int level) {
		this.meta.addEnchant(enchantment, level, true);
	}

	@Override
	public CraftItemStack clone() {
		CraftItemStack clone;

		try {
			clone = (CraftItemStack)super.clone();
		} catch (CloneNotSupportedException cnsex) {
			clone = new CraftItemStack(this.getHandle());
		}

		clone.getNbt().putAll(this.getNbt());
		clone.setGlowing(this.hasGlow());
		return clone;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		else if (!(obj instanceof ItemStack))
			return false;
		else {
			ItemStack stack = (ItemStack)obj;
			return this.getAmount() == stack.getAmount() && this.isSimilar(stack);
		}
	}

	@Override
	public int getAmount() {
		return this.getHandle().getAmount();
	}

	public org.bukkit.inventory.ItemStack getHandle() {
		return this.bukkitItem;
	}

	@Override
	public MaterialData getData() {
		return CraftConverter.fromBukkitData(this.getHandle().getData());
	}

	@Override
	public short getDurability() {
		return this.getHandle().getDurability();
	}

	@Override
	public int getEnchantLevel(Enchantment enchantment) {
		return this.meta.getEnchantLevel(enchantment);
	}

	@Override
	public Map<Enchantment, Integer> getEnchants() {
		return this.meta.getEnchants();
	}

	@Override
	public ItemMeta getItemMeta(boolean unformatted) {
		if (!this.hasItemMeta())
			return null;

		ItemMeta meta = this.meta.clone();
		List<String> lore = meta.getLore();

		if (unformatted && meta.hasDisplayName())
			meta.setDisplayName(RegexUtil.replace(meta.getDisplayName(), RegexUtil.VANILLA_PATTERN, "&$1"));

		if (ListUtil.isEmpty(lore))
			lore = new ArrayList<>();
		else {
			if (unformatted) {
				for (int i = 0; i < lore.size(); i++)
					lore.set(i, RegexUtil.replace(lore.get(i), RegexUtil.VANILLA_PATTERN, "&$1"));
			}
		}

		meta.setLore(lore);
		return meta;
	}

	@Override
	public NbtCompound getNbt() {
		return this.root;
	}

	@Override
	public int getTypeId() {
		return this.getHandle().getTypeId();
	}

	@Override
	public boolean hasEnchant(Enchantment enchantment) {
		return this.meta.hasEnchant(enchantment);
	}

	@Override
	public boolean hasItemMeta() {
		return !Nifty.getItemFactory().equals(this.meta, null);
	}

	@Override
	public int hashCode() {
		return this.getHandle().hashCode();
	}

	private void initMeta(org.bukkit.inventory.meta.ItemMeta old) {
		ItemMeta factory = Nifty.getItemFactory().getItemMeta(this.getType());

		if (factory != null)
			this.setItemMeta0(factory);

		if (this.hasItemMeta() && old != null) {
			org.bukkit.inventory.meta.ItemMeta bukkitMeta = bukkitItem.getItemMeta();
			this.meta.setDisplayName(bukkitMeta.getDisplayName());
			this.meta.setLore(bukkitMeta.getLore());
			bukkitMeta.getItemFlags().forEach(flag -> this.meta.addItemFlags(ItemFlag.valueOf(flag.name())));
			bukkitMeta.getEnchants().forEach((enchant, level) -> this.meta.addEnchant(Enchantment.getByName(enchant.getName()), level));
		}
	}

	private void initNbt() {
		NbtCompound compound;

		if (org.bukkit.Material.AIR != CraftNbtFactory.getCraftItemStack(this.getHandle()).getType())
			compound = Nifty.getNbtFactory().fromItemTag(this);
		else
			compound = Nifty.getNbtFactory().createCompound();

		if (this.root != null)
			compound.putAll(this.root);

		this.root = compound;
	}

	@Override
	public int removeEnchant(Enchantment enchantment) {
		return 0;
	}

	@Override
	public void setAmount(int amount) {
		this.getHandle().setAmount(amount);
	}

	@Override
	public void setData(MaterialData data) {
		this.getHandle().setData(CraftConverter.toBukkitData(data));
	}

	@Override
	public void setDurability(short durability) {
		this.getHandle().setDurability(durability);
	}

	@Override
	public boolean setItemMeta(ItemMeta meta) {
		if (meta == null)
			meta = Nifty.getItemFactory().getItemMeta(this.getType());
		else if (!Nifty.getItemFactory().isApplicable(meta, this.getType()))
			return false;
		else
			meta = meta.clone();

		if (meta.hasDisplayName())
			meta.setDisplayName(RegexUtil.replaceColor(meta.getDisplayName(), RegexUtil.REPLACE_ALL_PATTERN));

		if (ListUtil.isEmpty(meta.getLore()))
			meta.setLore(new ArrayList<>());

		List<String> lore = meta.getLore();

		for (int i = 0; i < lore.size(); i++)
			lore.set(i, RegexUtil.replaceColor(lore.get(i), RegexUtil.REPLACE_ALL_PATTERN));

		meta.setLore(lore);
		this.setItemMeta0(meta);
		return true;
	}

	private void setItemMeta0(ItemMeta meta) {
		this.meta = meta;
		this.bukkitItem.setItemMeta(((CraftItemMeta)this.meta).getHandle());
	}

	@Override
	public void setType(Material material) {
		Preconditions.checkArgument(material != null, "Type cannot be NULL!");
		this.getHandle().setType(org.bukkit.Material.valueOf(material.name()));
	}

	@Override
	public void setTypeId(int type, boolean initNbt) {
		int id = this.getTypeId();
		this.getHandle().setTypeId(type);

		if (this.getTypeId() != id)
			this.initMeta(((CraftItemMeta)this.meta).getHandle());

		if (initNbt)
			this.initNbt();
	}

	@Override
	public String toString() {
		return this.getHandle().toString();
	}

	public static final class Builder implements ItemStack.Builder {

		private final CraftItemStack item = new CraftItemStack();

		@Override
		public Builder amount(int amount) {
			this.item.setAmount(amount);
			return this;
		}

		@Override
		public ItemStack build() {
			return this.item;
		}

		@Override
		public Builder clearEnchants() {
			this.item.getItemMeta().getEnchants().clear();
			return this;
		}

		@Override
		public Builder clearLore() {
			ItemMeta meta = this.item.getItemMeta();
			meta.getLore().clear();
			this.item.setItemMeta(meta);
			return this;
		}

		@Override
		public Builder clearNbt() {
			this.item.getNbt().clear();
			return this;
		}

		@Override
		public Builder durability(short durability) {
			this.item.setDurability(durability);
			return this;
		}

		@Override
		public Builder enchant(Enchantment enchantment, int level) {
			this.item.addEnchant(enchantment, level);
			return this;
		}

		@Override
		public Builder fromItem(ItemStack item) {
			this.item.setTypeId(item.getTypeId());
			this.item.setDurability(item.getDurability());
			this.item.setAmount(item.getAmount());
			this.item.setData(item.getData().clone());
			this.item.setItemMeta(item.getItemMeta());
			return this;
		}

		@Override
		public Builder glow(boolean value) {
			this.item.setGlowing(value);
			return this;
		}

		@Override
		public Builder lore(Collection<String> lore) {
			ItemMeta meta = this.item.getItemMeta();
			List<String> metaLore = meta.getLore();
			metaLore.addAll(lore);
			this.item.setItemMeta(meta);
			return this;
		}

		@Override
		public Builder name(String displayName) {
			ItemMeta meta = this.item.getItemMeta();
			meta.setDisplayName(displayName);
			this.item.setItemMeta(meta);
			return this;
		}

		@Override
		public Builder nbt(String key, Object value) {
			this.item.getNbt().put(key, value);
			return this;
		}

		@Override
		public Builder nbtPath(String path, Object value) {
			this.item.getNbt().putPath(path, value);
			return this;
		}

		@Override
		public Builder type(int id, boolean initNbt) {
			this.item.setTypeId(id, initNbt);
			return this;
		}

	}

}