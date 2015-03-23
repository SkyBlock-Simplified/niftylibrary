package net.netcoding.niftybukkit.mojang;

import java.net.InetSocketAddress;
import java.util.UUID;
import java.util.regex.Pattern;

import net.netcoding.niftybukkit.NiftyBukkit;
import net.netcoding.niftybukkit.minecraft.BungeeServer;
import net.netcoding.niftybukkit.reflection.MinecraftPackage;
import net.netcoding.niftybukkit.reflection.Reflection;
import net.netcoding.niftybukkit.util.StringUtil;

import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

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
		if (this.getOfflinePlayer().isOnline()) {
			if (StringUtil.notEmpty(this.ip) && this.ipAddress == null)
				this.ipAddress = InetSocketAddress.createUnresolved(this.ip, this.port);
		}

		return this.ipAddress;
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
				Reflection craftPlayerObj = new Reflection("CraftPlayer", "entity", MinecraftPackage.CRAFTBUKKIT);
				Reflection entityPlayerObj = new Reflection("EntityPlayer", MinecraftPackage.MINECRAFT_SERVER);
				Object craftPlayer = craftPlayerObj.getClazz().cast(this.getOfflinePlayer().getPlayer());
				Object playerHandle = craftPlayerObj.invokeMethod("getHandle", craftPlayer);
				locale = (String)entityPlayerObj.getValue("locale", playerHandle);
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

		// TODO: Send update across BungeeCord to update players name if change is detected.
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
				Reflection craftPlayerObj = new Reflection("CraftPlayer", "entity", MinecraftPackage.CRAFTBUKKIT);
				Reflection entityPlayerObj = new Reflection("EntityPlayer", MinecraftPackage.MINECRAFT_SERVER);
				Object craftPlayer = craftPlayerObj.getClazz().cast(this.getOfflinePlayer().getPlayer());
				Object playerHandle = craftPlayerObj.invokeMethod("getHandle", craftPlayer);
				ping = (int)entityPlayerObj.getValue("ping", playerHandle);
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
				Reflection playerObj = new Reflection("Player", Player.class.toString());
				Reflection playerConnObj = new Reflection("PlayerConnection", MinecraftPackage.MINECRAFT_SERVER);
				Reflection networkManagerObj = new Reflection("NetworkManager", MinecraftPackage.MINECRAFT_SERVER);
				Object playerHandle = playerObj.invokeMethod("getHandle", this.getOfflinePlayer().getPlayer());
				Object playerConnection = playerConnObj.getValue("playerConnection", playerHandle);
				Object networkManager = playerConnObj.getValue("networkManager", playerConnection);
				version = (int)networkManagerObj.invokeMethod("getVersion", networkManager);
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
	 * Checks if this profile is found anywhere on BungeeCord or local server.
	 * 
	 * @return True if online, otherwise false.
	 */
	public boolean isOnline() {
		return NiftyBukkit.getBungeeHelper().isPlayerOnline(this);
	}

	@Override
	public String toString() {
		return StringUtil.format("'{'{0},{1}'}'", this.getUniqueId(), this.getName());
	}

}