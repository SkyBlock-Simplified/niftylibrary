package net.netcoding.niftybukkit.minecraft.events;

import net.netcoding.niftybukkit.minecraft.events.profile.ProfileEvent;
import net.netcoding.niftybukkit.mojang.BukkitMojangProfile;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.EnderCrystal;
import org.bukkit.event.Cancellable;

public class EnderCrystalPlaceEvent extends ProfileEvent implements Cancellable {

	private final EnderCrystal entity;
	private boolean cancelled = false;

	public EnderCrystalPlaceEvent(BukkitMojangProfile profile, EnderCrystal entity) {
		super(profile);
		this.entity = entity;
	}

	public final Block getBaseBlock() {
		return this.entity.getLocation().getBlock().getRelative(BlockFace.DOWN);
	}

	public final EnderCrystal getEntity() {
		return this.entity;
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