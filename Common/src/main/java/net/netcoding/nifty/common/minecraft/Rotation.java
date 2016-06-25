package net.netcoding.nifty.common.minecraft;

public enum Rotation {

	NONE,
	CLOCKWISE_45,
	CLOCKWISE,
	CLOCKWISE_135,
	FLIPPED,
	FLIPPED_45,
	COUNTER_CLOCKWISE,
	COUNTER_CLOCKWISE_45;

	private static final Rotation[] ROTATIONS = values();

	public Rotation rotateClockwise() {
		return ROTATIONS[this.ordinal() + 1 & 7];
	}

	public Rotation rotateCounterClockwise() {
		return ROTATIONS[this.ordinal() - 1 & 7];
	}

}