package net.netcoding.nifty.common._new_.minecraft;

import net.netcoding.nifty.common._new_.minecraft.entity.AnimalTamer;
import net.netcoding.nifty.common._new_.minecraft.entity.living.Player;
import net.netcoding.nifty.common._new_.minecraft.permission.ServerOperator;
import net.netcoding.nifty.common._new_.minecraft.region.Location;
import net.netcoding.nifty.core.mojang.Profile;

public interface OfflinePlayer extends AnimalTamer, Profile, ServerOperator {

	Location getBedSpawnLocation();

	long getFirstPlayed();

	long getLastPlayed();

	Player getPlayer();

	boolean hasPlayedBefore();

	boolean isBanned();

	boolean isWhitelisted();

	void setBanned(boolean value);

	void setWhitelisted(boolean value);

}