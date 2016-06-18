package net.netcoding.niftybukkit._new_.api.signs.events;

import net.netcoding.niftybukkit._new_.api.signs.SignInfo;
import net.netcoding.niftybukkit._new_.minecraft.block.Action;
import net.netcoding.niftybukkit._new_.mojang.BukkitMojangProfile;

/**
 * Class for sign specific interact events.
 */
public class SignInteractEvent extends SignEvent {

	public SignInteractEvent(BukkitMojangProfile profile, SignInfo signInfo, Action action, String key) {
		super(profile, signInfo, action, key);
	}

}