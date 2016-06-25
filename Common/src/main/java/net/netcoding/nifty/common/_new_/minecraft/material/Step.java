package net.netcoding.nifty.common._new_.minecraft.material;

import java.util.List;

public interface Step extends TexturedMaterial, Invertable {

	@Override
	Step clone();

	@Override
	default List<Material> getTextures() {
		return MaterialHelper.getTextures(Step.class);
	}

	@Override
	default int getTextureIndex() {
		return this.getData() & 0x7;
	}

	@Override
	default boolean isInverted() {
		return (this.getData() & 0x8) != 0;
	}

	@Override
	default void setInverted(boolean value) {
		int data = this.getData() & 0x7;

		if (value)
			data |= 0x8;

		this.setData((byte)data);
	}

	default void setTextureIndex(int index) {
		this.setData((byte) ((this.getData() & 0x8) | index));
	}

}