package net.netcoding.niftybukkit.inventory.items;

import net.netcoding.niftybukkit.NiftyBukkit;
import net.netcoding.niftybukkit.reflection.MinecraftPackage;
import net.netcoding.niftycore.reflection.Reflection;
import net.netcoding.niftycore.util.ListUtil;
import net.netcoding.niftycore.util.RegexUtil;
import net.netcoding.niftycore.util.concurrent.ConcurrentMap;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("deprecation")
public class ItemData extends ItemStack {

	private ConcurrentMap<String, Object> metadata = new ConcurrentMap<>();
	private boolean glow = false;

	public ItemData(ItemStack stack) {
		super(stack);

		if (this.getAmount() <= 0)
			this.setAmount(1);
	}

	public ItemData(int id) {
		this(id, (short)0);
	}

	public ItemData(int id, short durability) {
		this(Material.getMaterial(id), durability);
	}

	public ItemData(Material material) {
		this(material, (short)0);
	}

	public ItemData(Material material, short durability) {
		this(new ItemStack(material, 1, durability));
	}

	public void addGlow() {
		if (this.hasGlow())
			return;

		try {
			if (!MinecraftPackage.IS_PRE_1_8)
				this.addUnsafeEnchantment(Enchantment.DURABILITY, -1);

			Reflection craftItemStack = new Reflection("CraftItemStack", "inventory", MinecraftPackage.CRAFTBUKKIT);
			Reflection nmsItemStack = new Reflection("ItemStack", MinecraftPackage.MINECRAFT_SERVER);
			Reflection tagCompound = new Reflection("NBTTagCompound", MinecraftPackage.MINECRAFT_SERVER);
			Object nmsItem = craftItemStack.invokeMethod("asNMSCopy", null, this);
			Object tagObj = nmsItemStack.invokeMethod("getTag", nmsItem);

			if (tagObj == null) {
				tagObj = tagCompound.newInstance();
				nmsItemStack.invokeMethod("setTag", nmsItem, tagObj);
				tagObj = nmsItemStack.invokeMethod("getTag", nmsItem);
			}

			if (MinecraftPackage.IS_PRE_1_8)
				tagCompound.invokeMethod("set", tagObj, "ench", new Reflection("NBTTagList", MinecraftPackage.MINECRAFT_SERVER).newInstance());
			else {
				int enchants = 1;

				if ((boolean)tagCompound.invokeMethod("hasKey", tagObj, "HideFlags"))
					enchants |= (int)tagCompound.invokeMethod("getInt", tagObj, "HideFlags");

				tagCompound.invokeMethod("setInt", tagObj, "HideFlags", enchants);
				ItemMeta meta = this.getItemMeta();

				if (!meta.hasItemFlag(org.bukkit.inventory.ItemFlag.HIDE_ENCHANTS))
					meta.addItemFlags(org.bukkit.inventory.ItemFlag.HIDE_ENCHANTS);

				this.setItemMeta(meta);
			}

			nmsItemStack.invokeMethod("setTag", nmsItem, tagObj);
			this.glow = true;
		} catch (Exception ignore) { }
	}

	@Deprecated
	public static ItemStack addGlow(ItemStack stack) {
		ItemData data = new ItemData(stack);
		data.addGlow();
		return data;
	}

	public void clearMetadata() {
		this.metadata.clear();
	}

	@Override
	public ItemData clone() {
		ItemData itemData = new ItemData(super.clone());
		itemData.metadata.putAll(this.metadata);
		if (this.hasGlow()) itemData.addGlow();
		return itemData;
	}

	@Override
	public ItemMeta getItemMeta() {
		ItemMeta itemMeta = super.getItemMeta();
		ItemMeta factory = NiftyBukkit.getPlugin().getServer().getItemFactory().getItemMeta(this.getType()).clone();

		if (itemMeta == null && factory != null)
			super.setItemMeta(itemMeta = factory);

		if (itemMeta != null) {
			if (ListUtil.isEmpty(itemMeta.getLore())) {
				itemMeta.setLore(new ArrayList<String>());
				super.setItemMeta(itemMeta);
			}
		}

		return itemMeta;
	}

	public Object getMetadata(String key) {
		return this.metadata.get(key);
	}

	@Override
	public int getTypeId() {
		return super.getTypeId();
	}

	public boolean hasGlow() {
		return this.glow;
	}

	@Override
	public boolean hasItemMeta() {
		return this.getItemMeta() != null;
	}

	public boolean hasMetadata(String key) {
		return this.metadata.containsKey(key);
	}

	@Override
	public boolean isSimilar(ItemStack stack) {
		if (stack == null) return false;
		ItemData data = new ItemData(stack);

		if (this.getTypeId() == data.getTypeId()) {
			if (this.getDurability() == data.getDurability()) {
				if (this.getData().getData() == data.getData().getData()) {
					if (this.getItemMeta().hasDisplayName() && this.getItemMeta().getDisplayName().equals(data.getItemMeta().getDisplayName())) {
						if (this.getItemMeta().getLore().equals(data.getItemMeta().getLore()))
							return true;
					}
				}
			}
		}

		return false;
	}

	public void removeGlow() {
		if (!this.hasGlow())
			return;

		try {
			if (!MinecraftPackage.IS_PRE_1_8)
				this.removeEnchantment(Enchantment.DURABILITY);

			Reflection craftItemStack = new Reflection("CraftItemStack", "inventory", MinecraftPackage.CRAFTBUKKIT);
			Reflection nmsItemStack = new Reflection("ItemStack", MinecraftPackage.MINECRAFT_SERVER);
			Reflection tagCompound = new Reflection("NBTTagCompound", MinecraftPackage.MINECRAFT_SERVER);
			Object nmsItem = craftItemStack.invokeMethod("asNMSCopy", null, this);
			Object tagObj = nmsItemStack.invokeMethod("getTag", nmsItem);

			if (tagObj == null) {
				tagObj = tagCompound.newInstance();
				nmsItemStack.invokeMethod("setTag", nmsItem, tagObj);
				tagObj = nmsItemStack.invokeMethod("getTag", nmsItem);
			}

			if (MinecraftPackage.IS_PRE_1_8)
				tagCompound.invokeMethod("set", tagObj, "ench", new Reflection("NBTTagList", MinecraftPackage.MINECRAFT_SERVER).newInstance());
			else {
				tagCompound.invokeMethod("remove", tagObj, "HideFlags");
				ItemMeta meta = this.getItemMeta();

				if (meta.hasItemFlag(org.bukkit.inventory.ItemFlag.HIDE_ENCHANTS))
					meta.removeItemFlags(org.bukkit.inventory.ItemFlag.HIDE_ENCHANTS);

				this.setItemMeta(meta);
			}

			nmsItemStack.invokeMethod("setTag", nmsItem, tagObj);
			this.glow = false;
		} catch (Exception ignore) { }
	}

	public void removeMetadata(String key) {
		this.metadata.remove(key);
	}

	@Override
	public boolean setItemMeta(ItemMeta itemMeta) {
		if (itemMeta.hasDisplayName())
			itemMeta.setDisplayName(RegexUtil.replaceColor(itemMeta.getDisplayName(), RegexUtil.REPLACE_ALL_PATTERN));

		if (ListUtil.isEmpty(itemMeta.getLore()))
			itemMeta.setLore(new ArrayList<String>());

		List<String> lore = itemMeta.getLore();

		for (int i = 0; i < lore.size(); i++)
			lore.set(i, RegexUtil.replaceColor(lore.get(i), RegexUtil.REPLACE_ALL_PATTERN));

		itemMeta.setLore(lore);
		return super.setItemMeta(itemMeta);
	}

	public void setMetadata(String key, Object obj) {
		this.metadata.put(key, obj);
	}

	@Override
	public void setTypeId(int type) {
		super.setTypeId(type);
	}

}