package net.netcoding.nifty.common._new_.minecraft.entity.living;

import net.netcoding.nifty.common.Nifty;
import net.netcoding.nifty.common._new_.api.plugin.messaging.PluginMessageRecipient;
import net.netcoding.nifty.common._new_.minecraft.BukkitServer;
import net.netcoding.nifty.common._new_.minecraft.GameMode;
import net.netcoding.nifty.common._new_.minecraft.OfflinePlayer;
import net.netcoding.nifty.common._new_.minecraft.region.Location;
import net.netcoding.nifty.common._new_.minecraft.region.World;
import net.netcoding.nifty.core.mojang.OnlineProfile;

import java.net.InetSocketAddress;

public interface Player extends HumanEntity, OfflinePlayer, OnlineProfile, PluginMessageRecipient {

	boolean canSee(Player player);

	void chat(String message);

	InetSocketAddress getAddress();

	boolean getAllowFlight();

	Location getBedSpawnLocation();

	String getDisplayName();

	float getExhaustion();

	float getExperience();

	float getFlySpeed();

	int getFoodLevel();

	GameMode getGameMode();

	int getLevel();

	String getPlayerListName();

	long getPlayerTime();

	long getPlayerTimeOffset();

	World.Weather getPlayerWeather();

	float getSaturation();

	int getTotalExperience();

	default BukkitServer getServer() {
		return Nifty.getServer();
	}

	float getWalkSpeed();

	void giveExperience(int experience);

	void giveExperienceLevels(int levels);

	void hidePlayer(Player player);

	boolean isFlying();

	@Deprecated
	boolean isOnGround();

	boolean isPlayerTimeRelative();

	boolean isSleepingIgnored();

	boolean isSneaking();

	boolean isSprinting();

	default void kick() {
		this.kick("You have been kicked!");
	}

	void kick(String message);

	void loadData();

	boolean performCommand(String command);

	void resetPlayerTime();

	void resetPlayerWeather();

	void saveData();

	void setAllowFlight(boolean value);

	void setDisplayName(String displayName);

	void setExhaustion(float exhaustion);

	void setExperience(float experience);

	void setFlying(boolean value);

	void setFlySpeed(float speed) throws IllegalArgumentException;

	void setFoodLevel(int level);

	void setGameMode(GameMode gameMode);

	void setLevel(int level);

	void setSleepingIgnored(boolean value);

	void setPlayerListName(String playerListName);

	void setPlayerWeather(World.Weather weather);

	void setSaturation(float saturation);

	void setSneaking(boolean value);

	void setSprinting(boolean value);

	void setTotalExperience(int experience);

	void setWalkSpeed(float speed) throws IllegalArgumentException;

	void showPlayer(Player player);

	void updateInventory();



	void setPlayerTime(long var1, boolean var3);

	void setBedSpawnLocation(Location location);

	void setBedSpawnLocation(Location location, boolean var2);

}