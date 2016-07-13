package net.netcoding.nifty.common.api.signs.events;

import net.netcoding.nifty.common.api.signs.SignInfo;
import net.netcoding.nifty.common.mojang.MinecraftMojangProfile;
import net.netcoding.nifty.common.minecraft.block.Action;

/**
 * Class for sign specific create events.
 */
public final class SignCreateEvent extends SignEvent {

	public SignCreateEvent(MinecraftMojangProfile profile, SignInfo signInfo, String key) {
		super(profile, signInfo, Action.LEFT_CLICK_AIR, key);
	}

}