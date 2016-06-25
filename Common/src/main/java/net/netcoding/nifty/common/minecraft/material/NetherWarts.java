package net.netcoding.nifty.common.minecraft.material;

import net.netcoding.nifty.common.minecraft.material.types.NetherWartsState;

public interface NetherWarts extends MaterialData {

	@Override
	NetherWarts clone();

	default NetherWartsState getState() {
		switch (this.getData()) {
			case 0:
				return NetherWartsState.SEEDED;
			case 1:
				return NetherWartsState.STAGE_ONE;
			case 2:
				return NetherWartsState.STAGE_TWO;
			default:
				return NetherWartsState.RIPE;
		}
	}

	default void setState(NetherWartsState state) {
		switch (state) {
			case SEEDED:
				setData((byte)0x0);
				return;
			case STAGE_ONE:
				setData((byte)0x1);
				return;
			case STAGE_TWO:
				setData((byte)0x2);
				return;
			case RIPE:
				setData((byte)0x3);
		}
	}


}