package net.netcoding.nifty.common.minecraft.event.player;

public interface PlayerJoinEvent extends PlayerEvent {

	String getJoinMessage();

	void setJoinMessage(String message);

}