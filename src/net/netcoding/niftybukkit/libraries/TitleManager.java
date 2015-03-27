package net.netcoding.niftybukkit.libraries;

import net.netcoding.niftybukkit.NiftyBukkit;
import net.netcoding.niftybukkit.mojang.MojangProfile;
import net.netcoding.niftybukkit.reflection.MinecraftPackage;
import net.netcoding.niftybukkit.reflection.Reflection;
import net.netcoding.niftybukkit.util.RegexUtil;
import net.netcoding.niftybukkit.util.StringUtil;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.craftbukkit.libs.com.google.gson.JsonObject;

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
		this(title, subtitle, 0, 0, 0);
	}

	public TitleManager(String title, String subtitle, int fadeInTime, int stayTime, int fadeOutTime) {
		this.setTitle(title);
		this.setSubtitle(subtitle);
		this.setTimeFadeIn(fadeInTime);
		this.setTimeStay(stayTime);
		this.setTimeFadeOut(fadeInTime);
	}

	public void broadcastClear() throws Exception {
		for (MojangProfile profile : NiftyBukkit.getMojangRepository().searchByPlayer(Bukkit.getOnlinePlayers()))
			this.sendClear(profile);
	}

	public void broadcastReset() throws Exception {
		for (MojangProfile profile : NiftyBukkit.getMojangRepository().searchByPlayer(Bukkit.getOnlinePlayers()))
			this.sendReset(profile);
	}

	public void broadcastTitle() throws Exception {
		for (MojangProfile profile : NiftyBukkit.getMojangRepository().searchByPlayer(Bukkit.getOnlinePlayers()))
			this.sendTitle(profile);
	}

	private Reflection getComatibleReflection(String className, String classEnum) {
		return new Reflection(StringUtil.format("{0}{1}", (MinecraftPackage.IS_PRE_1_8_3 ? "" : StringUtil.format("{0}$", className)), classEnum), MinecraftPackage.MINECRAFT_SERVER);
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

	public void sendClear(MojangProfile profile) throws Exception {
		if (!profile.getOfflinePlayer().isOnline()) return;

		Reflection entityPlayerObj = new Reflection("EntityPlayer", MinecraftPackage.MINECRAFT_SERVER);
		Reflection playerConnObj = new Reflection("PlayerConnection", MinecraftPackage.MINECRAFT_SERVER);
		Reflection packetTitleObj = new Reflection("PacketPlayOutTitle", MinecraftPackage.MINECRAFT_SERVER);
		Reflection titleActionObj = this.getComatibleReflection("PacketPlayOutTitle", "EnumTitleAction");
		Object[] titleActionEnums = titleActionObj.getClazz().getEnumConstants();
		Object playerConnection = entityPlayerObj.getValue("playerConnection", profile.getHandle());

		Object packetTitle = packetTitleObj.newInstance(titleActionEnums[3], null);
		playerConnObj.invokeMethod("sendPacket", playerConnection, packetTitle);
	}

	public void sendReset(MojangProfile profile) throws Exception {
		if (!profile.getOfflinePlayer().isOnline()) return;

		Reflection entityPlayerObj = new Reflection("EntityPlayer", MinecraftPackage.MINECRAFT_SERVER);
		Reflection playerConnObj = new Reflection("PlayerConnection", MinecraftPackage.MINECRAFT_SERVER);
		Reflection packetTitleObj = new Reflection("PacketPlayOutTitle", MinecraftPackage.MINECRAFT_SERVER);
		Reflection titleActionObj = this.getComatibleReflection("PacketPlayOutTitle", "EnumTitleAction");
		Object[] titleActionEnums = titleActionObj.getClazz().getEnumConstants();
		Object playerConnection = entityPlayerObj.getValue("playerConnection", profile.getHandle());

		Object packetTitle = packetTitleObj.newInstance(titleActionEnums[4], null);
		playerConnObj.invokeMethod("sendPacket", playerConnection, packetTitle);
	}

	public void sendTitle(MojangProfile profile) throws Exception {
		if (!profile.getOfflinePlayer().isOnline()) return;

		// v1_8_R1
		Reflection entityPlayerObj = new Reflection("EntityPlayer", MinecraftPackage.MINECRAFT_SERVER);
		Reflection playerConnObj = new Reflection("PlayerConnection", MinecraftPackage.MINECRAFT_SERVER);
		Reflection packetTitleObj = new Reflection("PacketPlayOutTitle", MinecraftPackage.MINECRAFT_SERVER);
		Reflection titleActionObj = this.getComatibleReflection("PacketPlayOutTitle", "EnumTitleAction");
		Reflection chatSerializeObj = this.getComatibleReflection("IChatBaseComponent", "ChatSerializer");
		Object[] titleActionEnums = titleActionObj.getClazz().getEnumConstants();
		Object playerConnection = entityPlayerObj.getValue("playerConnection", profile.getHandle());

		if (this.getTimeStay() > 0) {
			// Timings
			Object packetTimings = packetTitleObj.newInstance(titleActionEnums[2], null, this.getTimeFadeIn(), this.getTimeStay(), this.getTimeFadeOut());
			playerConnObj.invokeMethod("sendPacket", playerConnection, packetTimings);

			// Title
			JsonObject json = new JsonObject();
			json.addProperty("text", RegexUtil.replaceColor(this.getTitle(), RegexUtil.REPLACE_ALL_PATTERN));
			json.addProperty("color", RegexUtil.replaceColor(this.getTitleColor().name().toLowerCase(), RegexUtil.REPLACE_ALL_PATTERN));
			Object title = chatSerializeObj.invokeMethod("a", null, json.toString());
			Object packetTitle = packetTitleObj.newInstance(titleActionEnums[0], title);
			playerConnObj.invokeMethod("sendPacket", playerConnection, packetTitle);

			// Subtitle
			if (StringUtil.notEmpty(this.getSubtitle())) {
				json = new JsonObject();
				json.addProperty("text", RegexUtil.replaceColor(this.getSubtitle(), RegexUtil.REPLACE_ALL_PATTERN));
				json.addProperty("color", RegexUtil.replaceColor(this.getSubtitleColor().name().toLowerCase(), RegexUtil.REPLACE_ALL_PATTERN));
				Object subtitle = chatSerializeObj.invokeMethod("a", null, json.toString());
				Object packetSubtitle = packetTitleObj.newInstance(titleActionEnums[1], subtitle);
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