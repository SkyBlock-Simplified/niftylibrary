package net.netcoding.nifty.common.api.signs.events;

import net.netcoding.nifty.common.api.signs.SignInfo;
import net.netcoding.nifty.common.mojang.BukkitMojangProfile;
import net.netcoding.nifty.common.minecraft.block.Action;

/**
 * Class for sign specific break events.
 */
public final class SignBreakEvent extends SignEvent {

	public SignBreakEvent(BukkitMojangProfile profile, SignInfo signInfo, String key) {
		super(profile, signInfo, Action.LEFT_CLICK_BLOCK, key);
	}

}