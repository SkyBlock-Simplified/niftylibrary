package net.netcoding.niftybukkit.signs.events;

import org.bukkit.block.Sign;
import org.bukkit.entity.Player;
import org.bukkit.event.block.Action;

public class SignBreakEvent extends SignEvent {

	public SignBreakEvent(Player player, Sign sign) {
		super(player, sign, Action.LEFT_CLICK_BLOCK);
	}

}