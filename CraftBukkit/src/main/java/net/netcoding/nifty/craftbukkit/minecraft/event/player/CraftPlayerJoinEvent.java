package net.netcoding.nifty.craftbukkit.minecraft.event.player;

import net.netcoding.nifty.common.minecraft.event.player.PlayerJoinEvent;
import net.netcoding.nifty.common.mojang.MinecraftMojangProfile;

public class CraftPlayerJoinEvent extends CraftPlayerEvent implements PlayerJoinEvent {

	public CraftPlayerJoinEvent(MinecraftMojangProfile profile, org.bukkit.event.player.PlayerJoinEvent playerJoinEvent) {
		super(profile, playerJoinEvent);
	}

	@Override
	public org.bukkit.event.player.PlayerJoinEvent getHandle() {
		return (org.bukkit.event.player.PlayerJoinEvent)super.getHandle();
	}

	@Override
	public String getJoinMessage() {
		return this.getHandle().getJoinMessage();
	}

	@Override
	public void setJoinMessage(String message) {
		this.getHandle().setJoinMessage(message);
	}

}