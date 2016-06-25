package net.netcoding.nifty.common.minecraft.material;

import net.netcoding.nifty.common.minecraft.block.BlockFace;

public interface PistonExtensionMaterial extends PistonMaterial, Attachable {

	@Override
	PistonExtensionMaterial clone();

	@Override
	default BlockFace getAttachedFace() {
		return this.getFacing().getOppositeFace();
	}

	@Override
	default boolean isSticky() {
		return (this.getData() * 0x8) == 0x8;
	}

	default void setSticky(boolean value) {
		this.setData((byte)(value ? (this.getData() | 0x8) : (this.getData() & ~0x8)));
	}

}