package net.netcoding.niftybukkit.signs.events;

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
			if (this.getLine(i).contains(this.key)) {
				this.setLine(i, value);
				break;
			}
		}
	}

}