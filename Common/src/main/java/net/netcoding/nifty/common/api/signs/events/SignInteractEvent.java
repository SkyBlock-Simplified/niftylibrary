package net.netcoding.nifty.common.api.signs.events;

import net.netcoding.nifty.common.api.signs.SignInfo;
import net.netcoding.nifty.common.mojang.MinecraftMojangProfile;
import net.netcoding.nifty.common.minecraft.block.Action;

/**
 * Class for sign specific interact events.
 */
public final class SignInteractEvent extends SignEvent {

	public SignInteractEvent(MinecraftMojangProfile profile, SignInfo signInfo, Action action, String key) {
		super(profile, signInfo, action, key);
	}

}