package net.netcoding.niftybukkit.signs.events;

import net.netcoding.niftybukkit.mojang.MojangProfile;
import net.netcoding.niftybukkit.signs.SignInfo;

import org.bukkit.event.block.Action;

/**
 * Class for sign specific create events.
 */
public class SignCreateEvent extends SignEvent {

	public SignCreateEvent(MojangProfile profile, SignInfo signInfo, String key) {
		super(profile, signInfo, Action.LEFT_CLICK_AIR, key);
	}

}