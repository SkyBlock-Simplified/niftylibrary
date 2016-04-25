package net.netcoding.niftybukkit.minecraft.libraries;

import com.google.gson.JsonObject;
import net.netcoding.niftybukkit.NiftyBukkit;
import net.netcoding.niftybukkit.mojang.BukkitMojangProfile;
import net.netcoding.niftybukkit.reflection.BukkitReflection;
import net.netcoding.niftybukkit.reflection.MinecraftPackage;
import net.netcoding.niftycore.reflection.Reflection;
import net.netcoding.niftycore.util.RegexUtil;
import net.netcoding.niftycore.util.StringUtil;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;

public class TitleManager {

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
		for (BukkitMojangProfile profile : NiftyBukkit.getMojangRepository().searchByPlayer(Bukkit.getOnlinePlayers()))
			sendActionBar(profile, text);
	}

	public void clear() throws Exception {
		for (BukkitMojangProfile profile : NiftyBukkit.getMojangRepository().searchByPlayer(Bukkit.getOnlinePlayers()))
			this.sendClear(profile);
	}

	public void reset() throws Exception {
		for (BukkitMojangProfile profile : NiftyBukkit.getMojangRepository().searchByPlayer(Bukkit.getOnlinePlayers()))
			this.sendReset(profile);
	}

	public static void broadcastTabList(String header, String footer) throws Exception {
		for (BukkitMojangProfile profile : NiftyBukkit.getMojangRepository().searchByPlayer(Bukkit.getOnlinePlayers()))
			sendTabList(profile, header, footer);
	}

	public void broadcast() throws Exception {
		for (BukkitMojangProfile profile : NiftyBukkit.getMojangRepository().searchByPlayer(Bukkit.getOnlinePlayers()))
			this.sendTitle(profile);
	}

	public String getSubtitle() {
		return this.subtitle;
	}

	public ChatColor getSubtitleColor() {
		return this.subtitleColor;
	}

	public int getFadeIn() {
		return this.fadeIn;
	}

	public int getFadeOut() {
		return this.fadeOut;
	}

	public int getStay() {
		return this.stay;
	}

	public String getTitle() {
		return this.title;
	}

	public ChatColor getTitleColor() {
		return this.titleColor;
	}

	public static void sendActionBar(BukkitMojangProfile profile, String text) throws Exception {
		if (!profile.getOfflinePlayer().isOnline()) return;

		Reflection packetChat = new Reflection("PacketPlayOutChat", MinecraftPackage.MINECRAFT_SERVER);
		Reflection chatSerializer = BukkitReflection.getCompatibleReflection("IChatBaseComponent", "ChatSerializer");

		// Text
		JsonObject json = new JsonObject();
		json.addProperty("text", RegexUtil.replaceColor(text, RegexUtil.REPLACE_ALL_PATTERN));
		Object actionJson = chatSerializer.invokeMethod("a", null, json.toString());
		Object packetChatObj = packetChat.newInstance(actionJson, (byte)2);
		profile.sendPacket(packetChatObj);
	}

	public void sendClear(BukkitMojangProfile profile) throws Exception {
		if (!profile.getOfflinePlayer().isOnline()) return;

		Reflection packetTitle = new Reflection("PacketPlayOutTitle", MinecraftPackage.MINECRAFT_SERVER);
		Reflection titleAction = BukkitReflection.getCompatibleReflection("PacketPlayOutTitle", "EnumTitleAction");
		Object packetTitleObj = packetTitle.newInstance();
		Object enumClear = titleAction.getValue("CLEAR", null);

		if (MinecraftPackage.IS_PRE_1_8)
			packetTitle.setValue("a", packetTitleObj, enumClear);
		else
			packetTitleObj = packetTitle.newInstance(enumClear, null);

		profile.sendPacket(packetTitleObj);
	}

	public void sendReset(BukkitMojangProfile profile) throws Exception {
		if (!profile.getOfflinePlayer().isOnline()) return;

		Reflection packetTitle = new Reflection("PacketPlayOutTitle", MinecraftPackage.MINECRAFT_SERVER);
		Reflection titleAction = BukkitReflection.getCompatibleReflection("PacketPlayOutTitle", "EnumTitleAction");
		Object packetTitleObj = packetTitle.newInstance();
		Object enumReset = titleAction.getValue("RESET", null);

		if (MinecraftPackage.IS_PRE_1_8)
			packetTitle.setValue("a", packetTitleObj, enumReset);
		else
			packetTitleObj = packetTitle.newInstance(enumReset, null);

		profile.sendPacket(packetTitleObj);
	}

	public static void sendTabList(BukkitMojangProfile profile, String header, String footer) throws Exception {
		if (!profile.getOfflinePlayer().isOnline()) return;
		Reflection packetList = new Reflection("PacketPlayOutPlayerListHeaderFooter", MinecraftPackage.MINECRAFT_SERVER);
		Reflection chatSerializer = BukkitReflection.getCompatibleReflection("IChatBaseComponent", "ChatSerializer");
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

	public void sendTitle(BukkitMojangProfile profile) throws Exception {
		if (!profile.getOfflinePlayer().isOnline()) return;

		Reflection packetTitle = new Reflection("PacketPlayOutTitle", MinecraftPackage.MINECRAFT_SERVER);
		Reflection titleAction = BukkitReflection.getCompatibleReflection("PacketPlayOutTitle", "EnumTitleAction");
		Reflection chatSerializer = BukkitReflection.getCompatibleReflection("IChatBaseComponent", "ChatSerializer");

		if (this.getStay() > 0 && StringUtil.notEmpty(this.getTitle())) {
			Object enumTimes = titleAction.getValue("TIMES", null);
			Object enumTitle = titleAction.getValue("TITLE", null);
			Object enumSubtitle = titleAction.getValue("SUBTITLE", null);

			// Timings
			Object packetTimingsObj = packetTitle.newInstance();

			if (MinecraftPackage.IS_PRE_1_8) {
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

			if (MinecraftPackage.IS_PRE_1_8) {
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

				if (MinecraftPackage.IS_PRE_1_8) {
					packetTitle.setValue("a", packetSubtitleObj, enumSubtitle);
					packetTitle.setValue("b", packetSubtitleObj, subtitle);
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

	public void setFadeIn(int value) {
		this.fadeIn = value < 0 ? 0 : value;
	}

	public void setFadeOut(int value) {
		this.fadeOut = value < 0 ? 0 : value;
	}

	public void setStay(int value) {
		this.stay = value < 0 ? 0 : value;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public void setTitleColor(ChatColor color) {
		this.titleColor = color;
	}

}