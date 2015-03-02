package net.netcoding.niftybukkit.signs;

import java.util.Arrays;

import net.netcoding.niftybukkit.util.StringUtil;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.Sign;

/**
 * Used to access sign methods as well as retrieve or modify the lines.
 */
public class SignInfo {

	private final transient Sign sign;
	private final String[] lines;
	private String[] modified;

	/**
	 * Create new sign info instance.
	 * 
	 * @param sign Sign to get/set data with.
	 */
	SignInfo(Sign sign) {
		this.sign = sign;
		this.lines = sign.getLines();
		this.modified = sign.getLines().clone();
	}

	/**
	 * Get the block of the sign.
	 * 
	 * @return Sign block.
	 */
	public Block getBlock() {
		return this.sign.getBlock();
	}

	/**
	 * Gets the original line of text, given the index.
	 * 
	 * @param index Line number to retrieve.
	 * @return Unmodified sign text from given line number.
	 */
	public String getLine(int index) {
		return this.lines[index];
	}

	/**
	 * Gets the original lines on the sign.
	 * 
	 * @return Unmodified sign text.
	 */
	public String[] getLines() {
		return this.lines;
	}

	/**
	 * Gets the location of the sign.
	 * 
	 * @return Location of the sign.
	 */
	public Location getLocation() {
		return this.sign.getLocation();
	}

	/**
	 * Gets the modified line of text, given the index.
	 * 
	 * @param index Line number to retrieve.
	 * @return Modified sign text from given line number.
	 */
	public String getModifiedLine(int index) {
		return this.modified[index];
	}

	/**
	 * Gets the modified lines of text.
	 * 
	 * @return Modified sign text.
	 */
	public String[] getModifiedLines() {
		return this.modified;
	}

	/**
	 * Gets the world the sign belongs to.
	 * 
	 * @return World the sign belongs to.
	 */
	public World getWorld() {
		return this.getLocation().getWorld();
	}

	/**
	 * Gets the X value for the location of the sign.
	 * 
	 * @return X coordinate of the sign.
	 */
	public int getX() {
		return this.getLocation().getBlockX();
	}

	/**
	 * Gets the Y value for the location of the sign.
	 * 
	 * @return Y coordinate of the sign.
	 */
	public int getY() {
		return this.getLocation().getBlockY();
	}

	/**
	 * Gets the Z value for the location of the sign.
	 * 
	 * @return Z coordinate of the sign.
	 */
	public int getZ() {
		return this.getLocation().getBlockZ();
	}

	/**
	 * Gets if the sign has been modified.
	 * 
	 * @return True if modified, otherwise false.
	 */
	public boolean isModified() {
		return !Arrays.equals(this.lines, this.modified);
	}

	/**
	 * Modify the text on the sign, given the index.
	 * 
	 * @param index The line number to modify.
	 * @param value The text to change it to.
	 */
	public void setLine(int index, String value) {
		if (StringUtil.isEmpty(value)) value = "";
		if (value.length() > 15) value = value.substring(0, 15);
		this.modified[index] = value;
	}

}