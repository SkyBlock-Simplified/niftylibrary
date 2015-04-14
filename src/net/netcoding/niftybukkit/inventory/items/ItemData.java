package net.netcoding.niftybukkit.inventory.items;

import java.util.ArrayList;

import net.netcoding.niftybukkit.NiftyBukkit;
import net.netcoding.niftybukkit.reflection.MinecraftPackage;
import net.netcoding.niftybukkit.reflection.Reflection;

import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class ItemData extends ItemStack {

	private boolean glow = false;

	public ItemData(ItemStack stack) {
		super(stack);
	}

	public ItemData(Material material) {
		this(material.getId(), (short)0);
	}

	public ItemData(Material material, short durability) {
		super(material, durability);
	}

	public ItemData(int id) {
		this(id, (short)0);
	}

	public ItemData(int id, short data) {
		super(id, data);
	}

	private ItemData(ItemData source) {
		super(source);
	}

	public final static ItemStack addGlow(ItemStack stack) {
		try {
			if (!MinecraftPackage.IS_PRE_1_8)
				stack.addUnsafeEnchantment(Enchantment.DURABILITY, -1);

			Reflection craftItemStack = new Reflection("CraftItemStack", "inventory", MinecraftPackage.CRAFTBUKKIT);
			Reflection nmsItemStack = new Reflection("ItemStack", MinecraftPackage.MINECRAFT_SERVER);
			Reflection tagCompound = new Reflection("NBTTagCompound", MinecraftPackage.MINECRAFT_SERVER);
			Object itemStackObj = craftItemStack.invokeMethod("asNMSCopy", null, stack);
			Object tagObj = nmsItemStack.invokeMethod("getTag", itemStackObj);

			if (tagObj == null) {
				tagObj = tagCompound.newInstance();
				nmsItemStack.invokeMethod("setTag", itemStackObj, tagObj);
				tagObj = nmsItemStack.invokeMethod("getTag", itemStackObj);
			}

			if (MinecraftPackage.IS_PRE_1_8)
				tagCompound.invokeMethod("set", tagObj, "ench", new Reflection("NBTTagList", MinecraftPackage.MINECRAFT_SERVER).newInstance());
			else if (MinecraftPackage.IS_PRE_1_8_3) {
				int enchants = 1;

				if ((boolean)tagCompound.invokeMethod("hasKey", tagObj, "HideFlags"))
					enchants |= (int)tagCompound.invokeMethod("getInt", tagObj, "HideFlags");

				tagCompound.invokeMethod("setInt", tagObj, "HideFlags", enchants);
			} else {
				if (!stack.getItemMeta().hasItemFlag(org.bukkit.inventory.ItemFlag.HIDE_ENCHANTS))
					stack.getItemMeta().addItemFlags(org.bukkit.inventory.ItemFlag.HIDE_ENCHANTS);

				return stack;
			}

			nmsItemStack.invokeMethod("setTag", itemStackObj, tagObj);
			return (ItemStack)craftItemStack.invokeMethod("asCraftMirror", null, itemStackObj);
		} catch (Exception ex) {
			return stack;
		}
	}

	@Override
	public ItemData clone() {
		return new ItemData(this);
	}

	@Override
	public ItemMeta getItemMeta() {
		ItemMeta itemMeta = super.getItemMeta();
		ItemMeta factory = NiftyBukkit.getPlugin().getServer().getItemFactory().getItemMeta(this.getType());

		if (itemMeta == null && factory != null)
			super.setItemMeta(factory);

		if (itemMeta.getLore() == null) {
			itemMeta.setLore(new ArrayList<String>());
			this.setItemMeta(itemMeta);
		}

		return itemMeta;
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

	public void setGlow() {
		this.setGlow(true);
	}

	public void setGlow(boolean value) {
		this.glow = value;
	}

	@Override
	public void setTypeId(int type) {
		super.setTypeId(type);
	}

}