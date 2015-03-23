package net.netcoding.niftybukkit.libraries;

import net.netcoding.niftybukkit.NiftyBukkit;
import net.netcoding.niftybukkit.mojang.MojangProfile;
import net.netcoding.niftybukkit.reflection.MinecraftPackage;
import net.netcoding.niftybukkit.reflection.Reflection;
import net.netcoding.niftybukkit.util.RegexUtil;
import net.netcoding.niftybukkit.util.StringUtil;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import com.google.gson.JsonObject;

public class TitleManager {

	private String subtitle = "";
	private ChatColor subtitleColor = ChatColor.WHITE;
	private int timeFadeIn = 0;
	private int timeFadeOut = 0;
	private int timeStay = 0;
	private String title = "";
	private ChatColor titleColor = ChatColor.WHITE;

	public TitleManager(String title) {
		this(title, "");
	}

	public TitleManager(String title, String subtitle) {
		this(title, subtitle, -1, -1, -1);
	}

	public TitleManager(String title, String subtitle, int fadeInTime, int stayTime, int fadeOutTime) {
		this.title = title;
		this.subtitle = subtitle;
		this.timeFadeIn = fadeInTime;
		this.timeStay = stayTime;
		this.timeFadeOut = fadeOutTime;
	}

	public void broadcast() throws Exception {
		for (MojangProfile profile : NiftyBukkit.getMojangRepository().searchByPlayer(Bukkit.getOnlinePlayers()))
			this.send(profile);
	}

	public void clearTitle(MojangProfile profile) throws Exception {
		if (!profile.getOfflinePlayer().isOnline()) return;
		Player player = profile.getOfflinePlayer().getPlayer();

		Reflection playerObj = new Reflection("Player", Player.class.toString());
		Reflection playerConnObj = new Reflection("PlayerConnection", MinecraftPackage.MINECRAFT_SERVER);
		Reflection packetTitleObj = new Reflection("PacketPlayOutTitle", MinecraftPackage.MINECRAFT_SERVER);
		Reflection titleActionObj = new Reflection("PacketPlayOutTitle$EnumTitleAction", MinecraftPackage.MINECRAFT_SERVER);
		Reflection chatBaseObj = new Reflection("IChatBaseComponent", MinecraftPackage.MINECRAFT_SERVER);
		Object[] titleActionEnums = titleActionObj.getClazz().getEnumConstants();
		Object playerHandle = playerObj.invokeMethod("getHandle", player, playerObj.getClazz());
		Object playerConnection = playerConnObj.getValue("playerConnection", playerHandle);

		Object packetTitle = packetTitleObj.getConstructor(titleActionObj.getClazz(), chatBaseObj.getClazz()).newInstance(titleActionEnums[3], null);
		playerConnObj.invokeMethod("sendPacket", playerConnection, packetTitle);
	}

	public String getSubtitle() {
		return this.subtitle;
	}

	public ChatColor getSubtitleColor() {
		return this.subtitleColor;
	}

	public int getTimeFadeIn() {
		return this.timeFadeIn;
	}

	public int getTimeFadeOut() {
		return this.timeFadeOut;
	}

	public int getTimeStay() {
		return this.timeStay;
	}

	public String getTitle() {
		return this.title;
	}

	public ChatColor getTitleColor() {
		return this.titleColor;
	}

	public void resetTitle(MojangProfile profile) throws Exception {
		if (!profile.getOfflinePlayer().isOnline()) return;
		Player player = profile.getOfflinePlayer().getPlayer();

		Reflection playerObj = new Reflection("Player", Player.class.toString());
		Reflection playerConnObj = new Reflection("PlayerConnection", MinecraftPackage.MINECRAFT_SERVER);
		Reflection packetTitleObj = new Reflection("PacketPlayOutTitle", MinecraftPackage.MINECRAFT_SERVER);
		Reflection titleActionObj = new Reflection("PacketPlayOutTitle$EnumTitleAction", MinecraftPackage.MINECRAFT_SERVER);
		Reflection chatBaseObj = new Reflection("IChatBaseComponent", MinecraftPackage.MINECRAFT_SERVER);
		Object[] titleActionEnums = titleActionObj.getClazz().getEnumConstants();
		Object playerHandle = playerObj.invokeMethod("getHandle", player, playerObj.getClazz());
		Object playerConnection = playerConnObj.getValue("playerConnection", playerHandle);

		Object packetTitle = packetTitleObj.getConstructor(titleActionObj.getClazz(), chatBaseObj.getClazz()).newInstance(titleActionEnums[4], null);
		playerConnObj.invokeMethod("sendPacket", playerConnection, packetTitle);
	}

	public void send(MojangProfile profile) throws Exception {
		if (!profile.getOfflinePlayer().isOnline()) return;
		Player player = profile.getOfflinePlayer().getPlayer();

		Reflection playerObj = new Reflection("Player", Player.class.toString());
		Reflection playerConnObj = new Reflection("PlayerConnection", MinecraftPackage.MINECRAFT_SERVER);
		Reflection packetTitleObj = new Reflection("PacketPlayOutTitle", MinecraftPackage.MINECRAFT_SERVER);
		Reflection titleActionObj = new Reflection("PacketPlayOutTitle$EnumTitleAction", MinecraftPackage.MINECRAFT_SERVER);
		Reflection chatBaseObj = new Reflection("IChatBaseComponent", MinecraftPackage.MINECRAFT_SERVER);
		Reflection chatSerializeObj = new Reflection("IChatBaseComponent$ChatSerializer", MinecraftPackage.MINECRAFT_SERVER);
		Object[] titleActionEnums = titleActionObj.getClazz().getEnumConstants();
		Object playerHandle = playerObj.invokeMethod("getHandle", player, playerObj.getClazz());
		Object playerConnection = playerConnObj.getValue("playerConnection", playerHandle);

		if (this.getTimeStay() > 0) {
			// Timings
			Object packetTimings = packetTitleObj.getConstructor(titleActionObj.getClazz(), chatBaseObj.getClazz(), Integer.class, Integer.class, Integer.class)
					.newInstance(titleActionEnums[2], null, this.getTimeFadeIn(), this.getTimeStay(), this.getTimeFadeOut());
			playerConnObj.invokeMethod("sendPacket", playerConnection, packetTimings);

			// Title
			JsonObject json = new JsonObject();
			json.addProperty("text", RegexUtil.replaceColor(this.getTitle(), RegexUtil.REPLACE_ALL_PATTERN));
			json.addProperty("color", RegexUtil.replaceColor(this.getTitleColor().name().toLowerCase(), RegexUtil.REPLACE_ALL_PATTERN));
			Object title = chatSerializeObj.invokeMethod("a", null, json.toString());
			Object packetTitle = packetTitleObj.getConstructor(titleActionObj.getClazz(), chatBaseObj.getClazz()).newInstance(titleActionEnums[0], title);
			playerConnObj.invokeMethod("sendPacket", playerConnection, packetTitle);

			// Subtitle
			if (StringUtil.notEmpty(this.getSubtitle())) {
				json = new JsonObject();
				json.addProperty("text", RegexUtil.replaceColor(this.getSubtitle(), RegexUtil.REPLACE_ALL_PATTERN));
				json.addProperty("color", RegexUtil.replaceColor(this.getSubtitleColor().name().toLowerCase(), RegexUtil.REPLACE_ALL_PATTERN));
				Object subtitle = chatSerializeObj.invokeMethod("a", null, json.toString());
				Object packetSubtitle = packetTitleObj.getConstructor(titleActionObj.getClazz(), chatBaseObj.getClazz()).newInstance(titleActionEnums[1], subtitle);
				playerConnObj.invokeMethod("sendPacket", playerConnection, packetSubtitle);
			}
		}
	}

	public void setSubtitle(String subtitle) {
		this.subtitle = subtitle;
	}

	public void setSubtitleColor(ChatColor color) {
		this.subtitleColor = color;
	}

	public void setTimeFadeIn(int value) {
		this.timeFadeIn = value < 0 ? 0 : value;
	}

	public void setTimeFadeOut(int value) {
		this.timeFadeOut = value < 0 ? 0 : value;
	}

	public void setTimeStay(int value) {
		this.timeStay = value < 0 ? 0 : value;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public void setTitleColor(ChatColor color) {
		this.titleColor = color;
	}

}