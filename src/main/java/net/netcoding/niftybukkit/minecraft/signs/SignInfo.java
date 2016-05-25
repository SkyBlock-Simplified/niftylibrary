package net.netcoding.niftybukkit.minecraft.signs;

import net.netcoding.niftybukkit.minecraft.nbt.NbtCompound;
import net.netcoding.niftycore.util.RegexUtil;
import net.netcoding.niftycore.util.StringUtil;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.Sign;

import java.util.Arrays;

/**
 * Used to access sign methods as well as retrieve or modify the lines.
 */
public class SignInfo {

	private final transient Sign sign;
	private final String[] lines;
	private final String[] modified;

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

	static SignInfo fromCompound(World world, NbtCompound compound) {
		return new SignInfo((Sign)new Location(world, compound.<Integer>get("x"), compound.<Integer>get("y"), compound.<Integer>get("z")).getBlock().getState());
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
	 * Gets the color modified lines of text.
	 *
	 * @param filtered True to replace color codes, otherwise false.
	 * @return Modified sign text.
	 */
	public String[] getModifiedLines(boolean filtered) {
		String[] modified = this.getModifiedLines();

		if (filtered) {
			modified = modified.clone();

			for (int i = 0; i < modified.length; i++)
				modified[i] = RegexUtil.replaceColor(modified[i], RegexUtil.REPLACE_ALL_PATTERN);
		}

		return modified;
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