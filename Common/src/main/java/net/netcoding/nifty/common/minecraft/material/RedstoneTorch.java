package net.netcoding.nifty.common.minecraft.material;

public interface RedstoneTorch extends Torch, Redstone {

	@Override
	RedstoneTorch clone();

	@Override
	default boolean isPowered() {
		return Material.REDSTONE_TORCH_ON == this.getItemType();
	}

}