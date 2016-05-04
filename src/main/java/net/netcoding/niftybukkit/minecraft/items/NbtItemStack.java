package net.netcoding.niftybukkit.minecraft.items;

import net.netcoding.niftybukkit.minecraft.nbt.NbtCompound;
import net.netcoding.niftybukkit.minecraft.nbt.NbtFactory;
import net.netcoding.niftybukkit.reflection.MinecraftPackage;
import net.netcoding.niftycore.reflection.Reflection;
import net.netcoding.niftycore.util.ListUtil;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.Set;

@SuppressWarnings("deprecation")
public class NbtItemStack extends ItemStack {

	private static Reflection CRAFT_ITEM_STACK = new Reflection("CraftItemStack", "inventory", MinecraftPackage.CRAFTBUKKIT);
	private static Reflection NMS_ITEM_STACK = new Reflection("ItemStack", MinecraftPackage.MINECRAFT_SERVER);
	private final Object nmsItem;
	private final NbtCompound root;

	public NbtItemStack(int id) {
		this(id, (short)0);
	}

	public NbtItemStack(int id, short durability) {
		this(Material.getMaterial(id), durability);
	}

	public NbtItemStack(Material material) {
		this(material, (short)0);
	}

	public NbtItemStack(Material material, short durability) {
		this(new ItemStack(material, 1, durability));
	}

	public NbtItemStack(ItemStack stack) {
		this(stack, (stack != null && CRAFT_ITEM_STACK.getClazz().isAssignableFrom(stack.getClass()) && Material.AIR != stack.getType() ? NbtFactory.fromItemTag(stack) : null));
	}

	private NbtItemStack(ItemStack stack, NbtCompound root) {
		super(stack == null ? new ItemStack(Material.AIR) : stack);

		if (this.getAmount() <= 0)
			this.setAmount(1);

		this.nmsItem = CRAFT_ITEM_STACK.invokeMethod("asNMSCopy", null, this);
		this.root = (root == null ? ((stack instanceof ItemData) ? ((NbtItemStack)stack).root.clone() : NbtFactory.createRootCompound("tag")) : root);
	}

	@Override
	public NbtItemStack clone() {
		NbtItemStack nbtStack = new NbtItemStack(super.clone(), this.root.clone());
		nbtStack.setNbtCompound();
		return nbtStack;
	}

	public final boolean containsNbt() {
		return !this.root.isEmpty();
	}

	public final boolean containsNbtKey(String key) {
		return this.root.containsKey(key);
	}

	public final boolean containsNbtPath(String path) {
		return this.root.containsPath(path);
	}

	public final <T> T getNbt(String key) {
		return this.root.get(key);
	}

	public final Set<Map.Entry<String, Object>> getNbtEntrySet() {
		return Collections.unmodifiableSet(this.root.entrySet());
	}

	public final Set<String> getNbtKeys() {
		return Collections.unmodifiableSet(this.root.keySet());
	}

	public final <T> T getNbtPath(String path) {
		return this.root.getPath(path);
	}

	public final Collection<Object> getNbtValues() {
		return Collections.unmodifiableCollection(this.root.values());
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

	private void setNbtCompound() {
		NMS_ITEM_STACK.invokeMethod("setTag", this.nmsItem, this.root.getHandle());
		ItemMeta nmsMeta = (ItemMeta)CRAFT_ITEM_STACK.invokeMethod("getItemMeta", null, this.nmsItem);

		if (this.hasItemMeta()) {
			ItemMeta meta = super.getItemMeta();

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

}