package net.netcoding.niftybukkit.signs.events;

import net.netcoding.niftybukkit.mojang.MojangProfile;
import net.netcoding.niftybukkit.signs.SignInfo;
import net.netcoding.niftybukkit.util.RegexUtil;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.event.Cancellable;
import org.bukkit.event.block.Action;

/**
 * Base class for sign specific events.
 */
abstract class SignEvent implements Cancellable {

	private final transient MojangProfile profile;
	private final transient SignInfo signInfo;
	private final transient Action action;
	protected final String key;
	private boolean cancelled = false;

	SignEvent(MojangProfile profile, SignInfo signInfo, Action action, String key) {
		this.profile = profile;
		this.signInfo = signInfo;
		this.action = action;
		this.key = key;
	}

	/**
	 * Action taken on the event.
	 * 
	 * @return Left/right click block if interact, physical if break/update and left click air on create.
	 */
	public Action getAction() {
		return this.action;
	}

	/**
	 * Get the block of the sign.
	 * 
	 * @return Sign block.
	 */
	public Block getBlock() {
		return this.signInfo.getBlock();
	}

	/**
	 * Gets the key of the current event.
	 * 
	 * @return Key of this event.
	 */
	public String getKey() {
		return this.key.replaceAll("[\\[\\]]", "");
	}

	/**
	 * Gets the original line of text, given the index.
	 * 
	 * @param index Line number to retrieve.
	 * @return Unmodified sign text from given line number.
	 */
	public String getLine(int index) {
		return this.signInfo.getLine(index);
	}

	/**
	 * Gets the original lines on the sign.
	 * 
	 * @return Unmodified sign text.
	 */
	public String[] getLines() {
		return this.signInfo.getLines();
	}

	/**
	 * Gets the location of the sign.
	 * 
	 * @return Location of the sign.
	 */
	public Location getLocation() {
		return this.signInfo.getLocation();
	}

	/**
	 * Gets the modified line of text, given the index.
	 * 
	 * @param index Line number to retrieve.
	 * @return Modified sign text from given line number.
	 */
	public String getModifiedLine(int index) {
		return this.signInfo.getModifiedLine(index);
	}

	/**
	 * Gets the modified lines of text.
	 * 
	 * @return Modified sign text.
	 */
	public String[] getModifiedLines() {
		return this.signInfo.getModifiedLines();
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
	 * Gets the profile associated with the event.
	 * 
	 * @return Profile of who performed the event.
	 */
	public MojangProfile getProfile() {
		return this.profile;
	}

	/**
	 * Gets the world the sign belongs to.
	 * 
	 * @return World the sign belongs to.
	 */
	public World getWorld() {
		return this.signInfo.getWorld();
	}

	/**
	 * Gets if the event is cancelled.
	 * 
	 * @return True if cancelled, otherwise false.
	 */
	@Override
	public boolean isCancelled() {
		return this.cancelled;
	}

	/**
	 * Gets if the sign has been modified.
	 * 
	 * @return True if modified, otherwise false.
	 */
	public boolean isModified() {
		return this.signInfo.isModified();
	}

	/**
	 * Prevents the event from occurring.
	 * 
	 * @param cancelled True to cancel, otherwise false.
	 */
	@Override
	public void setCancelled(boolean cancelled) {
		this.cancelled = cancelled;
	}

	/**
	 * Modify the text on the sign, given the index.
	 * 
	 * @param index The line number to modify.
	 * @param value The text to change it to.
	 */
	protected void setLine(int index, String value) {
		this.signInfo.setLine(index, value);
	}

}