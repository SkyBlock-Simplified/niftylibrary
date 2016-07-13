package net.netcoding.nifty.craftbukkit.minecraft.block.state;

import net.netcoding.nifty.common.minecraft.block.state.Jukebox;
import net.netcoding.nifty.common.minecraft.material.Material;

public final class CraftJukebox extends CraftBlockState implements Jukebox {

	public CraftJukebox(org.bukkit.block.Jukebox jukebox) {
		super(jukebox);
	}

	@Override
	public boolean eject() {
		return this.getHandle().eject();
	}

	@Override
	public org.bukkit.block.Jukebox getHandle() {
		return (org.bukkit.block.Jukebox)super.getHandle();
	}

	@Override
	public Material getPlaying() {
		return Material.valueOf(this.getHandle().getPlaying().name());
	}

	@Override
	public boolean isPlaying() {
		return this.getHandle().isPlaying();
	}

	@Override
	public void setPlaying(Material record) {
		this.getHandle().setPlaying(org.bukkit.Material.valueOf(record.name()));
	}

}