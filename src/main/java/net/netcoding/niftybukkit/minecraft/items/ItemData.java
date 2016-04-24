package net.netcoding.niftybukkit.minecraft.items;

import net.netcoding.niftybukkit.NiftyBukkit;
import net.netcoding.niftybukkit.minecraft.nbt.NbtCompound;
import net.netcoding.niftybukkit.minecraft.nbt.NbtFactory;
import net.netcoding.niftybukkit.reflection.MinecraftPackage;
import net.netcoding.niftycore.reflection.Reflection;
import net.netcoding.niftycore.util.ListUtil;
import net.netcoding.niftycore.util.RegexUtil;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@SuppressWarnings("deprecation")
public class ItemData extends ItemStack {

	private static Reflection CRAFT_ITEM_STACK = new Reflection("CraftItemStack", "inventory", MinecraftPackage.CRAFTBUKKIT);
	private static Reflection NMS_ITEM_STACK = new Reflection("ItemStack", MinecraftPackage.MINECRAFT_SERVER);
	private final Object nmsItem;
	private final NbtCompound root;
	private boolean glow = false;

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

	public ItemData(ItemStack stack) {
		this(stack, (stack != null && CRAFT_ITEM_STACK.getClazz().isAssignableFrom(stack.getClass()) && Material.AIR != stack.getType() ? NbtFactory.fromItemTag(stack) : null));
	}

	private ItemData(ItemStack stack, NbtCompound root) {
		super(stack == null ? new ItemStack(Material.AIR) : stack);

		if (this.getAmount() <= 0)
			this.setAmount(1);

		this.nmsItem = CRAFT_ITEM_STACK.invokeMethod("asNMSCopy", null, this);
		this.root = (root == null ? ((stack instanceof ItemData) ? ((ItemData)stack).root.clone() : NbtFactory.createRootCompound("tag")) : root);
	}

	public void addGlow() {
		if (this.hasGlow())
			return;

		try {
			if (!MinecraftPackage.IS_PRE_1_8)
				this.addUnsafeEnchantment(Enchantment.DURABILITY, -1);

			if (MinecraftPackage.IS_PRE_1_8)
				this.putNbt("ench", NbtFactory.createList());
			else {
				int enchants = 1;

				if (this.hasNbt("HideFlags"))
					enchants |= this.<Integer>getNbt("HideFlags");

				this.putNbt("HideFlags", enchants);
				ItemMeta meta = this.getItemMeta();

				if (!meta.hasItemFlag(org.bukkit.inventory.ItemFlag.HIDE_ENCHANTS))
					meta.addItemFlags(org.bukkit.inventory.ItemFlag.HIDE_ENCHANTS);

				this.setItemMeta(meta);
			}

			this.setNbtCompound();
			this.glow = true;
		} catch (Exception ignore) { }
	}

	@Deprecated
	public static ItemData addGlow(ItemStack stack) {
		ItemData data = new ItemData(stack);
		data.addGlow();
		return data;
	}

	@Override
	public ItemData clone() {
		ItemData itemData = new ItemData(super.clone(), this.root.clone());

		if (this.hasGlow())
			itemData.addGlow();

		return itemData;
	}

	@Override
	public ItemMeta getItemMeta() {
		ItemMeta itemMeta = super.getItemMeta();

		if (itemMeta == null) {
			ItemMeta factory = NiftyBukkit.getPlugin().getServer().getItemFactory().getItemMeta(this.getType());

			if (factory != null)
				super.setItemMeta(itemMeta = factory.clone());
		}

		if (itemMeta != null) {
			if (ListUtil.isEmpty(itemMeta.getLore())) {
				itemMeta.setLore(new ArrayList<String>());
				super.setItemMeta(itemMeta);
			}
		}

		return (itemMeta != null ? itemMeta.clone() : null);
	}

	public final <T> T getNbt(String key) {
		return this.root.get(key);
	}

	public final <T> T getNbtPath(String path) {
		return this.root.getPath(path);
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

	public final boolean hasNbt(String key) {
		return this.root.containsKey(key);
	}

	public final boolean hasNbtPath(String path) {
		return this.root.containsPath(path);
	}

	@Override
	public boolean isSimilar(ItemStack stack) {
		if (stack == null) return false;
		ItemData data = new ItemData(stack);

		if (this.getTypeId() == data.getTypeId()) {
			if (this.getDurability() == data.getDurability()) {
				if (this.getData().getData() == data.getData().getData()) {
					if (this.hasItemMeta() == data.hasItemMeta())
						return !this.hasItemMeta() || Bukkit.getItemFactory().equals(this.getItemMeta(), data.getItemMeta());
				}
			}
		}

		return false;
	}

	public final void putAllNbt(Map<? extends String, ?> m) {
		this.root.putAll(m);
		this.setNbtCompound();
	}

	public final Object putNbt(String key, Object value) {
		Object obj = this.root.put(key, value);
		this.setNbtCompound();
		return obj;
	}

	public final NbtCompound putNbtPath(String path, Object value) {
		NbtCompound compound = this.root.putPath(path, value);
		this.setNbtCompound();
		return compound;
	}

	public void removeGlow() {
		if (!this.hasGlow())
			return;

		try {
			if (!MinecraftPackage.IS_PRE_1_8)
				this.removeEnchantment(Enchantment.DURABILITY);

			if (MinecraftPackage.IS_PRE_1_8)
				this.root.remove("ench");
			else {
				this.root.remove("HideFlags");
				ItemMeta meta = this.getItemMeta();

				if (meta.hasItemFlag(org.bukkit.inventory.ItemFlag.HIDE_ENCHANTS))
					meta.removeItemFlags(org.bukkit.inventory.ItemFlag.HIDE_ENCHANTS);

				this.setItemMeta(meta);
			}

			this.setNbtCompound();
			this.glow = false;
		} catch (Exception ignore) { }
	}

	public final Object removeNbt(String key) {
		Object obj = this.root.remove(key);
		this.setNbtCompound();
		return obj;
	}

	public final NbtCompound removeNbtPath(String path) {
		NbtCompound compund = this.root.removePath(path);
		this.setNbtCompound();
		return compund;
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

	private void setNbtCompound() {
		NMS_ITEM_STACK.invokeMethod("setTag", this.nmsItem, this.root.getHandle());
		ItemMeta nmsMeta = (ItemMeta)CRAFT_ITEM_STACK.invokeMethod("getItemMeta", null, this.nmsItem);

		if (this.hasItemMeta()) {
			ItemMeta meta = this.getItemMeta();

			if (!MinecraftPackage.IS_PRE_1_8)
				nmsMeta.addItemFlags(ListUtil.toArray(meta.getItemFlags(), org.bukkit.inventory.ItemFlag.class));

			nmsMeta.setDisplayName(meta.getDisplayName());
			nmsMeta.setLore(meta.getLore());
			Map<Enchantment, Integer> enchantments = meta.getEnchants();

			for (Enchantment enchantment : enchantments.keySet())
				nmsMeta.addEnchant(enchantment, enchantments.get(enchantment), true);
		}

		super.setItemMeta(nmsMeta);
	}

	@Override
	public void setTypeId(int type) {
		super.setTypeId(type);
	}

}