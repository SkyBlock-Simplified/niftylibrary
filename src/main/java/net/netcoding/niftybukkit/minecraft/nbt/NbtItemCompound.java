package net.netcoding.niftybukkit.minecraft.nbt;

import net.netcoding.niftybukkit.reflection.MinecraftPackage;
import net.netcoding.niftycore.util.ListUtil;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Map;

public final class NbtItemCompound extends WrappedCompound<ItemStack> {

	private final Object nmsItem;

	private NbtItemCompound(ItemStack itemStack, Object handle) {
		this(itemStack, NbtFactory.CRAFT_ITEM_STACK.invokeMethod("asNMSCopy", null, itemStack), handle);
	}

	NbtItemCompound(ItemStack itemStack, Object nms, Object handle) {
		super(itemStack, handle);
		this.nmsItem = nms;
		this.load();
	}

	@Override
	public NbtItemCompound clone() {
		return new NbtItemCompound(this.getWrapped().clone(), super.clone().getHandle());
	}

	@Override
	protected final void load() {
		if (Material.AIR != this.getWrapped().getType()) {
			Object tag = NbtFactory.NMS_ITEM_STACK.invokeMethod("getTag", this.nmsItem);

			if (tag != null) {
				NbtCompound compound = NbtFactory.fromCompound(tag);
				this.putAll(compound);
			}

			this.save();
		}
	}

	@Override
	protected final void save() {
		if (Material.AIR != this.getWrapped().getType()) {
			NbtFactory.NMS_ITEM_STACK.invokeMethod("setTag", this.nmsItem, this.getHandle());
			ItemMeta nmsMeta = (ItemMeta)NbtFactory.CRAFT_ITEM_STACK.invokeMethod("getItemMeta", null, this.nmsItem);

			if (this.getWrapped().hasItemMeta()) {
				ItemMeta meta = this.getWrapped().getItemMeta();

				if (!MinecraftPackage.IS_PRE_1_8)
					nmsMeta.addItemFlags(ListUtil.toArray(meta.getItemFlags(), org.bukkit.inventory.ItemFlag.class));

				nmsMeta.setDisplayName(meta.getDisplayName());
				nmsMeta.setLore(meta.getLore());
				Map<Enchantment, Integer> enchantments = meta.getEnchants();

				for (Enchantment enchantment : enchantments.keySet())
					nmsMeta.addEnchant(enchantment, enchantments.get(enchantment), true);
			}

			this.getWrapped().setItemMeta(nmsMeta);
		}
	}

}