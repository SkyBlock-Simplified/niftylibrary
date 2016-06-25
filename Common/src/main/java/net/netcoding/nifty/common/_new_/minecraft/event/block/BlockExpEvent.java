package net.netcoding.nifty.common._new_.minecraft.event.block;

public interface BlockExpEvent extends BlockEvent {

	int getExpToDrop();

	void setExpToDrop(int experience);

}