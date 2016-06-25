package net.netcoding.nifty.common.minecraft.event.player;

import java.net.InetAddress;

public interface PlayerLoginEvent extends PlayerEvent {

	void allow();

	void disallow(Result result, String message);

	InetAddress getAddress();

	default String getHostname() {
		return this.getAddress().getHostName();
	}

	String getKickMessage();

	Result getResult();

	void setKickMessage(String message);

	void setResult(Result result);

	enum Result {

		ALLOWED,
		KICK_FULL,
		KICK_BANNED,
		KICK_WHITELIST,
		KICK_OTHER

	}

}