package net.netcoding.niftybukkit.minecraft.libraries;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.events.PacketContainer;
import net.netcoding.niftybukkit.NiftyBukkit;
import net.netcoding.niftybukkit.mojang.BukkitMojangProfile;
import net.netcoding.niftybukkit.reflection.MinecraftPackage;
import net.netcoding.niftycore.reflection.Reflection;
import org.bukkit.entity.Player;

import java.lang.reflect.InvocationTargetException;

public class SkyFactory {

	private final static Reflection GAME_STATE_CHANGE = new Reflection("PacketPlayOutGameStateChange", MinecraftPackage.MINECRAFT_SERVER);

	@Deprecated
	public static PacketContainer changeGameState(int reason, float value) {
		PacketContainer packet = new PacketContainer(PacketType.Play.Server.GAME_STATE_CHANGE);
		packet.getIntegers().write(0, reason);
		packet.getFloat().write(0, value);
		return packet;
	}

	public enum SkyColor {

		DARK_GOLD(2, 0),
		DARK_RED(3, 0),
		DARKER_RED(4, 0),
		DARKEST_RED(5, 0),
		BLACK(2, 1),
		DARKER_BLUE(3, 1),
		DARK_BLUE(4, 1),
		AQUA(5, 1),
		LIGHT_BLUE(6, 1),
		DARK_BLUE_2(3, 2),
		AQUA_2(4, 2),
		LIGHT_BLUE_2(5, 2),
		BLUE_GOLD(3, 3),
		AQUA_3(4, 3),
		SEPIA_BLACK(1, 5),
		SEPIA_DARKER_BLACK(2, 5),
		SEPIA_DARKEST_BLACK(1, 6),
		LIGHT_SEPIA_BLACK(2, 6),
		BLUE_SKY_DARK_TERRAIN(3, 9);

		private final int value1;
		private final float value2;

		SkyColor(int value1, float value2) {
			this.value1 = value1;
			this.value2 = value2;
		}

		public void show(BukkitMojangProfile profile) throws Exception {
			if (!profile.isOnlineLocally()) return;

			profile.sendPacket(GAME_STATE_CHANGE.newInstance(7, this.value1));
			profile.sendPacket(GAME_STATE_CHANGE.newInstance(8, this.value2));
		}

		@Deprecated
		public void show(Player player) throws InvocationTargetException {
			NiftyBukkit.getProtocolManager().sendServerPacket(player, changeGameState(7, this.value1));
			NiftyBukkit.getProtocolManager().sendServerPacket(player, changeGameState(8, this.value2));
		}

	}

}