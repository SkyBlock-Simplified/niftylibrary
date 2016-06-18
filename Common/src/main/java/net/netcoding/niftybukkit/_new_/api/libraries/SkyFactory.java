package net.netcoding.niftybukkit._new_.api.libraries;

import net.netcoding.niftybukkit.Nifty;
import net.netcoding.niftybukkit._new_.mojang.BukkitMojangProfile;
import net.netcoding.niftybukkit._new_.reflection.MinecraftPackage;
import net.netcoding.niftycore.reflection.Reflection;

public enum SkyFactory {

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

	private final static Reflection GAME_STATE_CHANGE = new Reflection("PacketPlayOutGameStateChange", MinecraftPackage.MINECRAFT_SERVER);

	private final int value1;
	private final float value2;

	SkyFactory(int value1, float value2) {
		this.value1 = value1;
		this.value2 = value2;
	}

	public void show(BukkitMojangProfile profile) throws Exception {
		if (profile.isOnlineLocally()) {
			profile.sendPacket(GAME_STATE_CHANGE.newInstance(7, this.value1));
			profile.sendPacket(GAME_STATE_CHANGE.newInstance(8, this.value2));
		}
	}

	public static void show(SkyFactory color) {
		for (BukkitMojangProfile profile : Nifty.getBungeeHelper().getPlayerList())
			show(color, profile);
	}

	public static void show(SkyFactory color, BukkitMojangProfile profile) {
		if (profile.isOnlineLocally()) {
			profile.sendPacket(GAME_STATE_CHANGE.newInstance(7, color.value1));
			profile.sendPacket(GAME_STATE_CHANGE.newInstance(8, color.value2));
		}
	}

}