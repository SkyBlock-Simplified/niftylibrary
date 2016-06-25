package net.netcoding.nifty.common.minecraft.event.player;

import net.netcoding.nifty.common.minecraft.GameMode;
import net.netcoding.nifty.common.minecraft.event.Cancellable;

public interface PlayerGameModeChangeEvent extends Cancellable, PlayerEvent {

	GameMode getNewGameMode();

}