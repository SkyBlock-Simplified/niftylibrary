package net.netcoding.nifty.common._new_.minecraft.event.player;

import net.netcoding.nifty.common._new_.minecraft.GameMode;
import net.netcoding.nifty.common._new_.minecraft.event.Cancellable;

public interface PlayerGameModeChangeEvent extends Cancellable, PlayerEvent {

	GameMode getNewGameMode();

}