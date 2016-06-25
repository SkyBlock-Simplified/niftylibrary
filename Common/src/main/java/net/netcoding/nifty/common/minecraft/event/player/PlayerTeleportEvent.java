package net.netcoding.nifty.common.minecraft.event.player;

public interface PlayerTeleportEvent extends PlayerMoveEvent {

	TeleportCause getCause();

	enum TeleportCause {

		ENDER_PEARL,
		COMMAND,
		PLUGIN,
		NETHER_PORTAL,
		END_PORTAL,
		SPECTATE,
		END_GATEWAY,
		CHORUS_FRUIT,
		UNKNOWN

	}

}