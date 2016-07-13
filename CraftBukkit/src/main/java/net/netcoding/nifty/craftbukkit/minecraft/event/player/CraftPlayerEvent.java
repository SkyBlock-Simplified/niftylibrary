package net.netcoding.nifty.craftbukkit.minecraft.event.player;

import net.netcoding.nifty.common.minecraft.event.player.PlayerEvent;
import net.netcoding.nifty.common.mojang.MinecraftMojangProfile;
import net.netcoding.nifty.craftbukkit.minecraft.event.CraftEvent;

abstract class CraftPlayerEvent extends CraftEvent implements PlayerEvent {

	private final MinecraftMojangProfile profile;

	public CraftPlayerEvent(MinecraftMojangProfile profile, org.bukkit.event.player.PlayerEvent event) {
		super(event);
		this.profile = profile;
	}

	@Override
	public org.bukkit.event.player.PlayerEvent getHandle() {
		return (org.bukkit.event.player.PlayerEvent)super.getHandle();
	}

	@Override
	public MinecraftMojangProfile getProfile() {
		return this.profile;
	}

}