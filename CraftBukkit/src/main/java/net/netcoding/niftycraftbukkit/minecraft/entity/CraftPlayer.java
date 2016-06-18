package net.netcoding.niftycraftbukkit.minecraft.entity;

import net.netcoding.niftybukkit._new_.minecraft.GameMode;
import net.netcoding.niftybukkit._new_.minecraft.region.Location;
import net.netcoding.niftybukkit._new_.minecraft.entity.living.Player;
import net.netcoding.niftycore.util.json.JsonMessage;

import java.net.InetSocketAddress;
import java.util.UUID;

public final class CraftPlayer implements Player {

	private final org.bukkit.entity.Player player;

	public CraftPlayer(org.bukkit.entity.Player player) {
		this.player = player;
	}

	@Override
	public InetSocketAddress getAddress() {
		return this.getHandle().getAddress();
	}

	@Override
	public Location getBedSpawnLocation() {
		return null; // TODO this.getHandle().getBedSpawnLocation();
	}

	@Override
	public GameMode getGameMode() {
		return GameMode.valueOf(this.getHandle().getGameMode().name());
	}

	@Override
	public long getFirstPlayed() {
		return this.getHandle().getFirstPlayed();
	}

	@Override
	public long getLastPlayed() {
		return this.getHandle().getLastPlayed();
	}

	protected org.bukkit.entity.Player getHandle() {
		return this.player;
	}

	@Override
	public String getName() {
		return this.getHandle().getName();
	}

	@Override
	public Player getPlayer() {
		return this;
	}

	@Override
	public UUID getUniqueId() {
		return this.getHandle().getUniqueId();
	}

	@Override
	public boolean hasPlayedBefore() {
		return this.getHandle().hasPlayedBefore();
	}

	@Override
	public boolean isBanned() {
		return this.getHandle().isBanned();
	}

	@Override
	public boolean isOnline() {
		return this.getHandle().isOnline();
	}

	@Override
	public boolean isOp() {
		return this.getHandle().isOp();
	}

	@Override
	public boolean isWhitelisted() {
		return this.getHandle().isWhitelisted();
	}

	@Override
	public void sendMessage(JsonMessage message) throws Exception {
		// TODO
	}

	@Override
	public void sendMessage(String message) {
		this.getHandle().sendMessage(message);
	}

	@Override
	public void setBanned(boolean value) {
		this.getHandle().setBanned(value);
	}

	@Override
	public void setGameMode(GameMode gameMode) {
		this.getHandle().setGameMode(org.bukkit.GameMode.valueOf(gameMode.name()));
	}

	@Override
	public void setOp(boolean value) {
		this.getHandle().setOp(value);
	}

	@Override
	public void setWhitelisted(boolean value) {
		this.getHandle().setWhitelisted(value);
	}

}