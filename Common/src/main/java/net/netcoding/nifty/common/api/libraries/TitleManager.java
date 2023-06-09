package net.netcoding.nifty.common.api.libraries;

import com.google.gson.JsonObject;
import net.netcoding.nifty.common.Nifty;
import net.netcoding.nifty.common.mojang.MinecraftMojangProfile;
import net.netcoding.nifty.common.reflection.MinecraftReflection;
import net.netcoding.nifty.common.reflection.MinecraftPackage;
import net.netcoding.nifty.common.reflection.MinecraftProtocol;
import net.netcoding.nifty.core.api.color.ChatColor;
import net.netcoding.nifty.core.reflection.Reflection;
import net.netcoding.nifty.core.util.RegexUtil;
import net.netcoding.nifty.core.util.StringUtil;

public final class TitleManager {

	private String subtitle = "";
	private ChatColor subtitleColor = ChatColor.WHITE;
	private int fadeIn = 0;
	private int fadeOut = 0;
	private int stay = 0;
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
		this.setFadeIn(fadeInTime);
		this.setStay(stayTime);
		this.setFadeOut(fadeOutTime);
	}

	public static void broadcastActionBar(String text) throws Exception {
		for (MinecraftMojangProfile profile : Nifty.getMojangRepository().searchByPlayer(Nifty.getServer().getPlayerList()))
			sendActionBar(profile, text);
	}

	public void clear() throws Exception {
		for (MinecraftMojangProfile profile : Nifty.getMojangRepository().searchByPlayer(Nifty.getServer().getPlayerList()))
			this.sendClear(profile);
	}

	public void reset() throws Exception {
		for (MinecraftMojangProfile profile : Nifty.getMojangRepository().searchByPlayer(Nifty.getServer().getPlayerList()))
			this.sendReset(profile);
	}

	public static void broadcastTabList(String header, String footer) throws Exception {
		for (MinecraftMojangProfile profile : Nifty.getMojangRepository().searchByPlayer(Nifty.getServer().getPlayerList()))
			sendTabList(profile, header, footer);
	}

	public void broadcast() throws Exception {
		for (MinecraftMojangProfile profile : Nifty.getMojangRepository().searchByPlayer(Nifty.getServer().getPlayerList()))
			this.sendTitle(profile);
	}

	public final String getSubtitle() {
		return this.subtitle;
	}

	public final ChatColor getSubtitleColor() {
		return this.subtitleColor;
	}

	public final int getFadeIn() {
		return this.fadeIn;
	}

	public final int getFadeOut() {
		return this.fadeOut;
	}

	public final int getStay() {
		return this.stay;
	}

	public final String getTitle() {
		return this.title;
	}

	public final ChatColor getTitleColor() {
		return this.titleColor;
	}

	public static void sendActionBar(MinecraftMojangProfile profile, String text) {
		if (!profile.getOfflinePlayer().isOnline()) return;
		Reflection packetChat = new Reflection("PacketPlayOutChat", MinecraftPackage.MINECRAFT_SERVER);
		Reflection chatSerializer = MinecraftReflection.getCompatibleReflection("IChatBaseComponent", "ChatSerializer");

		// Text
		JsonObject json = new JsonObject();
		json.addProperty("text", RegexUtil.replaceColor(text, RegexUtil.REPLACE_ALL_PATTERN));
		Object actionJson = chatSerializer.invokeMethod("a", null, json.toString());
		Object packetChatObj = packetChat.newInstance(actionJson, (byte)2);
		profile.sendPacket(packetChatObj);
	}

	public final void sendClear(MinecraftMojangProfile profile) {
		if (!profile.getOfflinePlayer().isOnline()) return;
		Reflection packetTitle = new Reflection("PacketPlayOutTitle", MinecraftPackage.MINECRAFT_SERVER);
		Reflection titleAction = MinecraftReflection.getCompatibleReflection("PacketPlayOutTitle", "EnumTitleAction");
		Object packetTitleObj = packetTitle.newInstance();
		Object enumClear = titleAction.getValue("CLEAR", null);

		if (MinecraftProtocol.isPre1_8())
			packetTitle.setValue(titleAction.getClazz(), packetTitleObj, enumClear);
		else
			packetTitleObj = packetTitle.newInstance(enumClear, null);

		profile.sendPacket(packetTitleObj);
	}

	public final void sendReset(MinecraftMojangProfile profile) {
		if (!profile.getOfflinePlayer().isOnline()) return;
		Reflection packetTitle = new Reflection("PacketPlayOutTitle", MinecraftPackage.MINECRAFT_SERVER);
		Reflection titleAction = MinecraftReflection.getCompatibleReflection("PacketPlayOutTitle", "EnumTitleAction");
		Object packetTitleObj = packetTitle.newInstance();
		Object enumReset = titleAction.getValue("RESET", null);

		if (MinecraftProtocol.isPre1_8())
			packetTitle.setValue(titleAction.getClazz(), packetTitleObj, enumReset);
		else
			packetTitleObj = packetTitle.newInstance(enumReset, null);

		profile.sendPacket(packetTitleObj);
	}

	public static void sendTabList(MinecraftMojangProfile profile, String header, String footer) {
		if (!profile.getOfflinePlayer().isOnline()) return;
		Reflection packetList = new Reflection("PacketPlayOutPlayerListHeaderFooter", MinecraftPackage.MINECRAFT_SERVER);
		Reflection chatSerializer = MinecraftReflection.getCompatibleReflection("IChatBaseComponent", "ChatSerializer");
		Object packetListObj = packetList.newInstance();

		// Header
		JsonObject json = new JsonObject();
		json.addProperty("text", RegexUtil.replaceColor(header, RegexUtil.REPLACE_ALL_PATTERN));
		Object headerJson = chatSerializer.invokeMethod("a", null, json.toString());
		packetList.setValue("a", packetListObj, headerJson);

		// Footer
		json = new JsonObject();
		json.addProperty("text", RegexUtil.replaceColor(footer, RegexUtil.REPLACE_ALL_PATTERN));
		Object footerJson = chatSerializer.invokeMethod("a", null, json.toString());
		packetList.setValue("b", packetListObj, footerJson);
		profile.sendPacket(packetListObj);
	}

	public final void sendTitle(MinecraftMojangProfile profile) {
		if (!profile.getOfflinePlayer().isOnline()) return;
		Reflection packetTitle = new Reflection("PacketPlayOutTitle", MinecraftPackage.MINECRAFT_SERVER);
		Reflection titleAction = MinecraftReflection.getCompatibleReflection("PacketPlayOutTitle", "EnumTitleAction");
		Reflection chatSerializer = MinecraftReflection.getCompatibleReflection("IChatBaseComponent", "ChatSerializer");

		if (this.getStay() > 0 && StringUtil.notEmpty(this.getTitle())) {
			Object enumTimes = titleAction.getValue("TIMES", null);
			Object enumTitle = titleAction.getValue("TITLE", null);
			Object enumSubtitle = titleAction.getValue("SUBTITLE", null);

			// Timings
			Object packetTimingsObj = packetTitle.newInstance();

			if (MinecraftProtocol.isPre1_8()) {
				packetTitle.setValue("a", packetTimingsObj, enumTimes);
				packetTitle.setValue("c", packetTimingsObj, this.getFadeIn());
				packetTitle.setValue("d", packetTimingsObj, this.getStay());
				packetTitle.setValue("e", packetTimingsObj, this.getFadeOut());
			} else
				packetTimingsObj = packetTitle.newInstance(enumTimes, null, this.getFadeIn(), this.getStay(), this.getFadeOut());

			profile.sendPacket(packetTimingsObj);

			// Title
			JsonObject json = new JsonObject();
			json.addProperty("text", RegexUtil.replaceColor(this.getTitle(), RegexUtil.REPLACE_ALL_PATTERN));
			json.addProperty("color", this.getTitleColor().name().toLowerCase());
			Object title = chatSerializer.invokeMethod("a", null, json.toString());
			Object packetTitleObj = packetTitle.newInstance();

			if (MinecraftProtocol.isPre1_8()) {
				packetTitle.setValue("a", packetTitleObj, enumTitle);
				packetTitle.setValue("b", packetTitleObj, title);
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

				if (MinecraftProtocol.isPre1_8()) {
					packetTitle.setValue("a", packetSubtitleObj, enumSubtitle);
					packetTitle.setValue("b", packetSubtitleObj, subtitle);
				} else
					packetSubtitleObj = packetTitle.newInstance(enumSubtitle, subtitle);

				profile.sendPacket(packetSubtitleObj);
			}
		}
	}

	public final void setSubtitle(String subtitle) {
		this.subtitle = subtitle;
	}

	public final void setSubtitleColor(ChatColor color) {
		this.subtitleColor = color;
	}

	public final void setFadeIn(int value) {
		this.fadeIn = value < 0 ? 0 : value;
	}

	public final void setFadeOut(int value) {
		this.fadeOut = value < 0 ? 0 : value;
	}

	public final void setStay(int value) {
		this.stay = value < 0 ? 0 : value;
	}

	public final void setTitle(String title) {
		this.title = title;
	}

	public final void setTitleColor(ChatColor color) {
		this.titleColor = color;
	}

}