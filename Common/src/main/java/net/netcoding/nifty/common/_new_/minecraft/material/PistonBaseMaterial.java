package net.netcoding.nifty.common._new_.minecraft.material;

public interface PistonBaseMaterial extends PistonMaterial, OptionalRedstone {

	@Override
	PistonBaseMaterial clone();

	@Override
	default boolean isPowered() {
		return (this.getData() & 0x8) == 0x8;
	}

	@Override
	default boolean isSticky() {
		return Material.PISTON_STICKY_BASE == this.getItemType();
	}

	@Override
	default void setPowered(boolean value) {
		this.setData((byte)(value ? (this.getData() | 0x8) : (this.getData() & ~0x8)));
	}

}