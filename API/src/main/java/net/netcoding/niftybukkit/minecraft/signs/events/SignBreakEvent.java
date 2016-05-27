package net.netcoding.niftybukkit.minecraft.signs.events;

import net.netcoding.niftybukkit.mojang.BukkitMojangProfile;
import net.netcoding.niftybukkit.minecraft.signs.SignInfo;

import org.bukkit.event.block.Action;

/**
 * Class for sign specific break events.
 */
public class SignBreakEvent extends SignEvent {

	public SignBreakEvent(BukkitMojangProfile profile, SignInfo signInfo, String key) {
		super(profile, signInfo, Action.LEFT_CLICK_BLOCK, key);
	}

}