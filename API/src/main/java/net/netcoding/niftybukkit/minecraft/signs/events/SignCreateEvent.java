package net.netcoding.niftybukkit.minecraft.signs.events;

import net.netcoding.niftybukkit.mojang.BukkitMojangProfile;
import net.netcoding.niftybukkit.minecraft.signs.SignInfo;

import org.bukkit.event.block.Action;

/**
 * Class for sign specific create events.
 */
public class SignCreateEvent extends SignEvent {

	public SignCreateEvent(BukkitMojangProfile profile, SignInfo signInfo, String key) {
		super(profile, signInfo, Action.LEFT_CLICK_AIR, key);
	}

}