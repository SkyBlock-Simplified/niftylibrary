package net.netcoding.nifty.common._new_.minecraft;

public enum GameMode {

	SURVIVAL(0),
	CREATIVE(1),
	ADVENTURE(2),
	SPECTATOR(3);

	private final int value;

	GameMode(int value) {
		this.value = value;
	}

	public int getValue() {
		return this.value;
	}

	public static GameMode valueOf(int value) {
		for (GameMode mode : values()) {
			if (mode.getValue() == value)
				return mode;
		}

		throw new IllegalArgumentException("Value must be between 0 and 3 inclusive!");
	}

}