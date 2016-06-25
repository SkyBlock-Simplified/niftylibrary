package net.netcoding.nifty.common.minecraft.event.entity;

import net.netcoding.nifty.common.minecraft.block.Block;
import net.netcoding.nifty.common.minecraft.block.BlockFace;
import net.netcoding.nifty.common.minecraft.entity.block.EnderCrystal;
import net.netcoding.nifty.common.minecraft.event.Cancellable;
import net.netcoding.nifty.common.minecraft.event.player.PlayerEvent;
import net.netcoding.nifty.common.mojang.BukkitMojangProfile;

public final class EnderCrystalPlaceEvent implements Cancellable, PlayerEvent {

	private final BukkitMojangProfile profile;
	private final EnderCrystal entity;
	private boolean cancelled = false;

	public EnderCrystalPlaceEvent(BukkitMojangProfile profile, EnderCrystal entity) {
		this.profile = profile;
		this.entity = entity;
	}

	public Block getBaseBlock() {
		return this.entity.getLocation().getBlock().getRelative(BlockFace.DOWN);
	}

	public EnderCrystal getEntity() {
		return this.entity;
	}

	@Override
	public BukkitMojangProfile getProfile() {
		return this.profile;
	}

	@Override
	public boolean isCancelled() {
		return this.cancelled;
	}

	@Override
	public void setCancelled(boolean value) {
		this.cancelled = value;
	}

}