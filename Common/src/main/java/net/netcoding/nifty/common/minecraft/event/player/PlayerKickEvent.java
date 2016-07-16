package net.netcoding.nifty.common.minecraft.event.player;

import net.netcoding.nifty.common.minecraft.event.Cancellable;

public interface PlayerKickEvent extends PlayerEvent, Cancellable {

	String getLeaveMessage();

	String getReason();

	void setLeaveMessage(String leaveMessage);

	void setReason(String kickReason);

}