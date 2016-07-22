package net.netcoding.nifty.common.mojang;

import com.google.gson.JsonObject;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import net.netcoding.nifty.common.Nifty;
import net.netcoding.nifty.common.api.plugin.messaging.BungeeServer;
import net.netcoding.nifty.common.minecraft.OfflinePlayer;
import net.netcoding.nifty.common.minecraft.block.BlockFace;
import net.netcoding.nifty.common.minecraft.entity.Entity;
import net.netcoding.nifty.common.minecraft.entity.living.human.Player;
import net.netcoding.nifty.common.minecraft.inventory.item.ItemStack;
import net.netcoding.nifty.common.minecraft.inventory.type.PlayerInventory;
import net.netcoding.nifty.common.minecraft.material.Material;
import net.netcoding.nifty.common.reflection.MinecraftPackage;
import net.netcoding.nifty.common.reflection.MinecraftReflection;
import net.netcoding.nifty.common.util.LocationUtil;
import net.netcoding.nifty.core.api.color.ChatColor;
import net.netcoding.nifty.core.mojang.MojangProfile;
import net.netcoding.nifty.core.reflection.Reflection;
import net.netcoding.nifty.core.util.StringUtil;
import net.netcoding.nifty.core.util.json.JsonMessage;

import java.util.Arrays;
import java.util.Collection;

public abstract class MinecraftMojangProfile extends MojangProfile<MinecraftMojangProfile> {

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
	public final BungeeServer<MinecraftMojangProfile> getServer() {
		return Nifty.getBungeeHelper().getPlayerServer(this);
	}

	/**
	 * Gets a skull skinned to this profile's face.
	 *
	 * @return Skull item with this profiles skin face.
	 */
	public final ItemStack getSkull() {
		return Nifty.getMiniBlockDatabase().create(this.getUniqueId(), StringUtil.format("{0}{1}''s Head", ChatColor.RESET, this.getName()));
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
	 * Opens a book for this profile.
	 *
	 * @param title The title of the book.
	 * @param author The author of the book.
	 * @param pages The content of the pages.
	 */
	public final void openBook(String title, String author, String... pages) {
		this.openBook(title, author, Arrays.asList(pages));
	}

	/**
	 * Opens a book for this profile.
	 *
	 * @param title The title of the book.
	 * @param author The author of the book.
	 * @param pages The content of the pages.
	 */
	public final void openBook(String title, String author, Collection<String> pages) {
		if (!this.isOnlineLocally()) return;
		ItemStack book = ItemStack.of(Material.WRITTEN_BOOK);
		book.getNbt().put("title", title);
		book.getNbt().put("author", author);
		book.getNbt().put("pages", Nifty.getNbtFactory().createList(pages));
		PlayerInventory inventory = this.getOfflinePlayer().getPlayer().getInventory();
		int slot = inventory.getHeldItemSlot();
		ItemStack old = inventory.getItem(slot);
		inventory.setItem(slot, book);
		ByteBuf buf = Unpooled.buffer(256);
		buf.setByte(0, (byte)0);
		buf.writerIndex(1);
		Object packetCustomObj = MinecraftReflection.CUSTOM_PAYLOD.newInstance("MC|BOpen", MinecraftReflection.DATA_SERIALIZER.newInstance(buf));
		this.sendPacket(packetCustomObj);
		inventory.setItem(slot, old);
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
		Reflection chatSerializer = MinecraftReflection.getCompatibleReflection("IChatBaseComponent", "ChatSerializer");
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