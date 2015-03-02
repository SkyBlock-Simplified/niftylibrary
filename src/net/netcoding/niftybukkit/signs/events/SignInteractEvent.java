package net.netcoding.niftybukkit.signs.events;

import net.netcoding.niftybukkit.signs.SignInfo;

import org.bukkit.entity.Player;
import org.bukkit.event.block.Action;

/**
 * Class for sign specific interact events.
 */
public class SignInteractEvent extends SignEvent {

	public SignInteractEvent(Player player, SignInfo signInfo, Action action, String key) {
		super(player, signInfo, action, key);
	}

}