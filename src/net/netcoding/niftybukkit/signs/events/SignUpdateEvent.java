package net.netcoding.niftybukkit.signs.events;

import java.util.regex.Pattern;

import net.netcoding.niftybukkit.signs.SignInfo;

import org.bukkit.entity.Player;
import org.bukkit.event.block.Action;

public class SignUpdateEvent extends SignEvent {

	public SignUpdateEvent(Player player, SignInfo signInfo, String key) {
		super(player, signInfo, Action.PHYSICAL, key);
	}

	@Override
	public void setLine(int index, String value) {
		super.setLine(index, value);
	}

	public void updateLine(String value) {
		for (int i = 0; i < 4; i++) {
			if (this.getLine(i).toLowerCase().contains(this.key.toLowerCase()))
				this.setLine(i, this.getLine(i).replaceAll("(?i)" + Pattern.quote(this.key), value));
		}
	}

}