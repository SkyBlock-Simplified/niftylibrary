package net.netcoding.niftybukkit.signs.events;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.bukkit.event.block.Action;

class SignEvent extends Event implements Cancellable {

	private static final transient HandlerList handlers = new HandlerList();
	private final transient Player player;
	private final transient Sign sign;
	private final transient Action action;
	private transient boolean cancelled = false;

	SignEvent(Player player, Sign sign, Action action) {
		this.player = player;
		this.sign = sign;
		this.action = action;
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
		return this.sign.getLine(index);
	}

	public String[] getLines() {
		return this.sign.getLines();
	}

	public Location getLocation() {
		return this.sign.getLocation();
	}

	public Player getPlayer() {
		return this.player;
	}

	public World getWorld() {
		return this.sign.getWorld();
	}

	public int getX() {
		return this.sign.getX();
	}

	public int getY() {
		return this.sign.getY();
	}

	public int getZ() {
		return this.sign.getZ();
	}

	@Override
	public boolean isCancelled() {
		return this.cancelled;
	}

	public void setCancelled() {
		this.setCancelled(true);
	}

	@Override
	public void setCancelled(boolean cancelled) {
		this.cancelled = cancelled;
	}

	public boolean updateSign() {
		return this.updateSign(false);
	}

	public boolean updateSign(boolean force) {
		return this.updateSign(force, true);
	}

	public boolean updateSign(boolean force, boolean applyPhysics) {
		return this.sign.update(force, applyPhysics);
	}

}