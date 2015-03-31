package net.netcoding.niftybukkit.mojang;

import java.net.InetSocketAddress;
import java.util.UUID;
import java.util.regex.Pattern;

import net.netcoding.niftybukkit.NiftyBukkit;
import net.netcoding.niftybukkit.inventory.items.ItemData;
import net.netcoding.niftybukkit.minecraft.BungeeServer;
import net.netcoding.niftybukkit.reflection.MinecraftPackage;
import net.netcoding.niftybukkit.reflection.Reflection;
import net.netcoding.niftybukkit.util.StringUtil;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.SkullType;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;

import com.google.gson.JsonObject;

/**
 * Container for a players unique id and name.
 */
public class MojangProfile {

	private static final Pattern UUID_FIX = Pattern.compile("(\\w{8})(\\w{4})(\\w{4})(\\w{4})(\\w{12})");
	private String id;
	private UUID uuid;
	private String name;
	private String ip;
	private int port;
	private InetSocketAddress ipAddress;
	private long updated = System.currentTimeMillis();
	// http://skins.minecraft.net/MinecraftSkins/<username>.png

	private MojangProfile() { }

	/**
	 * Checks if the player is associated to this profile.
	 * 
	 * @param oplayer Offline player to check.
	 * @return True if associated, otherwise false.
	 */
	public boolean belongsTo(OfflinePlayer oplayer) {
		if (oplayer == null) return false;
		return oplayer.getUniqueId().equals(this.getUniqueId());
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null) return false;
		if (!(obj instanceof MojangProfile)) return false;
		if (this == obj) return true;
		MojangProfile profile = (MojangProfile)obj;
		return this.getUniqueId().equals(profile.getUniqueId());
	}

	/**
	 * Gets the ip address of the player if they are online.
	 * 
	 * @return Socket address of the player if online, otherwise null.
	 */
	public InetSocketAddress getAddress() {
		if (this.isOnlineAnywhere()) {
			if (StringUtil.notEmpty(this.ip) && this.ipAddress == null)
				this.ipAddress = InetSocketAddress.createUnresolved(this.ip, this.port);
		}

		return this.ipAddress;
	}

	/**
	 * Gets the connection of this profiles client, if they are online.
	 * 
	 * @return Connection of the client.
	 */
	public Object getConnection() throws Exception {
		return this.getOfflinePlayer().isOnline() ? new Reflection("EntityPlayer", MinecraftPackage.MINECRAFT_SERVER).getValue("playerConnection", this.getHandle()) : null;
	}

	/**
	 * Gets the handle of this profiles client, if they are online.
	 * 
	 * @return Handle of the client.
	 */
	public Object getHandle() throws Exception {
		if (!this.getOfflinePlayer().isOnline()) return null;
		Reflection craftPlayer = new Reflection("CraftPlayer", "entity", MinecraftPackage.CRAFTBUKKIT);
		Object craftPlayerObj = craftPlayer.getClazz().cast(this.getOfflinePlayer().getPlayer());
		return craftPlayer.invokeMethod("getHandle", craftPlayerObj);
	}

	/**
	 * Gets clients locale.
	 * 
	 * @return Clients locale.
	 */
	public String getLocale() {
		String locale = "en_EN";

		if (this.getOfflinePlayer().isOnline()) {
			try {
				locale = (String)new Reflection("EntityPlayer", MinecraftPackage.MINECRAFT_SERVER).getValue("locale", this.getHandle());
			} catch (Exception ex) { }
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
	public int getPing() {
		int ping = 0;

		if (this.getOfflinePlayer().isOnline()) {
			try {
				ping = (int)new Reflection("EntityPlayer", MinecraftPackage.MINECRAFT_SERVER).getValue("ping", this.getHandle());
			} catch (Exception ex) { }
		}

		return ping;
	}

	/**
	 * Get clients protocol version.
	 * 
	 * @return Client protocol version.
	 */
	public int getProtocolVersion() {
		int version = 0;

		if (this.getOfflinePlayer().isOnline()) {
			try {
				Reflection playerConnection = new Reflection("PlayerConnection", MinecraftPackage.MINECRAFT_SERVER);
				Reflection networkManager = new Reflection("NetworkManager", MinecraftPackage.MINECRAFT_SERVER);
				Object networkManagerObj = playerConnection.getValue("networkManager", this.getConnection());
				version = (int)networkManager.invokeMethod("getVersion", networkManagerObj);
			} catch (Exception ex) { }
		}

		return version;
	}

	/**
	 * Gets the server this profile belongs to.
	 * 
	 * @return BungeeServer Server object.
	 */
	public BungeeServer getServer() {
		return NiftyBukkit.getBungeeHelper().getPlayerServer(this);
	}

	/**
	 * Gets a skull skinned to this profile's face.
	 * 
	 * @return Skull item with this profiles skin face.
	 */
	public final ItemStack getSkull() {
		ItemData data = new ItemData(Material.SKULL_ITEM, (byte)SkullType.PLAYER.ordinal());
		ItemStack stack = data.getItem();
		SkullMeta meta = (SkullMeta)stack.getItemMeta();
		meta.setOwner(this.getName());
		meta.setDisplayName(ChatColor.RESET + this.getName());
		stack.setItemMeta(meta);
		return stack;
	}

	/**
	 * Gets the players UUID.
	 * 
	 * @return Player UUID.
	 */
	public UUID getUniqueId() {
		if (this.uuid == null)
			this.uuid = UUID.fromString(UUID_FIX.matcher(this.id.replace("-", "")).replaceAll("$1-$2-$3-$4-$5"));

		return this.uuid;
	}

	/**
	 * Checks if this profile has an assigned ip address.
	 * 
	 * @return True if address exists, otherwise false.
	 */
	public boolean hasAddress() {
		return this.getAddress() != null;
	}

	/**
	 * Checks if this players profile is expired.
	 * <p>
	 * This is only used for cache and can currently be ignored.
	 * 
	 * @return True if expired, otherwise false.
	 */
	public boolean hasExpired() {
		return System.currentTimeMillis() - this.updated >= 1800000;
	}

	@Override
	public int hashCode() {
		return this.getUniqueId().hashCode();
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
	public void respawn() {
		if (!this.getOfflinePlayer().isOnline()) return;

		try {
			Reflection clientCommandObj = new Reflection("PacketPlayInClientCommand", MinecraftPackage.MINECRAFT_SERVER);
			Reflection enumCommandsObj = new Reflection("PacketPlayInClientCommand$EnumClientCommand", MinecraftPackage.MINECRAFT_SERVER);
			Reflection playerConnObj = new Reflection("PlayerConnection", MinecraftPackage.MINECRAFT_SERVER);
			Object playerConnection = this.getConnection();
			Object[] titleActionEnums = enumCommandsObj.getClazz().getEnumConstants();
			playerConnObj.invokeMethod("a", playerConnection, clientCommandObj.newInstance(titleActionEnums[1]));
		} catch (Exception ex) { }
	}

	/**
	 * Sends a packet to the profiles client, if they are online.
	 * 
	 * @param packet Packet to send.
	 */
	public final void sendPacket(Object packet) throws Exception {
		if (!this.getOfflinePlayer().isOnline()) return;

		Reflection playerConnObj = new Reflection("PlayerConnection", MinecraftPackage.MINECRAFT_SERVER);
		Object playerConnectionObj = this.getConnection();
		playerConnObj.invokeMethod("sendPacket", playerConnectionObj, packet);
	}

	/**
	* Sets this player as a spectator of a target entity.
	*
	* @param target The target to spectate.
	*/
	@SuppressWarnings("unused")
	private final void spectate(Entity target) throws Exception {
		if (!this.getOfflinePlayer().isOnline()) return;

		Reflection entityTarget = new Reflection(target.getClass().getSimpleName(), MinecraftPackage.CRAFTBUKKIT);
		Reflection entityPlayer = new Reflection("EntityPlayer", MinecraftPackage.MINECRAFT_SERVER);
		Object targetHandle = entityTarget.invokeMethod("getHandle", target);
		entityPlayer.invokeMethod("e", this.getHandle(), targetHandle);
	}

	@Override
	public String toString() {
		return StringUtil.format("'{'{0},{1}'}'", this.getUniqueId(), this.getName());
	}

}