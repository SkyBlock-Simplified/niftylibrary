package net.netcoding.niftybukkit.mojang;

import java.net.InetSocketAddress;
import java.util.UUID;
import java.util.regex.Pattern;

import net.netcoding.niftybukkit.NiftyBukkit;
import net.netcoding.niftybukkit.util.StringUtil;

import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

/**
 * Container for a players uuid and name.
 */
public class MojangProfile {

	private String id;
	private UUID uuid;
	private String name;
	private String ip;
	private int port;
	private InetSocketAddress ipAddress;
	private int updated = (int)(System.currentTimeMillis() / 1000);
	private static final Pattern UUID_FIX = Pattern.compile("(\\w{8})(\\w{4})(\\w{4})(\\w{4})(\\w{12})");

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
	 * Gets the players name associated to this UUID.
	 * 
	 * @return Current player name.
	 */
	// TODO: Send update across BungeeCord to update players name
	// if change is detected.
	public String getName() {
		Player player = NiftyBukkit.getPlugin().getServer().getPlayer(this.getUniqueId());

		if (player == null || player.getName().equals(this.name))
			return this.name;

		return this.name = player.getName();
	}

	/**
	 * Gets the players offline player object associated to this UUID.
	 * 
	 * @return Offline player object.
	 */
	public OfflinePlayer getOfflinePlayer() {
		return NiftyBukkit.getPlugin().getServer().getOfflinePlayer(this.getUniqueId());
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
		return System.currentTimeMillis() / 1000 - this.updated >= 600;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (StringUtil.isEmpty(this.getName()) ? 0 : this.getName().hashCode());
		result = prime * result + (StringUtil.isEmpty(this.id) ? 0 : this.getUniqueId().hashCode());
		result = prime * result + this.updated;
		return result;
	}

	/**
	 * Checks if this profile is found anywhere on BungeeCord.
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