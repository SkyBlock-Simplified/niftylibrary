package net.netcoding.nifty.craftbukkit.mojang;

import net.netcoding.nifty.common.minecraft.entity.Entity;
import net.netcoding.nifty.common.mojang.MinecraftMojangProfile;
import net.netcoding.nifty.common.reflection.MinecraftPackage;
import net.netcoding.nifty.common.reflection.MinecraftProtocol;
import net.netcoding.nifty.common.reflection.MinecraftReflection;
import net.netcoding.nifty.core.reflection.Reflection;
import net.netcoding.nifty.craftbukkit.minecraft.entity.CraftEntity;
import net.netcoding.nifty.craftbukkit.minecraft.entity.living.human.CraftPlayer;
import net.netcoding.nifty.craftbukkit.reflection.CraftMinecraftPackage;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;

public final class CraftMojangProfile extends MinecraftMojangProfile {

	private CraftMojangProfile() { }

	/**
	 * Gets the connection of this profiles client, if they are online.
	 *
	 * @return Connection of the client.
	 */
	public final Object getConnection() {
		return this.isOnlineLocally() ? MinecraftReflection.ENTITY_PLAYER.getValue(MinecraftReflection.PLAYER_CONNECTION.getClazz(), this.getHandle()) : null;
	}

	/**
	 * Gets the handle of this profiles client, if they are online.
	 *
	 * @return Handle of the client.
	 */
	public final Object getHandle() {
		if (!this.isOnlineLocally()) return null;
		Reflection craftPlayer = new MinecraftReflection("CraftPlayer", "entity", CraftMinecraftPackage.CRAFTBUKKIT);
		Object craftPlayerObj = craftPlayer.getClazz().cast(((CraftPlayer)this.getOfflinePlayer().getPlayer()).getHandle());
		return craftPlayer.invokeMethod("getHandle", craftPlayerObj);
	}

	@Override
	public final boolean isGliding() {
		if (this.isOnlineLocally()) {
			if (MinecraftProtocol.getCurrentProtocol() >= MinecraftProtocol.v1_9_2.getProtocol())
				return this.getOfflinePlayer().getPlayer().isGliding();
			else if (MinecraftProtocol.getCurrentProtocol() >= MinecraftProtocol.v1_9_1_pre1.getProtocol())
				return (boolean) MinecraftReflection.ENTITY_PLAYER.invokeMethod("cB", this.getHandle());
		}

		return false;
	}

	@Override
	public String getLocale() {
		String locale = "en_EN";

		if (this.isOnlineLocally()) {
			try {
				locale = (String) MinecraftReflection.ENTITY_PLAYER.getValue("locale", this.getHandle());
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
				ping = (int) MinecraftReflection.ENTITY_PLAYER.getValue("ping", this.getHandle());
			} catch (Exception ignore) { }
		}

		return ping;
	}

	@Override
	public int getProtocolVersion() {
		int version = MinecraftProtocol.getCurrentProtocol();

		if (this.getOfflinePlayer().isOnline()) {
			try {
				Object networkManagerObj = MinecraftReflection.PLAYER_CONNECTION.getValue(MinecraftReflection.NETWORK_MANAGER, this.getConnection());
				version = (int)MinecraftReflection.NETWORK_MANAGER.invokeMethod("getVersion", networkManagerObj);
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
			Object[] titleActionEnums = enumCommandsObj.getClazz().getEnumConstants();
			MinecraftReflection.PLAYER_CONNECTION.invokeMethod(Void.class, this.getConnection(), clientCommandObj.newInstance(titleActionEnums[1]));
		} catch (Exception ignore) { }
	}

	@Override
	public void sendPacket(Object packet) {
		if (!this.isOnlineLocally()) return;
		MinecraftReflection.PLAYER_CONNECTION.invokeMethod(Void.class/*"sendPacket"*/, this.getConnection(), packet);
	}

	@Override
	protected void spectate(Entity target) {
		if (!this.isOnlineLocally()) return;
		org.bukkit.entity.Entity bukkitEntity = ((CraftEntity)target).getHandle();
		Reflection entityTarget = new Reflection(bukkitEntity.getClass().getSimpleName(), CraftMinecraftPackage.CRAFTBUKKIT);
		Object targetHandle = entityTarget.invokeMethod(MinecraftReflection.ENTITY.getClazz(), bukkitEntity);

		if (MinecraftProtocol.getCurrentProtocol() >= MinecraftProtocol.v1_9_pre1.getProtocol())
			MinecraftReflection.ENTITY_PLAYER.invokeMethod("setSpectatorTarget", this.getHandle(), targetHandle);
		else
			MinecraftReflection.ENTITY_PLAYER.invokeMethod("e", this.getHandle(), targetHandle);
	}

}