package net.netcoding.nifty.craftbukkit.minecraft.entity.hanging;

import net.netcoding.nifty.common.minecraft.Art;
import net.netcoding.nifty.common.minecraft.entity.hanging.Painting;

public final class CraftPainting extends CraftHanging implements Painting {

	public CraftPainting(org.bukkit.entity.Painting painting) {
		super(painting);
	}

	@Override
	public org.bukkit.entity.Painting getHandle() {
		return (org.bukkit.entity.Painting)super.getHandle();
	}

	@Override
	public Art getArt() {
		return Art.valueOf(this.getHandle().getArt().name());
	}

	@Override
	public boolean setArt(Art art, boolean force) {
		return this.getHandle().setArt(org.bukkit.Art.valueOf(art.name()), force);
	}

}