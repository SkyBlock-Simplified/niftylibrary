package net.netcoding.nifty.common._new_.minecraft.material;

import net.netcoding.nifty.core.util.concurrent.ConcurrentMap;

public interface Crops extends MaterialData {

	@Override
	Crops clone();

	default State getState() {
		switch (this.getItemType()) {
			case CROPS:
			case CARROT:
			case POTATO:
				// Mask the data just incase top bit set
				return State.getByData((byte)(this.getData() & 0x7));
			case BEETROOT_BLOCK:
			case NETHER_WARTS:
				// Mask the data just incase top bits are set
				// Will return SEEDED, SMALL, TALL, RIPE for the three growth data values
				return State.getByData((byte)(((this.getData() & 0x3) * 7 + 2) / 3));
		}

		throw new IllegalStateException("Block type is not a crop!");
	}

	default void setState(State state) {
		switch (this.getItemType()) {
			case CROPS:
			case CARROT:
			case POTATO:
				// Preserve the top bit in case it is set
				this.setData((byte)((this.getData() & 0x8) | state.getData()));
				break;
			case NETHER_WARTS:
			case BEETROOT_BLOCK:
				// Preserve the top bits in case they are set
				this.setData((byte)((this.getData() & 0xC) | (state.getData() >> 1)));
				break;
			default:
				throw new IllegalStateException("Block type is not a crop");
		}
	}

	enum State {

		/**
		 * State when first seeded
		 */
		SEEDED(0x0),
		/**
		 * First growth stage
		 */
		GERMINATED(0x1),
		/**
		 * Second growth stage
		 */
		VERY_SMALL(0x2),
		/**
		 * Third growth stage
		 */
		SMALL(0x3),
		/**
		 * Fourth growth stage
		 */
		MEDIUM(0x4),
		/**
		 * Fifth growth stage
		 */
		TALL(0x5),
		/**
		 * Almost ripe stage
		 */
		VERY_TALL(0x6),
		/**
		 * Ripe stage
		 */
		RIPE(0x7);

		private static final ConcurrentMap<Byte, State> BY_DATA = new ConcurrentMap<>();
		private final byte data;

		static {
			for (State state : values())
				BY_DATA.put(state.getData(), state);
		}

		State(final int data) {
			this.data = (byte) data;
		}

		public byte getData() {
			return data;
		}

		public static State getByData(byte data) {
			return BY_DATA.get(data);
		}

	}

}