package net.netcoding.niftybukkit.inventory.items;

import java.util.ArrayList;

import net.netcoding.niftybukkit.NiftyBukkit;
import net.netcoding.niftybukkit.reflection.MinecraftPackage;
import net.netcoding.niftybukkit.reflection.Reflection;
import net.netcoding.niftybukkit.util.ListUtil;

import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;

@SuppressWarnings("deprecation")
public class ItemData {

	private final int id;
	private final short data;
	private boolean glow = false;

	public ItemData(ItemStack stack) {
		this(stack.getTypeId(), stack.getDurability());
	}

	public ItemData(Material material) {
		this(material.getId(), (short)0);
	}

	public ItemData(Material material, short data) {
		this(material.getId(), data);
	}

	public ItemData(int id) {
		this(id, (short)0);
	}

	public ItemData(int id, short data) {
		this.id = id;
		this.data = data;
	}

	public void addGlow() {
		this.glow = true;
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

				tagCompound.invokeMethod("setInt", tagObj, "HideFlags", 63);
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
	public boolean equals(Object obj) {
		if (obj == null) return false;
		if (!(obj instanceof ItemData)) return false;
		if (this == obj) return true;
		ItemData item = (ItemData)obj;
		return this.getId() == item.getId() && this.getData() == item.getData();
	}

	public ItemStack getItem() {
		ItemStack stack = new ItemStack(id, data);
		if (this.hasGlow()) stack = addGlow(stack);

		if (!stack.hasItemMeta() || stack.getItemMeta() == null)
			stack.setItemMeta(NiftyBukkit.getPlugin().getServer().getItemFactory().getItemMeta(stack.getType()));

		if (!stack.getItemMeta().hasLore() || ListUtil.isEmpty(stack.getItemMeta().getLore()))
			stack.getItemMeta().setLore(new ArrayList<String>());

		return stack;
	}

	public int getId() {
		return this.getItem().getTypeId();
	}

	public short getData() {
		return this.getItem().getDurability();
	}

	public boolean hasGlow() {
		return this.glow;
	}

	@Override
	public int hashCode() {
		return (31 * this.getId()) ^ this.getData();
	}

	public void removeGlow() {
		this.glow = false;
	}

}