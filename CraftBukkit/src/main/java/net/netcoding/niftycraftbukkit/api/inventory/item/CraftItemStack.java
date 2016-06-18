package net.netcoding.niftycraftbukkit.api.inventory.item;

import com.google.common.base.Preconditions;
import net.netcoding.niftybukkit.Nifty;
import net.netcoding.niftybukkit._new_.api.nbt.NbtCompound;
import net.netcoding.niftybukkit._new_.minecraft.inventory.item.ItemStack;
import net.netcoding.niftybukkit._new_.minecraft.inventory.item.enchantment.Enchantment;
import net.netcoding.niftybukkit._new_.minecraft.inventory.item.meta.ItemMeta;
import net.netcoding.niftybukkit._new_.minecraft.material.Material;
import net.netcoding.niftybukkit._new_.minecraft.material.MaterialData;
import net.netcoding.niftycore.util.ListUtil;
import net.netcoding.niftycore.util.RegexUtil;
import net.netcoding.niftycore.util.StringUtil;
import net.netcoding.niftycraftbukkit.api.nbt.CraftNbtFactory;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

@SuppressWarnings("deprecation")
public final class CraftItemStack implements ItemStack {

	private final org.bukkit.inventory.ItemStack bukkitItem;
	private final NbtCompound root;
	private ItemMeta meta;
	private MaterialData data; // TODO

	private CraftItemStack() {
		this(new org.bukkit.inventory.ItemStack(org.bukkit.Material.AIR));
	}

	public CraftItemStack(org.bukkit.inventory.ItemStack bukkitItem) {
		this.bukkitItem = bukkitItem;
		this.setItemMeta(new CraftItemMeta(this.bukkitItem.getItemMeta())); // TODO

		if (this.getAmount() < 0)
			this.setAmount(1);

		if (org.bukkit.Material.AIR != CraftNbtFactory.getCraftItemStack(this.getBukkitItem()).getType())
			this.root = Nifty.getNbtFactory().fromItemTag(this);
		else
			this.root = Nifty.getNbtFactory().createCompound();
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
		return this.getBukkitItem().getAmount();
	}

	public org.bukkit.inventory.ItemStack getBukkitItem() {
		return this.bukkitItem;
	}

	@Override
	public MaterialData getData() {
		return this.data;
	}

	@Override
	public short getDurability() {
		return this.getBukkitItem().getDurability();
	}

	@Override
	public int getEnchantLevel(Enchantment enchantment) {
		return this.meta.getEnchantLevel(enchantment);
	}

	@Override
	public Map<Enchantment, Integer> getEnchants() {
		return this.meta.getEnchants();
	}

	public ItemMeta getItemMeta(boolean unformatted) {
		ItemMeta meta = this.meta;

		if (Nifty.getItemFactory().equals(meta, null)) {
			ItemMeta factory = Nifty.getItemFactory().getItemMeta(this.getType());

			if (factory != null)
				this.setItemMeta(meta = factory.clone());
		}

		if (meta != null) {
			meta = meta.clone();

			if (unformatted) {
				if (meta.hasDisplayName())
					meta.setDisplayName(RegexUtil.replace(meta.getDisplayName(), RegexUtil.VANILLA_PATTERN, "&$1"));
			}

			List<String> lore = meta.getLore();

			if (ListUtil.isEmpty(lore))
				lore = new ArrayList<>();
			else {
				if (unformatted) {
					for (int i = 0; i < lore.size(); i++)
						lore.set(i, RegexUtil.replace(lore.get(i), RegexUtil.VANILLA_PATTERN, "&$1"));
				}
			}

			meta.setLore(lore);
		}

		return meta;
	}

	@Override
	public NbtCompound getNbt() {
		return this.root;
	}

	@Override
	public int getTypeId() {
		return this.getBukkitItem().getTypeId();
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
		byte hash = 1;
		int hash1 = hash * 31 + this.getTypeId();
		hash1 = hash1 * 31 + this.getAmount();
		hash1 = hash1 * 31 + this.getDurability();
		hash1 = hash1 * 31 + this.getItemMeta().hashCode();
		return hash1;
	}

	@Override
	public int removeEnchant(Enchantment enchantment) {
		return 0;
	}

	@Override
	public void setAmount(int amount) {
		this.getBukkitItem().setAmount(amount);
	}

	@Override
	public void setData(MaterialData data) {
		Material mat = this.getType();

		if (data != null && mat != null && mat.getData() != null) {
			if (!data.getClass().equals(mat.getData()) && !MaterialData.class.isAssignableFrom(data.getClass()))
				throw new IllegalArgumentException(StringUtil.format("Provided data is not of type {0}, found {1}!", mat.getData().getName(), data.getClass().getName()));
		}

		this.data = data;
		this.getBukkitItem().setData(new org.bukkit.material.MaterialData(data.getItemTypeId(), data.getData()));
	}

	@Override
	public void setDurability(short durability) {
		this.getBukkitItem().setDurability(durability);
	}

	@SuppressWarnings("ObjectEquality")
	@Override
	public boolean setItemMeta(ItemMeta itemMeta) {
		if (itemMeta == null)
			itemMeta = Nifty.getItemFactory().getItemMeta(this.getType());
		else if (!Nifty.getItemFactory().isApplicable(itemMeta, this.getType()))
			return false;
		else {
			itemMeta = Nifty.getItemFactory().asMetaFor(itemMeta, this.getType());

			if (itemMeta == itemMeta)
				itemMeta = itemMeta.clone();
		}

		if (itemMeta.hasDisplayName())
			itemMeta.setDisplayName(RegexUtil.replaceColor(itemMeta.getDisplayName(), RegexUtil.REPLACE_ALL_PATTERN));

		if (ListUtil.isEmpty(itemMeta.getLore()))
			itemMeta.setLore(new ArrayList<>());

		List<String> lore = itemMeta.getLore();

		for (int i = 0; i < lore.size(); i++)
			lore.set(i, RegexUtil.replaceColor(lore.get(i), RegexUtil.REPLACE_ALL_PATTERN));

		itemMeta.setLore(lore);
		this.meta = itemMeta;
		return true;
	}

	@Override
	public void setType(Material material) {
		Preconditions.checkArgument(material != null, "Type cannot be NULL!");
		this.getBukkitItem().setType(org.bukkit.Material.valueOf(material.name()));
	}

	@Override
	public void setTypeId(int type) {
		this.getBukkitItem().setTypeId(type);
	}

	@Override
	public String toString() {
		return this.getBukkitItem().toString(); // TODO: Nbt Output
	}

	public static final class Builder implements ItemStack.Builder {

		private final CraftItemStack item = new CraftItemStack();

		@Override
		public Builder amount(int amount) {
			this.item.setAmount(amount);
			return this;
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
		public Builder fromItemStack(ItemStack item) {
			this.item.setTypeId(item.getTypeId());
			this.item.setDurability(item.getDurability());
			this.item.setAmount(item.getAmount());
			this.item.setData(item.getData().clone());
			this.item.setItemMeta(item.getItemMeta());
			return this;
		}

		@Override
		public ItemStack build() {
			return this.item;
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
		public Builder type(Material material) {
			this.item.setType(material);
			return this;
		}

		@Override
		public ItemStack.Builder type(int id) {
			this.item.setTypeId(id);
			return this;
		}

	}

}