package net.netcoding.niftybukkit.signs.events;

import net.netcoding.niftybukkit.mojang.BukkitMojangProfile;
import net.netcoding.niftybukkit.signs.SignInfo;

import org.bukkit.event.block.Action;

/**
 * Class for sign specific interact events.
 */
public class SignInteractEvent extends SignEvent {

	public SignInteractEvent(BukkitMojangProfile profile, SignInfo signInfo, Action action, String key) {
		super(profile, signInfo, action, key);
	}

}