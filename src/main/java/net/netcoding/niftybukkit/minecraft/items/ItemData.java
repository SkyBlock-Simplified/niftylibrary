package net.netcoding.niftybukkit.minecraft.items;

import net.netcoding.niftybukkit.NiftyBukkit;
import net.netcoding.niftybukkit.minecraft.nbt.NbtCompound;
import net.netcoding.niftybukkit.minecraft.nbt.NbtFactory;
import net.netcoding.niftybukkit.reflection.MinecraftProtocol;
import net.netcoding.niftycore.util.ListUtil;
import net.netcoding.niftycore.util.RegexUtil;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("deprecation")
public class ItemData extends ItemStack {

	private final NbtCompound root;

	private static final String GLOWING = "ITEMDATA_GLOW";

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

	ItemData(Material material, short durability, boolean create) {
		this(new ItemStack(material, 1, durability), create);
	}

	public ItemData(ItemStack stack) {
		this(stack, true);
	}

	ItemData(ItemStack itemStack, boolean create) {
		super(itemStack == null ? new ItemStack(Material.AIR) : itemStack);

		if (this.getAmount() <= 0)
			this.setAmount(1);

		if (create && Material.AIR != NbtFactory.getCraftItemStack(this).getType())
			this.root = NbtFactory.fromItemTag(this);
		else
			this.root = NbtFactory.createRootCompound("tag");
	}

	@Deprecated
	public void addGlow() {
		if (this.hasGlow())
			return;

		if (MinecraftProtocol.isPost1_8())
			this.addUnsafeEnchantment(Enchantment.DURABILITY, 0);

		if (MinecraftProtocol.isPre1_8())
			this.getNbt().put("ench", NbtFactory.createList());
		else {
			int enchants = 1;

			if (this.getNbt().containsKey("HideFlags"))
				enchants |= this.getNbt().<Integer>get("HideFlags");

			this.getNbt().put("HideFlags", enchants);
			ItemMeta meta = this.getItemMeta();

			if (!meta.hasItemFlag(org.bukkit.inventory.ItemFlag.HIDE_ENCHANTS))
				meta.addItemFlags(org.bukkit.inventory.ItemFlag.HIDE_ENCHANTS);

			this.setItemMeta(meta);
		}

		this.getNbt().put(GLOWING, true);
	}

	@Deprecated
	public static ItemData addGlow(ItemStack stack) {
		ItemData itemData = new ItemData(stack);
		itemData.setGlowing(true);
		return itemData;
	}

	@Override
	public ItemData clone() {
		ItemData itemData = new ItemData(super.clone());
		itemData.setGlowing(this.hasGlow());
		itemData.getNbt().putAll(this.root);
		return itemData;
	}

	@Override
	public ItemMeta getItemMeta() {
		return this.getItemMeta(false);
	}

	public ItemMeta getItemMeta(boolean unformatted) {
		ItemMeta itemMeta = super.getItemMeta();

		if (itemMeta == null) {
			ItemMeta factory = NiftyBukkit.getPlugin().getServer().getItemFactory().getItemMeta(this.getType());

			if (factory != null)
				super.setItemMeta(itemMeta = factory.clone());
		}

		if (itemMeta != null) {
			if (unformatted) {
				if (itemMeta.hasDisplayName())
					itemMeta.setDisplayName(RegexUtil.replace(itemMeta.getDisplayName(), RegexUtil.VANILLA_PATTERN, "&$1"));
			}

			List<String> lore = itemMeta.getLore();

			if (ListUtil.isEmpty(lore))
				lore = new ArrayList<>();
			else {
				if (unformatted) {
					for (int i = 0; i < lore.size(); i++)
						lore.set(i, RegexUtil.replace(lore.get(i), RegexUtil.VANILLA_PATTERN, "&$1"));
				}
			}

			itemMeta.setLore(lore);
			super.setItemMeta(itemMeta);
		}

		return (itemMeta != null ? itemMeta.clone() : null);
	}

	public final NbtCompound getNbt() {
		return this.root;
	}

	@Override
	public int getTypeId() {
		return super.getTypeId();
	}

	public boolean hasGlow() {
		return this.getNbt().containsKey(GLOWING) && this.getNbt().<Boolean>get(GLOWING);
	}

	@Override
	public boolean hasItemMeta() {
		return this.getItemMeta() != null;
	}

	@Override
	public boolean isSimilar(ItemStack stack) {
		if (stack == null) return false;
		ItemData data = new ItemData(stack, false);

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

	@Deprecated
	public void removeGlow() {
		if (!this.hasGlow())
			return;

		if (MinecraftProtocol.isPost1_8())
			this.removeEnchantment(Enchantment.DURABILITY);

		if (MinecraftProtocol.isPre1_8())
			this.getNbt().remove("ench");
		else {
			this.getNbt().remove("HideFlags");
			ItemMeta meta = this.getItemMeta();

			if (meta.hasItemFlag(org.bukkit.inventory.ItemFlag.HIDE_ENCHANTS))
				meta.removeItemFlags(org.bukkit.inventory.ItemFlag.HIDE_ENCHANTS);

			this.setItemMeta(meta);
		}

		this.getNbt().remove(GLOWING);
	}

	public void setGlowing() {
		this.setGlowing(true);
	}

	public void setGlowing(boolean value) {
		if (value)
			this.addGlow();
		else
			this.removeGlow();
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

	@Override
	public void setTypeId(int type) {
		super.setTypeId(type);
	}

	@Override
	public String toString() {
		return super.toString(); // TODO: Nbt Output
	}

}