package net.netcoding.nifty.craftbukkit.minecraft.inventory.item.meta;

import net.netcoding.nifty.common.minecraft.inventory.item.meta.SkullMeta;

public final class CraftSkullMeta extends CraftItemMeta implements SkullMeta {

	public CraftSkullMeta(org.bukkit.inventory.meta.SkullMeta skullMeta) {
		super(skullMeta);
	}

	@Override
	public SkullMeta clone() {
		return new CraftSkullMeta(this.getHandle().clone());
	}

	@Override
	public org.bukkit.inventory.meta.SkullMeta getHandle() {
		return (org.bukkit.inventory.meta.SkullMeta)super.getHandle();
	}

	@Override
	public String getOwner() {
		return this.getHandle().getOwner();
	}

	@Override
	public boolean setOwner(String owner) {
		return this.getHandle().setOwner(owner);
	}

}