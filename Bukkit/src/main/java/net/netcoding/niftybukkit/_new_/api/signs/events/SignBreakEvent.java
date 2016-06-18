package net.netcoding.niftybukkit._new_.api.signs.events;

import net.netcoding.niftybukkit._new_.api.signs.SignInfo;
import net.netcoding.niftybukkit._new_.minecraft.block.Action;
import net.netcoding.niftybukkit._new_.mojang.BukkitMojangProfile;

/**
 * Class for sign specific break events.
 */
public class SignBreakEvent extends SignEvent {

	public SignBreakEvent(BukkitMojangProfile profile, SignInfo signInfo, String key) {
		super(profile, signInfo, Action.LEFT_CLICK_BLOCK, key);
	}

}