package net.netcoding.nifty.craftbukkit.minecraft;

import net.netcoding.nifty.common.Nifty;
import net.netcoding.nifty.common.minecraft.OfflinePlayer;
import net.netcoding.nifty.common.minecraft.entity.living.human.Player;
import net.netcoding.nifty.common.minecraft.region.Location;
import net.netcoding.nifty.craftbukkit.minecraft.region.CraftLocation;

import java.util.UUID;

@SuppressWarnings("deprecation")
public final class CraftOfflinePlayer implements OfflinePlayer {

	private final org.bukkit.OfflinePlayer offlinePlayer;

	public CraftOfflinePlayer(org.bukkit.OfflinePlayer offlinePlayer) {
		this.offlinePlayer = offlinePlayer;
	}

	@Override
	public Location getBedSpawnLocation() {
		return new CraftLocation(this.getHandle().getBedSpawnLocation());
	}

	@Override
	public long getFirstPlayed() {
		return this.getHandle().getFirstPlayed();
	}

	public org.bukkit.OfflinePlayer getHandle() {
		return this.offlinePlayer;
	}

	@Override
	public long getLastPlayed() {
		return this.getHandle().getLastPlayed();
	}

	@Override
	public Player getPlayer() {
		return Nifty.getServer().getPlayer(this.getUniqueId());
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
	public boolean isWhitelisted() {
		return this.getHandle().isWhitelisted();
	}

	@Override
	public void setBanned(boolean value) {
		this.getHandle().setBanned(value);
	}

	@Override
	public void setWhitelisted(boolean value) {
		this.getHandle().setWhitelisted(value);
	}

	@Override
	public String getName() {
		return this.getHandle().getName();
	}

	@Override
	public UUID getUniqueId() {
		return this.getHandle().getUniqueId();
	}

	@Override
	public boolean isOp() {
		return this.getHandle().isOp();
	}

	@Override
	public void setOp(boolean value) {
		this.getHandle().setOp(value);
	}

}