package net.netcoding.nifty.common.minecraft.entity.living.human;

import net.netcoding.nifty.common.Nifty;
import net.netcoding.nifty.common.api.plugin.messaging.PluginMessageRecipient;
import net.netcoding.nifty.common.minecraft.OfflinePlayer;
import net.netcoding.nifty.common.minecraft.region.Location;
import net.netcoding.nifty.common.minecraft.region.World;
import net.netcoding.nifty.common.minecraft.sound.Sound;
import net.netcoding.nifty.core.mojang.OnlineProfile;
import net.netcoding.nifty.core.util.json.JsonMessage;

public interface Player extends HumanEntity, OfflinePlayer, OnlineProfile, PluginMessageRecipient {

	boolean canSee(Player player);

	void chat(String message);

	boolean getAllowFlight();

	String getDisplayName();

	float getExhaustion();

	float getExperience();

	float getFlySpeed();

	int getFoodLevel();

	int getLevel();

	String getPlayerListName();

	long getPlayerTime();

	long getPlayerTimeOffset();

	World.Weather getPlayerWeather();

	float getSaturation();

	int getTotalExperience();

	float getWalkSpeed();

	void giveExperience(int experience);

	void giveExperienceLevels(int levels);

	void hidePlayer(Player player);

	boolean isFlying();

	@Override
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

	default void playSound(Sound sound, float volume, float pitch) {
		this.playSound(sound.name(), volume, pitch); // TODO: Check
	}

	default void playSound(String sound, float volume, float pitch) {
		this.getWorld().playSound(this.getLocation(), sound, volume, pitch);
	}

	void resetPlayerTime();

	void resetPlayerWeather();

	void saveData();

	@Override
	default void sendMessage(JsonMessage message) throws Exception {
		Nifty.getMojangRepository().searchByPlayer(this).sendMessage(message);
	}

	void setAllowFlight(boolean value);

	default void setBedSpawnLocation(Location location) {
		this.setBedSpawnLocation(location, false);
	}

	void setBedSpawnLocation(Location location, boolean force);

	void setDisplayName(String displayName);

	void setExhaustion(float exhaustion);

	void setExperience(float experience);

	void setFlying(boolean value);

	void setFlySpeed(float speed) throws IllegalArgumentException;

	void setFoodLevel(int level);

	void setLevel(int level);

	void setSleepingIgnored(boolean value);

	void setPlayerListName(String playerListName);

	void setPlayerTime(long time, boolean relative);

	void setPlayerWeather(World.Weather weather);

	void setSaturation(float saturation);

	void setSneaking(boolean value);

	void setSprinting(boolean value);

	void setTotalExperience(int experience);

	void setWalkSpeed(float speed) throws IllegalArgumentException;

	void showPlayer(Player player);

	void updateInventory();

}