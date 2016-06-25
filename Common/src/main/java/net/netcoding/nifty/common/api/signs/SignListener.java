package net.netcoding.nifty.common.api.signs;

import net.netcoding.nifty.common.api.signs.events.SignBreakEvent;
import net.netcoding.nifty.common.api.signs.events.SignCreateEvent;
import net.netcoding.nifty.common.api.signs.events.SignInteractEvent;
import net.netcoding.nifty.common.api.signs.events.SignUpdateEvent;

public interface SignListener {

	void onSignBreak(SignBreakEvent event);

	void onSignCreate(SignCreateEvent event);

	void onSignInteract(SignInteractEvent event);

	void onSignUpdate(SignUpdateEvent event);

}