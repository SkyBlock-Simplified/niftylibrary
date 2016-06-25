package net.netcoding.nifty.common._new_.api.signs.events;

import net.netcoding.nifty.common._new_.api.signs.SignInfo;
import net.netcoding.nifty.common._new_.mojang.BukkitMojangProfile;
import net.netcoding.nifty.common._new_.minecraft.block.Action;

/**
 * Class for sign specific interact events.
 */
public final class SignInteractEvent extends SignEvent {

	public SignInteractEvent(BukkitMojangProfile profile, SignInfo signInfo, Action action, String key) {
		super(profile, signInfo, action, key);
	}

}