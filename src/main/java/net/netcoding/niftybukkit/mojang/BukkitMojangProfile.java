package net.netcoding.niftybukkit.mojang;

import com.google.gson.JsonObject;
import net.netcoding.niftybukkit.NiftyBukkit;
import net.netcoding.niftybukkit.minecraft.inventory.FakeInventory;
import net.netcoding.niftybukkit.minecraft.items.ItemData;
import net.netcoding.niftybukkit.minecraft.messages.BungeeServer;
import net.netcoding.niftybukkit.reflection.BukkitReflection;
import net.netcoding.niftybukkit.reflection.MinecraftPackage;
import net.netcoding.niftybukkit.reflection.MinecraftProtocol;
import net.netcoding.niftybukkit.util.LocationUtil;
import net.netcoding.niftycore.minecraft.ChatColor;
import net.netcoding.niftycore.mojang.MojangProfile;
import net.netcoding.niftycore.reflection.Reflection;
import net.netcoding.niftycore.util.StringUtil;
import net.netcoding.niftycore.util.json.JsonMessage;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.SkullType;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;

public class BukkitMojangProfile extends MojangProfile {

	protected BukkitMojangProfile() { }

	/**
	 * Checks if the player is associated to this profile.
	 *
	 * @param oplayer Offline player to check.
	 * @return True if associated, otherwise false.
	 */
	public boolean belongsTo(OfflinePlayer oplayer) {
		return oplayer != null && oplayer.getUniqueId().equals(this.getUniqueId());
	}

	/**
	 * Gets the connection of this profiles client, if they are online.
	 *
	 * @return Connection of the client.
	 */
	public final Object getConnection() {
		return this.isOnlineLocally() ? new Reflection("EntityPlayer", MinecraftPackage.MINECRAFT_SERVER).getValue("playerConnection", this.getHandle()) : null;
	}

	/**
	 * Gets the direction a player is looking
	 *
	 * @return The direction the player is looking
	 */
	public BlockFace getFacing() {
		if (this.isOnlineLocally()) {
			float yaw = this.getOfflinePlayer().getPlayer().getLocation().getYaw();
			return LocationUtil.yawToFace(yaw);
		}

		return null;
	}

	/**
	 * Gets the handle of this profiles client, if they are online.
	 *
	 * @return Handle of the client.
	 */
	public final Object getHandle() {
		if (!this.isOnlineLocally()) return null;
		Reflection craftPlayer = new Reflection("CraftPlayer", "entity", MinecraftPackage.CRAFTBUKKIT);
		Object craftPlayerObj = craftPlayer.getClazz().cast(this.getOfflinePlayer().getPlayer());
		return craftPlayer.invokeMethod("getHandle", craftPlayerObj);
	}

	/**
	 * Gets clients locale.
	 *
	 * @return Clients locale.
	 */
	public final String getLocale() {
		String locale = "en_EN";

		if (this.isOnlineLocally()) {
			try {
				locale = (String)new Reflection("EntityPlayer", MinecraftPackage.MINECRAFT_SERVER).getValue("locale", this.getHandle());
			} catch (Exception ignore) { }
		}

		return locale;
	}

	/**
	 * Gets the players name associated to this UUID.
	 *
	 * @return Current player name.
	 */
	public String getName() {
		Player player = NiftyBukkit.getPlugin().getServer().getPlayer(this.getUniqueId());

		if (player == null || player.getName().equals(this.name))
			return this.name;

		if (NiftyBukkit.getBungeeHelper().isDetected()) {
			JsonObject json = new JsonObject();
			json.addProperty("id", this.getUniqueId().toString());
			json.addProperty("name", this.getName());
			json.addProperty("ip", this.ip);
			json.addProperty("port", this.port);
			String serverName = NiftyBukkit.getBungeeHelper().getServerName();
			NiftyBukkit.getBungeeHelper().forward(serverName, "PlayerUpdate", json.toString(), serverName);
			NiftyBukkit.getBungeeHelper().forward("ONLINE", "PlayerUpdate", json.toString(), serverName);
		}

		return this.name = player.getName();
	}

	/**
	 * Gets the players offline player object associated to this profile.
	 *
	 * @return Offline Offline player object.
	 */
	public OfflinePlayer getOfflinePlayer() {
		return NiftyBukkit.getPlugin().getServer().getOfflinePlayer(this.getUniqueId());
	}

	/**
	 * Get clients ping.
	 *
	 * @return Client latency with the server.
	 */
	public final int getPing() {
		int ping = 0;

		if (this.isOnlineLocally()) {
			try {
				ping = (int)new Reflection("EntityPlayer", MinecraftPackage.MINECRAFT_SERVER).getValue("ping", this.getHandle());
			} catch (Exception ignore) { }
		}

		return ping;
	}

	/**
	 * Get clients protocol version.
	 *
	 * @return Client protocol version.
	 */
	public int getProtocolVersion() {
		int version = MinecraftProtocol.getCurrentProtocol();

		if (this.getOfflinePlayer().isOnline()) {
			try {
				Reflection playerConnection = new Reflection("PlayerConnection", MinecraftPackage.MINECRAFT_SERVER);
				Reflection networkManager = new Reflection("NetworkManager", MinecraftPackage.MINECRAFT_SERVER);
				Object networkManagerObj = playerConnection.getValue("networkManager", this.getConnection());
				version = (int)networkManager.invokeMethod("getVersion", networkManagerObj);
			} catch (Exception ignore) { }
		}

		return version;
	}

	/**
	 * Gets the server this profile belongs to.
	 *
	 * @return BungeeServer Server object.
	 */
	@Override
	public BungeeServer getServer() {
		return NiftyBukkit.getBungeeHelper().getPlayerServer(this);
	}

	/**
	 * Gets a skull skinned to this profile's face.
	 *
	 * @return Skull item with this profiles skin face.
	 */
	public final ItemStack getSkull() {
		ItemData itemData = new ItemData(Material.SKULL_ITEM, (short)SkullType.PLAYER.ordinal());
		SkullMeta meta = (SkullMeta)itemData.getItemMeta();
		meta.setOwner(this.getName());
		meta.setDisplayName(StringUtil.format("{0}{1}''s Head", ChatColor.RESET, this.getName()));
		itemData.setItemMeta(meta);
		return itemData;
	}

	/**
	 * Checks if this profile is found anywhere on the local server.
	 *
	 * @return True if online, otherwise false.
	 */
	public boolean isOnlineLocally() {
		return this.getOfflinePlayer().isOnline();
	}

	/**
	 * Checks if this profile is found anywhere on BungeeCord or local server.
	 *
	 * @return True if online, otherwise false.
	 */
	public boolean isOnlineAnywhere() {
		return NiftyBukkit.getBungeeHelper().isPlayerOnline(this);
	}

	/**
	 * Respawns the player if they are online.
	 */
	public final void respawn() {
		if (!this.isOnlineLocally()) return;

		try {
			Reflection clientCommandObj = new Reflection("PacketPlayInClientCommand", MinecraftPackage.MINECRAFT_SERVER);
			Reflection enumCommandsObj = new Reflection("PacketPlayInClientCommand$EnumClientCommand", MinecraftPackage.MINECRAFT_SERVER);
			Reflection playerConnObj = new Reflection("PlayerConnection", MinecraftPackage.MINECRAFT_SERVER);
			Object[] titleActionEnums = enumCommandsObj.getClazz().getEnumConstants();
			playerConnObj.invokeMethod("a", this.getConnection(), clientCommandObj.newInstance(titleActionEnums[1]));
		} catch (Exception ignore) { }
	}

	@Override
	public void sendMessage(JsonMessage message) {
		if (!this.isOnlineLocally()) return;
		Reflection packetChat = new Reflection("PacketPlayOutChat", MinecraftPackage.MINECRAFT_SERVER);
		Reflection chatSerializer = BukkitReflection.getCompatibleReflection("IChatBaseComponent", "ChatSerializer");
		Object chatJson = chatSerializer.invokeMethod("a", null, message.toJSONString());
		Object packetChatObj = packetChat.newInstance(chatJson);
		this.sendPacket(packetChatObj);
	}

	@Override
	public void sendMessage(String message) {
		if (!this.isOnlineLocally()) return;
		this.getOfflinePlayer().getPlayer().sendMessage(message);
	}

	/**
	 * Sends a packet to the profiles client, if they are online.
	 *
	 * @param packet Packet to send.
	 */
	public final void sendPacket(Object packet) {
		if (!this.isOnlineLocally()) return;
		Reflection playerConnObj = new Reflection("PlayerConnection", MinecraftPackage.MINECRAFT_SERVER);
		Object playerConnectionObj = this.getConnection();
		playerConnObj.invokeMethod("sendPacket", playerConnectionObj, packet);
	}

	/**
	* Sets this player as a spectator of a target entity.
	*
	* @param target The target to spectate.
	*/
	private void spectate(Entity target) {
		if (!this.isOnlineLocally()) return;
		Reflection entityTarget = new Reflection(target.getClass().getSimpleName(), MinecraftPackage.CRAFTBUKKIT);
		Reflection entityPlayer = new Reflection("EntityPlayer", MinecraftPackage.MINECRAFT_SERVER);
		Object targetHandle = entityTarget.invokeMethod("getHandle", target);
		entityPlayer.invokeMethod("e", this.getHandle(), targetHandle);
	}

	public final void updateOpenInventory(String title, int totalSlots) {
		if (!this.isOnlineLocally()) return;
		Player player = this.getOfflinePlayer().getPlayer();

		if (player.getOpenInventory() != null) {
			Reflection entityPlayer = new Reflection("EntityPlayer", MinecraftPackage.MINECRAFT_SERVER);
			Reflection packetOpenWindow = new Reflection("PacketPlayOutOpenWindow", MinecraftPackage.MINECRAFT_SERVER);
			Reflection container = new Reflection("Container", MinecraftPackage.MINECRAFT_SERVER);
			Reflection chatMessage = new Reflection("ChatMessage", MinecraftPackage.MINECRAFT_SERVER);

			Object handle = this.getHandle();
			Object chatMessageObj = chatMessage.newInstance(title, new Object[] { });
			Object containerObj = entityPlayer.getValue("activeContainer", handle);
			Object packetOpenWindowObj = packetOpenWindow.newInstance(container.getValue("windowId", containerObj), "minecraft:chest", chatMessageObj, FakeInventory.calculateTotalSlots(totalSlots));
			this.sendPacket(packetOpenWindowObj);
			entityPlayer.invokeMethod("updateInventory", handle, containerObj);
		}
	}

}