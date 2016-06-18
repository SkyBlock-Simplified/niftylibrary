package net.netcoding.nifty.common._new_.mojang;

import com.google.gson.JsonObject;
import net.netcoding.nifty.common.Nifty;
import net.netcoding.nifty.common._new_.api.plugin.messaging.BungeeServer;
import net.netcoding.nifty.common._new_.minecraft.OfflinePlayer;
import net.netcoding.nifty.common._new_.minecraft.block.BlockFace;
import net.netcoding.nifty.common._new_.minecraft.entity.Entity;
import net.netcoding.nifty.common._new_.minecraft.entity.living.Player;
import net.netcoding.nifty.common._new_.minecraft.inventory.item.ItemStack;
import net.netcoding.nifty.common._new_.minecraft.inventory.item.SkullType;
import net.netcoding.nifty.common._new_.minecraft.material.Material;
import net.netcoding.nifty.common._new_.reflection.BukkitReflection;
import net.netcoding.nifty.common._new_.reflection.MinecraftPackage;
import net.netcoding.nifty.common._new_.util.LocationUtil;
import net.netcoding.niftycore.api.ChatColor;
import net.netcoding.niftycore.mojang.MojangProfile;
import net.netcoding.niftycore.reflection.Reflection;
import net.netcoding.niftycore.util.StringUtil;
import net.netcoding.niftycore.util.json.JsonMessage;
import org.bukkit.inventory.meta.SkullMeta;

public abstract class BukkitMojangProfile extends MojangProfile {

	protected BukkitMojangProfile() { }

	/**
	 * Checks if the profile is associated to this profile.
	 *
	 * @param player Profile to check.
	 * @return True if associated, otherwise false.
	 */
	public final boolean belongsTo(OfflinePlayer player) {
		return player != null && player.getUniqueId().equals(this.getUniqueId());
	}

	/**
	 * Gets the direction the client is looking
	 *
	 * @return The direction the client is looking
	 */
	public final BlockFace getFacing() {
		if (this.isOnlineLocally()) {
			float yaw = this.getOfflinePlayer().getPlayer().getLocation().getYaw();
			return LocationUtil.yawToFace(yaw);
		}

		return null;
	}

	/**
	 * Gets if the client is gliding.
	 * <p>
	 * Returns false before 1.9.
	 *
	 * @return True if gliding, otherwise false.
	 */
	public abstract boolean isGliding();

	/**
	 * Gets clients locale.
	 *
	 * @return Clients locale.
	 */
	public abstract String getLocale();

	/**
	 * Gets the clients name associated to this UUID.
	 *
	 * @return Current clients name.
	 */
	@Override
	public final String getName() {
		Player player = Nifty.getServer().getPlayer(this.getUniqueId());

		if (player == null || player.getName().equals(super.getName()))
			return super.getName();

		if (Nifty.getBungeeHelper().getDetails().isDetected()) {
			JsonObject json = new JsonObject();
			json.addProperty("id", this.getUniqueId().toString());
			json.addProperty("name", this.getName());
			json.addProperty("ip", this.getAddress().getAddress().getHostAddress());
			json.addProperty("port", this.getAddress().getPort());
			String serverName = Nifty.getBungeeHelper().getServerName();
			Nifty.getBungeeHelper().forward(serverName, "PlayerUpdate", json.toString(), serverName);
			Nifty.getBungeeHelper().forward("ONLINE", "PlayerUpdate", json.toString(), serverName);
		}

		return this.name = player.getName();
	}

	/**
	 * Gets the clients offline player object associated to this profile.
	 *
	 * @return Offline client object.
	 */
	public final OfflinePlayer getOfflinePlayer() {
		return Nifty.getServer().getOfflinePlayer(this.getUniqueId());
	}

	/**
	 * Get clients ping.
	 *
	 * @return Client latency with the server.
	 */
	public abstract int getPing();

	/**
	 * Get clients protocol version.
	 *
	 * @return Client protocol version.
	 */
	public abstract int getProtocolVersion();

	/**
	 * Gets the server this profile belongs to.
	 *
	 * @return BungeeServer Server object.
	 */
	@Override
	public final BungeeServer<BukkitMojangProfile> getServer() {
		return Nifty.getBungeeHelper().getPlayerServer(this);
	}

	/**
	 * Gets a skull skinned to this profile's face.
	 *
	 * @return Skull item with this profiles skin face.
	 */
	public final ItemStack getSkull() {
		ItemStack itemData = ItemStack.of(Material.SKULL_ITEM, (short)SkullType.PLAYER.ordinal());
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
	public final boolean isOnlineLocally() {
		return this.getOfflinePlayer().isOnline();
	}

	/**
	 * Checks if this profile is found anywhere on BungeeCord or local server.
	 *
	 * @return True if online, otherwise false.
	 */
	@Override
	public final boolean isOnline() {
		return Nifty.getBungeeHelper().isPlayerOnline(this);
	}

	/**
	 * Respawns the client if they are online.
	 */
	public abstract void respawn();

	/**
	 * Send the client a json chat message.
	 *
	 * @param message Json message to send.
	 */
	@Override
	public final void sendMessage(JsonMessage message) {
		if (!this.isOnlineLocally()) return;
		Reflection packetChat = new Reflection("PacketPlayOutChat", MinecraftPackage.MINECRAFT_SERVER);
		Reflection chatSerializer = BukkitReflection.getCompatibleReflection("IChatBaseComponent", "ChatSerializer");
		Object chatJson = chatSerializer.invokeMethod("a", null, message.toJSONString());
		Object packetChatObj = packetChat.newInstance(chatJson);
		this.sendPacket(packetChatObj);
	}

	/**
	 * Send the client a regular chat message.
	 *
	 * @param message Message to send.
	 */
	@Override
	public final void sendMessage(String message) {
		if (!this.isOnlineLocally()) return;
		this.getOfflinePlayer().getPlayer().sendMessage(message);
	}

	/**
	 * Sends a packet to the profiles client, if they are online.
	 *
	 * @param packet Packet to send.
	 */
	public abstract void sendPacket(Object packet);

	/**
	* Sets this client as a spectator of a target entity.
	*
	* @param target The target to spectate.
	*/
	protected abstract void spectate(Entity target);

}