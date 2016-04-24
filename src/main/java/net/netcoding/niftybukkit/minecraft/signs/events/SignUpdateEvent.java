package net.netcoding.niftybukkit.minecraft.signs.events;

import net.netcoding.niftybukkit.mojang.BukkitMojangProfile;
import net.netcoding.niftybukkit.minecraft.signs.SignInfo;
import org.bukkit.event.block.Action;

import java.util.regex.Pattern;

/**
 * Class for sign specific update events.
 */
public class SignUpdateEvent extends SignEvent {

	public SignUpdateEvent(BukkitMojangProfile profile, SignInfo signInfo, String key) {
		super(profile, signInfo, Action.PHYSICAL, key);
	}

	@Override
	public void setLine(int index, String value) {
		super.setLine(index, value);
	}

	/**
	 * Change the value from {@link #getKey()} to the given value.
	 *
	 * @param value Text to change key to.
	 */
	public void updateLine(String value) {
		for (int i = 0; i < 4; i++) {
			if (this.getLine(i).toLowerCase().contains(this.key))
				this.setLine(i, this.getLine(i).replaceAll("(?i)" + Pattern.quote(this.key), value));
		}
	}

}