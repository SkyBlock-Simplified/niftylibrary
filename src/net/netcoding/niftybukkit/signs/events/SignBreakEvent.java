package net.netcoding.niftybukkit.signs.events;

import net.netcoding.niftybukkit.signs.SignInfo;

import org.bukkit.entity.Player;
import org.bukkit.event.block.Action;

/**
 * Class for sign specific break events.
 */
public class SignBreakEvent extends SignEvent {

	public SignBreakEvent(Player player, SignInfo signInfo, String key) {
		super(player, signInfo, Action.LEFT_CLICK_BLOCK, key);
	}

}