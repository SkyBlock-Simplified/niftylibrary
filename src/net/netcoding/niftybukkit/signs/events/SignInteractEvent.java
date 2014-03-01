package net.netcoding.niftybukkit.signs.events;

import org.bukkit.block.Sign;
import org.bukkit.entity.Player;
import org.bukkit.event.block.Action;

public class SignInteractEvent extends SignEvent {

	public SignInteractEvent(Player player, Sign sign, Action action, String key) {
		super(player, sign, action, key);
	}

}