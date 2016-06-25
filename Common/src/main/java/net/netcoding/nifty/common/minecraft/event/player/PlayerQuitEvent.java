package net.netcoding.nifty.common.minecraft.event.player;

public interface PlayerQuitEvent extends PlayerEvent {

	String getQuitMessage();

	void setQuitMessage(String message);

}