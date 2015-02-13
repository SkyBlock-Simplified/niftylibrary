package net.netcoding.niftybukkit.libraries;

import net.netcoding.niftybukkit.reflection.MinecraftPackage;
import net.netcoding.niftybukkit.reflection.Reflection;

import org.bukkit.inventory.ItemStack;

public class FakeEnchantment {
	public static ItemStack addReflectedGlow(ItemStack item) throws Exception {
		Reflection craftItemStack = new Reflection("CraftItemStack", "inventory", MinecraftPackage.CRAFTBUKKIT);
		Object itemStack;
		itemStack = craftItemStack.invokeMethod("asNMSCopy", null, item);
		Reflection nmsStack = new Reflection("ItemStack", MinecraftPackage.MINECRAFT_SERVER);
		Object tag = null;
		boolean hasTag = (boolean)nmsStack.invokeMethod("hasTag", itemStack);
		Reflection tagCompound = new Reflection("NBTTagCompound", MinecraftPackage.MINECRAFT_SERVER);
		if (!hasTag) {
			tag = tagCompound.newInstance();
			nmsStack.invokeMethod("setTag", itemStack, tag);
		}
		if (tag == null) 
			tag = nmsStack.invokeMethod("getTag", itemStack);
		tagCompound.invokeMethod("set", tag, "ench", new Reflection("NBTTagList", MinecraftPackage.MINECRAFT_SERVER).newInstance());
		nmsStack.invokeMethod("setTag", itemStack, tag);
		return (ItemStack)craftItemStack.invokeMethod("asCraftMirror", null, itemStack);
	}
}