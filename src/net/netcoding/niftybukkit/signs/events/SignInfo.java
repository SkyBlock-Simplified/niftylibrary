package net.netcoding.niftybukkit.signs.events;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Sign;

public class SignInfo {

	private final Sign sign;
	private final String[] lines;
	private String[] modified;

	public SignInfo(Sign sign) {
		this.sign = sign;
		this.lines = sign.getLines();
		this.modified = sign.getLines();
	}

	public String getLine(int index) {
		return this.lines[index];
	}

	public Location getLocation() {
		return this.sign.getLocation();
	}

	public String[] getLines() {
		return this.lines;
	}

	public String getModifiedLine(int index) {
		return this.modified[index];
	}

	public String[] getModifiedLines() {
		return this.modified;
	}

	public World getWorld() {
		return this.getLocation().getWorld();
	}

	public int getX() {
		return this.getLocation().getBlockX();
	}

	public int getY() {
		return this.getLocation().getBlockY();
	}

	public int getZ() {
		return this.getLocation().getBlockZ();
	}

	public boolean isModified() {
		return !this.lines.equals(this.modified);
	}

	void setLine(int index, String value) {
		if ("".equals(value)) value = "";
		this.modified[index] = value;
	}

	public boolean update(boolean force, boolean applyPhysics) {
		return this.sign.update(force, applyPhysics);
	}

}