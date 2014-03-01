package net.netcoding.niftybukkit.signs.events;

import org.bukkit.entity.Player;
import org.bukkit.event.block.Action;

public class SignInteractEvent extends SignEvent {

	public SignInteractEvent(Player player, SignInfo signInfo, Action action, String key) {
		super(player, signInfo, action, key);
	}

}