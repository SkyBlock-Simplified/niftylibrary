package net.netcoding.niftybukkit.database.pooling;

/**
 * Represents time to wait for a connection. Ensures unified waiting intervals.
 */
public enum WaitTime {

	IMMEDIATELY(0),
	HALF_A_SECOND(500),
	ONE_SECOND(1000),
	THREE_SECONDS(3000),
	FIVE_SECONDS(5000);

	private int waitTime;

	private WaitTime(int milliseconds) {
		this.waitTime = milliseconds;
	}

	public int getWaitTime() {
		return waitTime;
	}

}