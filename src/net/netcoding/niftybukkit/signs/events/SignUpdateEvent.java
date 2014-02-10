package net.netcoding.niftybukkit.signs.events;

import org.bukkit.block.Sign;
import org.bukkit.entity.Player;
import org.bukkit.event.block.Action;

public class SignUpdateEvent extends SignEvent {

	private String[] lines;
	private boolean modified = false;
	private final String key;
	private final int index;

	public SignUpdateEvent(Player player, Sign sign, int index, String key) {
		super(player, sign, Action.PHYSICAL);
		this.lines = sign.getLines();
		this.index = index;
		this.key = key;
	}

	@Override
	public String getLine(int index) {
		return this.lines[index];
	}

	@Override
	public String[] getLines() {
		return this.lines;
	}

	public String getKey() {
		return this.key.replaceAll("[\\[\\]]", "");
	}

	public boolean isModified() {
		return this.modified;
	}

	public void setLine(int index, String value) throws IndexOutOfBoundsException {
		if ("".equals(value)) value = "";
		this.modified = true;
		this.lines[index] = value;
	}

	public void replaceKey(String value) {
		String current = this.getLine(this.index);
		if ("".equals(current)) current = "";
		if ("".equals(value)) value = "";
		this.modified = true;
		this.setLine(this.index, current.replace(this.key, value));
	}

}