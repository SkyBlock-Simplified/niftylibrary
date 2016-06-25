package net.netcoding.nifty.common.minecraft.event.block;

public interface BlockExpEvent extends BlockEvent {

	int getExpToDrop();

	void setExpToDrop(int experience);

}