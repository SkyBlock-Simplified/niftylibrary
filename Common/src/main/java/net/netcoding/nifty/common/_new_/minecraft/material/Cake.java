package net.netcoding.nifty.common._new_.minecraft.material;

public interface Cake extends MaterialData {

	@Override
	Cake clone();

	default int getSlicesEaten() {
		return this.getData();
	}

	default int getSlicesRemaining() {
		return 6 - this.getSlicesEaten();
	}

	default void setSlicesEaten(int slices) {
		if (slices > 5)
			slices = 5;

		this.setData((byte)slices);
	}

	default void setSlicesRemaining(int slices) {
		if (slices > 6)
			slices = 6;

		this.setData((byte)(6 - slices));
	}

}