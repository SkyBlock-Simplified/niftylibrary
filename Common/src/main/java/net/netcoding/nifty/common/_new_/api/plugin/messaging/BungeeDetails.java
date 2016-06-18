package net.netcoding.nifty.common._new_.api.plugin.messaging;

public final class BungeeDetails {

	private String socketIp = null;
	private int socketPort = -1;
	private boolean detected = false;
	private boolean onlineMode = false;
	private boolean socketTriedOnce = true;//false;

	public final boolean isDetected() {
		return this.detected;
	}

	public final boolean isOnlineMode() {
		return this.onlineMode;
	}

	public final String getSocketIp() {
		return this.socketIp;
	}

	public final int getSocketPort() {
		return this.socketPort;
	}

	public final boolean isSocketTriedOnce() {
		return this.socketTriedOnce;
	}

	void setDetected(boolean detected) {
		this.detected = detected;
	}

	void setOnlineMode(boolean onlineMode) {
		this.onlineMode = onlineMode;
	}

	void setSocketIp(String ip) {
		this.socketIp = ip;
	}

	void setSocketPort(int port) {
		this.socketPort = port;
	}

	void setSocketTriedOnce() {
		this.socketTriedOnce = true;
	}
}