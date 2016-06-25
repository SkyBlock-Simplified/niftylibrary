package net.netcoding.nifty.common._new_.minecraft.event.player;

public interface PlayerQuitEvent extends PlayerEvent {

	String getQuitMessage();

	void setQuitMessage(String message);

}