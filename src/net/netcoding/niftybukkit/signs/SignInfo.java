package net.netcoding.niftybukkit.signs;

import java.util.Arrays;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.Sign;

public class SignInfo {

	private final Sign sign;
	private final String[] lines;
	private String[] modified;

	public SignInfo(Sign sign) {
		this.sign = sign;
		this.lines = sign.getLines();
		this.modified = sign.getLines().clone();
	}

	public Block getBlock() {
		return this.sign.getBlock();
	}

	public String getLine(int index) {
		return this.lines[index];
	}

	public String[] getLines() {
		return this.lines;
	}

	public Location getLocation() {
		return this.sign.getLocation();
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
		return !Arrays.equals(this.lines, this.modified);
	}

	public void setLine(int index, String value) {
		if ("".equals(value)) value = "";
		if (value.length() > 15) value = value.substring(0, 15);
		this.modified[index] = value;
	}

	public boolean update(boolean force, boolean applyPhysics) {
		return this.sign.update(force, applyPhysics);
	}

}