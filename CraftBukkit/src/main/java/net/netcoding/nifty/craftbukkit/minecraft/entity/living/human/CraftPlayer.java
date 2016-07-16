package net.netcoding.nifty.craftbukkit.minecraft.entity.living.human;

import net.netcoding.nifty.common.Nifty;
import net.netcoding.nifty.common.api.plugin.MinecraftPlugin;
import net.netcoding.nifty.common.minecraft.entity.living.human.Player;
import net.netcoding.nifty.common.minecraft.region.Location;
import net.netcoding.nifty.common.minecraft.region.World;
import net.netcoding.nifty.craftbukkit.minecraft.region.CraftLocation;

import java.net.InetSocketAddress;
import java.util.Set;

@SuppressWarnings("deprecation")
public final class CraftPlayer extends CraftHumanEntity implements Player {

	public CraftPlayer(org.bukkit.entity.Player player) {
		super(player);
	}

	@Override
	public boolean canSee(Player player) {
		return this.getHandle().canSee(((CraftPlayer)player).getHandle());
	}

	@Override
	public void chat(String message) {
		this.getHandle().chat(message);
	}

	@Override
	public InetSocketAddress getAddress() {
		return this.getHandle().getAddress();
	}

	@Override
	public boolean getAllowFlight() {
		return this.getHandle().getAllowFlight();
	}

	@Override
	public Location getBedSpawnLocation() {
		return new CraftLocation(this.getHandle().getBedSpawnLocation());
	}

	@Override
	public String getDisplayName() {
		return this.getHandle().getDisplayName();
	}

	@Override
	public float getExhaustion() {
		return this.getHandle().getExhaustion();
	}

	@Override
	public float getExperience() {
		return this.getHandle().getExp();
	}

	@Override
	public long getFirstPlayed() {
		return this.getHandle().getFirstPlayed();
	}

	@Override
	public float getFlySpeed() {
		return this.getHandle().getFlySpeed();
	}

	@Override
	public int getFoodLevel() {
		return this.getHandle().getFoodLevel();
	}

	@Override
	public org.bukkit.entity.Player getHandle() {
		return (org.bukkit.entity.Player)super.getHandle();
	}

	@Override
	public long getLastPlayed() {
		return this.getHandle().getLastPlayed();
	}

	@Override
	public int getLevel() {
		return this.getHandle().getLevel();
	}

	@Override
	public Player getPlayer() {
		return this;
	}

	@Override
	public String getPlayerListName() {
		return this.getHandle().getPlayerListName();
	}

	@Override
	public long getPlayerTime() {
		return this.getHandle().getPlayerTime();
	}

	@Override
	public long getPlayerTimeOffset() {
		return this.getHandle().getPlayerTimeOffset();
	}

	@Override
	public World.Weather getPlayerWeather() {
		return World.Weather.valueOf(this.getHandle().getPlayerWeather().name());
	}

	@Override
	public float getSaturation() {
		return this.getHandle().getSaturation();
	}

	@Override
	public int getTotalExperience() {
		return this.getHandle().getTotalExperience();
	}

	@Override
	public float getWalkSpeed() {
		return this.getHandle().getWalkSpeed();
	}

	@Override
	public void giveExperience(int experience) {
		this.getHandle().giveExp(experience);
	}

	@Override
	public void giveExperienceLevels(int levels) {
		this.getHandle().giveExpLevels(levels);
	}

	@Override
	public boolean hasPlayedBefore() {
		return this.getHandle().hasPlayedBefore();
	}

	@Override
	public void hidePlayer(Player player) {
		this.getHandle().hidePlayer(((CraftPlayer)player).getHandle());
	}

	@Override
	public boolean isBanned() {
		return this.getHandle().isBanned();
	}

	@Override
	public boolean isFlying() {
		return this.getHandle().isFlying();
	}

	@Override
	public boolean isOnline() {
		return this.getHandle().isOnline();
	}

	@Override
	public boolean isPlayerTimeRelative() {
		return this.getHandle().isPlayerTimeRelative();
	}

	@Override
	public boolean isSleepingIgnored() {
		return this.getHandle().isSleepingIgnored();
	}

	@Override
	public boolean isSneaking() {
		return this.getHandle().isSneaking();
	}

	@Override
	public boolean isSprinting() {
		return this.getHandle().isSprinting();
	}

	@Override
	public boolean isWhitelisted() {
		return this.getHandle().isWhitelisted();
	}

	@Override
	public void kick(String message) {
		this.getHandle().kickPlayer(message);
	}

	@Override
	public void loadData() {
		this.getHandle().loadData();
	}

	@Override
	public boolean performCommand(String command) {
		return Nifty.getServer().dispatchCommand(this, command);
	}

	@Override
	public void resetPlayerTime() {
		this.getHandle().resetPlayerTime();
	}

	@Override
	public void resetPlayerWeather() {
		this.getHandle().resetPlayerWeather();
	}

	@Override
	public void saveData() {
		this.getHandle().saveData();
	}

	@Override
	public void sendPluginMessage(MinecraftPlugin plugin, String channel, byte[] message) {
		Nifty.getMessenger().dispatch(channel, message);
	}

	@Override
	public void setAllowFlight(boolean value) {
		this.getHandle().setAllowFlight(value);
	}

	@Override
	public void setBanned(boolean value) {
		this.getHandle().setBanned(value);
	}

	@Override
	public void setBedSpawnLocation(Location location, boolean force) {
		this.getHandle().setBedSpawnLocation(((CraftLocation)location).getHandle(), force);
	}

	@Override
	public void setDisplayName(String displayName) {
		this.getHandle().setDisplayName(displayName);
	}

	@Override
	public void setExhaustion(float exhaustion) {
		this.getHandle().setExhaustion(exhaustion);
	}

	@Override
	public void setExperience(float experience) {
		this.getHandle().setExp(experience);
	}

	@Override
	public void setFlying(boolean value) {
		this.getHandle().setFlying(value);
	}

	@Override
	public void setFlySpeed(float speed) throws IllegalArgumentException {
		this.getHandle().setFlySpeed(speed);
	}

	@Override
	public void setFoodLevel(int level) {
		this.getHandle().setFoodLevel(level);
	}

	@Override
	public void setLevel(int level) {
		this.getHandle().setLevel(level);
	}

	@Override
	public void setSleepingIgnored(boolean value) {
		this.getHandle().setSleepingIgnored(value);
	}

	@Override
	public void setPlayerListName(String playerListName) {
		this.getHandle().setPlayerListName(playerListName);
	}

	@Override
	public void setPlayerTime(long time, boolean relative) {
		this.getHandle().setPlayerTime(time, relative);
	}

	@Override
	public void setPlayerWeather(World.Weather weather) {
		this.getHandle().setPlayerWeather(org.bukkit.WeatherType.valueOf(weather.name()));
	}

	@Override
	public void setSaturation(float saturation) {
		this.getHandle().setSaturation(saturation);
	}

	@Override
	public void setSneaking(boolean value) {
		this.getHandle().setSneaking(value);
	}

	@Override
	public void setSprinting(boolean value) {
		this.getHandle().setSprinting(value);
	}

	@Override
	public void setTotalExperience(int experience) {
		this.getHandle().setTotalExperience(experience);
	}

	@Override
	public void setWalkSpeed(float speed) throws IllegalArgumentException {
		this.getHandle().setWalkSpeed(speed);
	}

	@Override
	public void setWhitelisted(boolean value) {
		this.getHandle().setWhitelisted(value);
	}

	@Override
	public void showPlayer(Player player) {
		this.getHandle().showPlayer(((CraftPlayer)player).getHandle());
	}

	@Override
	public void updateInventory() {
		this.getHandle().updateInventory();
	}





	@Override
	public Set<String> getListeningPluginChannels() {
		return this.getHandle().getListeningPluginChannels(); // TODO
	}

}