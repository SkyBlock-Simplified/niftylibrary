package net.netcoding.nifty.craftbukkit.mojang;

import net.netcoding.nifty.common._new_.minecraft.entity.Entity;
import net.netcoding.nifty.common._new_.mojang.BukkitMojangProfile;
import net.netcoding.nifty.common._new_.reflection.BukkitReflection;
import net.netcoding.nifty.common._new_.reflection.MinecraftPackage;
import net.netcoding.nifty.common._new_.reflection.MinecraftProtocol;
import net.netcoding.nifty.core.reflection.Reflection;
import net.netcoding.nifty.craftbukkit.reflection.CraftMinecraftPackage;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;

public final class CraftMojangProfile extends BukkitMojangProfile {

	protected CraftMojangProfile() { }

	/**
	 * Gets the connection of this profiles client, if they are online.
	 *
	 * @return Connection of the client.
	 */
	public final Object getConnection() {
		return this.isOnlineLocally() ? BukkitReflection.NMS_ENTITY_PLAYER.getValue("playerConnection", this.getHandle()) : null;
	}

	/**
	 * Gets the handle of this profiles client, if they are online.
	 *
	 * @return Handle of the client.
	 */
	public final Object getHandle() {
		if (!this.isOnlineLocally()) return null;
		Reflection craftPlayer = new BukkitReflection("CraftPlayer", "entity", CraftMinecraftPackage.CRAFTBUKKIT);
		Object craftPlayerObj = craftPlayer.getClazz().cast(this.getOfflinePlayer().getPlayer());
		return craftPlayer.invokeMethod("getHandle", craftPlayerObj);
	}

	@Override
	public final boolean isGliding() {
		if (this.isOnlineLocally()) {
			if (MinecraftProtocol.getCurrentProtocol() >= MinecraftProtocol.v1_9_2.getProtocol())
				return this.getOfflinePlayer().getPlayer().isGliding();
			else if (MinecraftProtocol.getCurrentProtocol() >= MinecraftProtocol.v1_9_1_pre1.getProtocol())
				return (boolean)BukkitReflection.NMS_ENTITY_PLAYER.invokeMethod("cB", this.getHandle());
		}

		return false;
	}

	@Override
	public String getLocale() {
		String locale = "en_EN";

		if (this.isOnlineLocally()) {
			try {
				locale = (String)BukkitReflection.NMS_ENTITY_PLAYER.getValue("locale", this.getHandle());
			} catch (Exception ignore) { }
		}

		return locale;
	}

	public OfflinePlayer getBukkitOfflinePlayer() {
		return Bukkit.getOfflinePlayer(this.getUniqueId());
	}

	@Override
	public int getPing() {
		int ping = 0;

		if (this.isOnlineLocally()) {
			try {
				ping = (int)BukkitReflection.NMS_ENTITY_PLAYER.getValue("ping", this.getHandle());
			} catch (Exception ignore) { }
		}

		return ping;
	}

	@Override
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

	@Override
	public void respawn() {
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
	public void sendPacket(Object packet) {
		if (!this.isOnlineLocally()) return;
		Reflection playerConnObj = new Reflection("PlayerConnection", MinecraftPackage.MINECRAFT_SERVER);
		Object playerConnectionObj = this.getConnection();
		playerConnObj.invokeMethod("sendPacket", playerConnectionObj, packet);
	}

	@Override
	protected void spectate(Entity target) {
		/*if (!this.isOnlineLocally()) return;
		Reflection entityTarget = new Reflection(target.getClass().getSimpleName(), MinecraftPackage.CRAFTBUKKIT);
		Object targetHandle = entityTarget.invokeMethod("getHandle", target); // TODO
		BukkitReflection.NMS_ENTITY_PLAYER.invokeMethod("e", this.getHandle(), targetHandle);*/
	}

}