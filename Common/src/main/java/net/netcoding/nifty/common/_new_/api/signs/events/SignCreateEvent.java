package net.netcoding.nifty.common._new_.api.signs.events;

import net.netcoding.nifty.common._new_.api.signs.SignInfo;
import net.netcoding.nifty.common._new_.mojang.BukkitMojangProfile;
import net.netcoding.nifty.common._new_.minecraft.block.Action;

/**
 * Class for sign specific create events.
 */
public final class SignCreateEvent extends SignEvent {

	public SignCreateEvent(BukkitMojangProfile profile, SignInfo signInfo, String key) {
		super(profile, signInfo, Action.LEFT_CLICK_AIR, key);
	}

}