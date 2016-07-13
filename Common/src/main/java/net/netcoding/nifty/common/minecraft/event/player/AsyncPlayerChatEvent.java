package net.netcoding.nifty.common.minecraft.event.player;

import net.netcoding.nifty.common.minecraft.entity.living.human.Player;
import net.netcoding.nifty.common.minecraft.event.Cancellable;

import java.util.Set;

public interface AsyncPlayerChatEvent extends Cancellable, PlayerEvent {

	String getFormat();

	String getMessage();

	Set<Player> getRecipients();

	void setFormat(String format);

	void setMessage(String message);

}