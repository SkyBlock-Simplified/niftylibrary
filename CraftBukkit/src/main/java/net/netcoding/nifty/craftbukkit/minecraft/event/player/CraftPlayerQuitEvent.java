package net.netcoding.nifty.craftbukkit.minecraft.event.player;

import net.netcoding.nifty.common.minecraft.event.player.PlayerQuitEvent;
import net.netcoding.nifty.common.mojang.MinecraftMojangProfile;

public class CraftPlayerQuitEvent extends CraftPlayerEvent implements PlayerQuitEvent {

	public CraftPlayerQuitEvent(MinecraftMojangProfile profile, org.bukkit.event.player.PlayerQuitEvent event) {
		super(profile, event);
	}

	@Override
	public org.bukkit.event.player.PlayerQuitEvent getHandle() {
		return (org.bukkit.event.player.PlayerQuitEvent)super.getHandle();
	}

	@Override
	public String getQuitMessage() {
		return this.getHandle().getQuitMessage();
	}

	@Override
	public void setQuitMessage(String message) {
		this.getHandle().setQuitMessage(message);
	}

}