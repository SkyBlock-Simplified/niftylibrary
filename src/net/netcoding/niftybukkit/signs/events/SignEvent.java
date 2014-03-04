package net.netcoding.niftybukkit.signs.events;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.bukkit.event.block.Action;

abstract class SignEvent extends Event implements Cancellable {

	private static final transient HandlerList handlers = new HandlerList();
	private final transient Player player;
	private final transient SignInfo signInfo;
	private final transient Action action;
	protected final String key;
	private boolean cancelled = false;

	SignEvent(Player player, SignInfo signInfo, Action action, String key) {
		this.player = player;
		this.signInfo = signInfo;
		this.action = action;
		this.key = key;
	}

	public static HandlerList getHandlerList() {
		return handlers;
	}

	public Action getAction() {
		return this.action;
	}

	@Override
	public HandlerList getHandlers() {
		return handlers;
	}

	public String getLine(int index) {
		return this.signInfo.getLine(index);
	}

	public String[] getLines() {
		return this.signInfo.getLines();
	}

	public Location getLocation() {
		return this.signInfo.getLocation();
	}

	public String getKey() {
		return this.key.replaceAll("[\\[\\]]", "");
	}

	public String getModifiedLine(int index) {
		return this.signInfo.getModifiedLine(index);
	}

	public String[] getModifiedLines() {
		return this.signInfo.getModifiedLines();
	}

	public Player getPlayer() {
		return this.player;
	}

	public World getWorld() {
		return this.signInfo.getWorld();
	}

	public int getX() {
		return this.signInfo.getX();
	}

	public int getY() {
		return this.signInfo.getY();
	}

	public int getZ() {
		return this.signInfo.getZ();
	}

	@Override
	public boolean isCancelled() {
		return this.cancelled;
	}

	public boolean isModified() {
		return this.signInfo.isModified();
	}

	@Override
	public void setCancelled(boolean cancelled) {
		this.cancelled = cancelled;
	}

	protected void setLine(int index, String value) {
		this.signInfo.setLine(index, value);
	}

	public boolean updateSign() {
		return this.updateSign(false);
	}

	public boolean updateSign(boolean force) {
		return this.updateSign(force, true);
	}

	public boolean updateSign(boolean force, boolean applyPhysics) {
		return this.signInfo.update(force, applyPhysics);
	}

}