package net.netcoding.nifty.common.minecraft.event;

public interface Cancellable {

	boolean isCancelled();

	void setCancelled(boolean value);

}