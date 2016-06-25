package net.netcoding.nifty.common._new_.minecraft.material;

import net.netcoding.nifty.core.util.concurrent.ConcurrentMap;

public interface Coal extends MaterialData {

	@Override
	Coal clone();

	default CoalType getType() {
		return CoalType.getByData(this.getData());
	}

	default void setType(CoalType type) {
		this.setData(type.getData());
	}

	enum CoalType {

		COAL(0),
		CHARCOAL(1);

		private static final ConcurrentMap<Byte, CoalType> BY_DATA = new ConcurrentMap<>();
		private final byte data;

		static {
			for (CoalType type : values())
				BY_DATA.put(type.getData(), type);
		}

		CoalType(int data) {
			this.data = (byte)data;
		}

		public byte getData() {
			return this.data;
		}

		public static CoalType getByData(byte data) {
			return BY_DATA.get(data);
		}

	}

}