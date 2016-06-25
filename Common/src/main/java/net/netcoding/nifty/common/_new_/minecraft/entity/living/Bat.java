package net.netcoding.nifty.common._new_.minecraft.entity.living;

/**
 * Represents a Bat.
 */
public interface Bat extends Ambient {

	/**
	 * Checks the current waking state of this bat.
	 * <p>
	 * This does not imply any persistence of state past the method call.
	 *
	 * @return True if the bat is awake, false if hanging from a block.
	 */
	boolean isAwake();

	/**
	 * This method modifies the current waking state of this bat.
	 * <p>
	 * This does not prevent a bat from spontaneously awaking itself, or from
	 * reattaching itself to a block.
	 *
	 * @param state The new state.
	 */
	void setAwake(boolean state);
}