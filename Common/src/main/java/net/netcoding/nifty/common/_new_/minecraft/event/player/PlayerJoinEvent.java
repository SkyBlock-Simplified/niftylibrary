package net.netcoding.nifty.common._new_.minecraft.event.player;

public interface PlayerJoinEvent extends PlayerEvent {

	String getJoinMessage();

	void setJoinMessage(String message);

}