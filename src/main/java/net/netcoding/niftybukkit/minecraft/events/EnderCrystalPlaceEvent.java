package net.netcoding.niftybukkit.minecraft.events;

import net.netcoding.niftybukkit.mojang.BukkitMojangProfile;
import org.bukkit.entity.EnderCrystal;
import org.bukkit.event.Cancellable;

public class EnderCrystalPlaceEvent extends PlayerEvent implements Cancellable {

	private final EnderCrystal entity;
	private boolean cancelled = false;

	public EnderCrystalPlaceEvent(BukkitMojangProfile profile, EnderCrystal entity) {
		super(profile);
		this.entity = entity;
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