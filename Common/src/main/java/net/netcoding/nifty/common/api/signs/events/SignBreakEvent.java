package net.netcoding.nifty.common.api.signs.events;

import net.netcoding.nifty.common.api.signs.SignInfo;
import net.netcoding.nifty.common.mojang.MinecraftMojangProfile;
import net.netcoding.nifty.common.minecraft.block.Action;

/**
 * Class for sign specific break events.
 */
public final class SignBreakEvent extends SignEvent {

	public SignBreakEvent(MinecraftMojangProfile profile, SignInfo signInfo, String key) {
		super(profile, signInfo, Action.LEFT_CLICK_BLOCK, key);
	}

}