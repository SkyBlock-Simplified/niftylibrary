package net.netcoding.niftybukkit.inventory.items;

import net.netcoding.niftybukkit.NiftyBukkit;
import net.netcoding.niftybukkit.reflection.MinecraftPackage;
import net.netcoding.niftybukkit.reflection.Reflection;

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

	public final static ItemStack addGlow(ItemStack item) {
		try {
			item.addUnsafeEnchantment(Enchantment.DURABILITY, -1);
			Reflection craftItemStack = new Reflection("CraftItemStack", "inventory", MinecraftPackage.CRAFTBUKKIT);
			Reflection nmsItemStack = new Reflection("ItemStack", MinecraftPackage.MINECRAFT_SERVER);
			Reflection tagCompound = new Reflection("NBTTagCompound", MinecraftPackage.MINECRAFT_SERVER);
			Object itemStack = craftItemStack.invokeMethod("asNMSCopy", null, item);
			Object tag = nmsItemStack.invokeMethod("getTag", itemStack);

			if (tag == null) {
				tag = tagCompound.newInstance();
				nmsItemStack.invokeMethod("setTag", itemStack, tag);
				tag = nmsItemStack.invokeMethod("getTag", itemStack);
			}


			if (MinecraftPackage.IS_PRE_1_8)
				tagCompound.invokeMethod("set", tag, "ench", new Reflection("NBTTagList", MinecraftPackage.MINECRAFT_SERVER).newInstance());
			else {
				if (!(boolean)tagCompound.invokeMethod("hasKey", tag, "HideFlags"))
					tagCompound.invokeMethod("setInt", tag, "HideFlags", 1);
			}

			nmsItemStack.invokeMethod("setTag", itemStack, tag);
			return (ItemStack)craftItemStack.invokeMethod("asCraftMirror", null, itemStack);
		} catch (Exception ex) {
			return item;
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
		if (!stack.hasItemMeta()) stack.setItemMeta(NiftyBukkit.getPlugin().getServer().getItemFactory().getItemMeta(stack.getType()));
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