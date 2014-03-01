package net.netcoding.niftybukkit.signs.events;

import org.bukkit.entity.Player;
import org.bukkit.event.block.Action;

public class SignCreateEvent extends SignEvent {

	public SignCreateEvent(Player player, SignInfo signInfo, String key) {
		super(player, signInfo, Action.LEFT_CLICK_AIR, key);
	}

}