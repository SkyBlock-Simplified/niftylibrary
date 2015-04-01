package net.netcoding.niftybukkit.libraries;

import net.netcoding.niftybukkit.NiftyBukkit;
import net.netcoding.niftybukkit.mojang.MojangProfile;
import net.netcoding.niftybukkit.reflection.FieldEntry;
import net.netcoding.niftybukkit.reflection.MinecraftPackage;
import net.netcoding.niftybukkit.reflection.Reflection;
import net.netcoding.niftybukkit.util.RegexUtil;
import net.netcoding.niftybukkit.util.StringUtil;
import net.netcoding.niftybukkit.util.gson.JsonObject;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;

public class TitleManager {

	private String subtitle = "";
	private ChatColor subtitleColor = ChatColor.WHITE;
	private int timeFadeIn = 0;
	private int timeFadeOut = 0;
	private int timeStay = 0;
	private String title = "";
	private ChatColor titleColor = ChatColor.WHITE;

	public TitleManager() {
		this("");
	}

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

	public static void broadcastActionBar(String text) throws Exception {
		for (MojangProfile profile : NiftyBukkit.getMojangRepository().searchByPlayer(Bukkit.getOnlinePlayers()))
			sendActionBar(profile, text);
	}

	public void broadcastClear() throws Exception {
		for (MojangProfile profile : NiftyBukkit.getMojangRepository().searchByPlayer(Bukkit.getOnlinePlayers()))
			this.sendClear(profile);
	}

	public void broadcastReset() throws Exception {
		for (MojangProfile profile : NiftyBukkit.getMojangRepository().searchByPlayer(Bukkit.getOnlinePlayers()))
			this.sendReset(profile);
	}

	public static void broadcastTabList(String header, String footer) throws Exception {
		for (MojangProfile profile : NiftyBukkit.getMojangRepository().searchByPlayer(Bukkit.getOnlinePlayers()))
			sendTabList(profile, header, footer);
	}

	public void broadcastTitle() throws Exception {
		for (MojangProfile profile : NiftyBukkit.getMojangRepository().searchByPlayer(Bukkit.getOnlinePlayers()))
			this.sendTitle(profile);
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

	public static void sendActionBar(MojangProfile profile, String text) throws Exception {
		if (!profile.getOfflinePlayer().isOnline()) return;

		Reflection packetChat = new Reflection("PacketPlayOutChat", MinecraftPackage.MINECRAFT_SERVER);
		Reflection chatSerializer = Reflection.getComatibleReflection("IChatBaseComponent", "ChatSerializer");

		// Text
		JsonObject json = new JsonObject();
		Object actionJson = chatSerializer.invokeMethod("a", null, json.toString());
		Object packetChatObj = packetChat.newInstance(actionJson, (byte)2);
		profile.sendPacket(packetChatObj);
	}

	public void sendClear(MojangProfile profile) throws Exception {
		if (!profile.getOfflinePlayer().isOnline()) return;

		Reflection packetTitle = new Reflection("PacketPlayOutTitle", MinecraftPackage.MINECRAFT_SERVER);
		Reflection titleAction = Reflection.getComatibleReflection("PacketPlayOutTitle", "EnumTitleAction");
		Object packetTitleObj = packetTitle.newInstance();
		Object enumClear = titleAction.getValue("CLEAR", null);

		if (MinecraftPackage.IS_PRE_1_8) 
			packetTitle.setValue(packetTitleObj, new FieldEntry("a", enumClear));
		else
			packetTitleObj = packetTitle.newInstance(enumClear, null);

		profile.sendPacket(packetTitleObj);
	}

	public void sendReset(MojangProfile profile) throws Exception {
		if (!profile.getOfflinePlayer().isOnline()) return;

		Reflection packetTitle = new Reflection("PacketPlayOutTitle", MinecraftPackage.MINECRAFT_SERVER);
		Reflection titleAction = Reflection.getComatibleReflection("PacketPlayOutTitle", "EnumTitleAction");
		Object packetTitleObj = packetTitle.newInstance();
		Object enumReset = titleAction.getValue("RESET", null);

		if (MinecraftPackage.IS_PRE_1_8) 
			packetTitle.setValue(packetTitleObj, new FieldEntry("a", enumReset));
		else
			packetTitleObj = packetTitle.newInstance(enumReset, null);

		profile.sendPacket(packetTitleObj);
	}

	public static void sendTabList(MojangProfile profile, String header, String footer) throws Exception {
		if (!profile.getOfflinePlayer().isOnline()) return;
		Reflection packetList = new Reflection("PacketPlayOutPlayerListHeaderFooter", MinecraftPackage.MINECRAFT_SERVER);
		Reflection chatSerializer = Reflection.getComatibleReflection("IChatBaseComponent", "ChatSerializer");
		Object packetListObj = packetList.newInstance();

		// Header
		JsonObject json = new JsonObject();
		json.addProperty("text", RegexUtil.replaceColor(header, RegexUtil.REPLACE_ALL_PATTERN));
		Object headerJson = chatSerializer.invokeMethod("a", null, json.toString());
		packetList.setValue(packetListObj, new FieldEntry("a", headerJson));

		// Footer
		json = new JsonObject();
		json.addProperty("text", RegexUtil.replaceColor(footer, RegexUtil.REPLACE_ALL_PATTERN));
		Object footerJson = chatSerializer.invokeMethod("a", null, json.toString());
		packetList.setValue(packetListObj, new FieldEntry("b", footerJson));
		profile.sendPacket(packetListObj);
	}

	public void sendTitle(MojangProfile profile) throws Exception {
		if (!profile.getOfflinePlayer().isOnline()) return;

		Reflection packetTitle = new Reflection("PacketPlayOutTitle", MinecraftPackage.MINECRAFT_SERVER);
		Reflection titleAction = Reflection.getComatibleReflection("PacketPlayOutTitle", "EnumTitleAction");
		Reflection chatSerializer = Reflection.getComatibleReflection("IChatBaseComponent", "ChatSerializer");

		if (this.getTimeStay() > 0 && StringUtil.notEmpty(this.getTitle())) {
			Object enumTimes = titleAction.getValue("TIMES", null);
			Object enumTitle = titleAction.getValue("TITLE", null);
			Object enumSubtitle = titleAction.getValue("SUBTITLE", null);

			// Timings
			Object packetTimingsObj = packetTitle.newInstance();

			if (MinecraftPackage.IS_PRE_1_8) {
				packetTitle.setValue(packetTimingsObj, new FieldEntry("a", enumTimes));
				packetTitle.setValue(packetTimingsObj, new FieldEntry("c", this.getTimeFadeIn()));
				packetTitle.setValue(packetTimingsObj, new FieldEntry("d", this.getTimeStay()));
				packetTitle.setValue(packetTimingsObj, new FieldEntry("e", this.getTimeFadeOut()));
			} else
				packetTimingsObj = packetTitle.newInstance(enumTimes, null, this.getTimeFadeIn(), this.getTimeStay(), this.getTimeFadeOut());

			profile.sendPacket(packetTimingsObj);

			// Title
			JsonObject json = new JsonObject();
			json.addProperty("text", RegexUtil.replaceColor(this.getTitle(), RegexUtil.REPLACE_ALL_PATTERN));
			json.addProperty("color", this.getTitleColor().name().toLowerCase());
			Object title = chatSerializer.invokeMethod("a", null, json.toString());
			Object packetTitleObj = packetTitle.newInstance();

			if (MinecraftPackage.IS_PRE_1_8) {
				packetTitle.setValue(packetTitleObj, new FieldEntry("a", enumTitle));
				packetTitle.setValue(packetTitleObj, new FieldEntry("b", title));
			} else
				packetTitleObj = packetTitle.newInstance(enumTitle, title);

			profile.sendPacket(packetTitleObj);

			// Subtitle
			if (StringUtil.notEmpty(this.getSubtitle())) {
				json = new JsonObject();
				json.addProperty("text", RegexUtil.replaceColor(this.getSubtitle(), RegexUtil.REPLACE_ALL_PATTERN));
				json.addProperty("color", this.getSubtitleColor().name().toLowerCase());
				Object subtitle = chatSerializer.invokeMethod("a", null, json.toString());
				Object packetSubtitleObj = packetTitle.newInstance();

				if (MinecraftPackage.IS_PRE_1_8) {
					packetTitle.setValue(packetSubtitleObj, new FieldEntry("a", enumSubtitle));
					packetTitle.setValue(packetSubtitleObj, new FieldEntry("b", subtitle));
				} else
					packetSubtitleObj = packetTitle.newInstance(enumSubtitle, subtitle);

				profile.sendPacket(packetSubtitleObj);
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