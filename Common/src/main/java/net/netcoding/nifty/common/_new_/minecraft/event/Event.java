package net.netcoding.nifty.common._new_.minecraft.event;

public abstract class Event {

	private final String name = this.getClass().getSimpleName();
	private final boolean isAsync;

	protected Event() {
		this(false);
	}

	protected Event(boolean isAsync) {
		this.isAsync = isAsync;
	}

	public String getEventName() {
		return this.name;
	}

	public final boolean isAsynchronous() {
		return this.isAsync;
	}

	public enum Result {
		DENY,
		DEFAULT,
		ALLOW
	}

}