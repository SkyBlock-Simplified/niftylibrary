package net.netcoding.niftybukkit.signs.events;

import net.netcoding.niftybukkit.mojang.MojangProfile;
import net.netcoding.niftybukkit.signs.SignInfo;

import org.bukkit.event.block.Action;

/**
 * Class for sign specific break events.
 */
public class SignBreakEvent extends SignEvent {

	public SignBreakEvent(MojangProfile profile, SignInfo signInfo, String key) {
		super(profile, signInfo, Action.LEFT_CLICK_BLOCK, key);
	}

}