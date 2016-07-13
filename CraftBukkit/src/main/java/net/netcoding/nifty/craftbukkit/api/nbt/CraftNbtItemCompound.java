package net.netcoding.nifty.craftbukkit.api.nbt;

import net.netcoding.nifty.common.Nifty;
import net.netcoding.nifty.common.api.nbt.NbtCompound;
import net.netcoding.nifty.common.api.nbt.NbtItemCompound;
import net.netcoding.nifty.common.minecraft.inventory.item.enchantment.Enchantment;
import net.netcoding.nifty.common.minecraft.inventory.item.meta.ItemMeta;
import net.netcoding.nifty.common.minecraft.material.Material;
import net.netcoding.nifty.common.reflection.MinecraftProtocol;
import net.netcoding.nifty.craftbukkit.minecraft.inventory.item.CraftItemStack;
import net.netcoding.nifty.craftbukkit.minecraft.inventory.item.meta.CraftItemMeta;

import java.util.Map;

public final class CraftNbtItemCompound extends NbtItemCompound<CraftItemStack> {

	private final Object nmsItem;

	CraftNbtItemCompound(CraftItemStack item, Object nms, Object handle) {
		super(item, handle);
		this.nmsItem = nms;
		this.load();
	}

	@Override
	protected void load() {
		if (Material.AIR != this.getWrapped().getType()) {
			Object tag = CraftNbtFactory.NMS_ITEM_STACK.invokeMethod(CraftNbtFactory.NBT_TAG_COMPOUND.getClazz(), this.nmsItem);

			if (tag != null) {
				NbtCompound compound = Nifty.getNbtFactory().fromCompound(tag);
				this.putAll(compound);
			} else
				this.save();
		}
	}

	@Override
	protected void save() {
		if (Material.AIR != this.getWrapped().getType()) {
			CraftNbtFactory.NMS_ITEM_STACK.setValue(CraftNbtFactory.NBT_TAG_COMPOUND.getClazz(), this.nmsItem, this.getHandle());
			org.bukkit.inventory.meta.ItemMeta nmsMeta = (org.bukkit.inventory.meta.ItemMeta)CraftNbtFactory.CRAFT_ITEM_STACK.invokeMethod("getItemMeta", null, this.nmsItem);

			if (this.getWrapped().hasItemMeta()) {
				ItemMeta meta = this.getWrapped().getItemMeta();
				nmsMeta.setDisplayName(meta.getDisplayName());
				nmsMeta.setLore(meta.getLore());

				if (MinecraftProtocol.isPost1_7())
					meta.getItemFlags().stream().forEach(flag -> nmsMeta.addItemFlags(org.bukkit.inventory.ItemFlag.valueOf(flag.name())));

				for (Map.Entry<Enchantment, Integer> entry : meta.getEnchants().entrySet())
					nmsMeta.addEnchant(org.bukkit.enchantments.Enchantment.getByName(entry.getKey().getName()), entry.getValue(), true);
			}

			this.getWrapped().setItemMeta(new CraftItemMeta(nmsMeta));
		}
	}

}