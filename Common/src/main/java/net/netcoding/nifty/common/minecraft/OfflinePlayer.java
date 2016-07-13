package net.netcoding.nifty.common.minecraft;

import net.netcoding.nifty.common.minecraft.entity.AnimalTamer;
import net.netcoding.nifty.common.minecraft.entity.living.human.Player;
import net.netcoding.nifty.common.minecraft.permission.ServerOperator;
import net.netcoding.nifty.common.minecraft.region.Location;
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