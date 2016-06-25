package net.netcoding.nifty.common._new_.minecraft.event.player;

import net.netcoding.nifty.common._new_.minecraft.entity.living.Player;
import net.netcoding.nifty.common._new_.minecraft.event.Cancellable;

import java.util.Set;

public interface AsyncPlayerChatEvent extends Cancellable, PlayerEvent {

	String getFormat();

	String getMessage();

	Set<Player> getRecipients();

	void setFormat(String format);

	void setMessage(String message);

}