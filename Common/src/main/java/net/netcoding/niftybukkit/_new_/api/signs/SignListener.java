package net.netcoding.niftybukkit._new_.api.signs;

import net.netcoding.niftybukkit._new_.api.signs.events.SignBreakEvent;
import net.netcoding.niftybukkit._new_.api.signs.events.SignCreateEvent;
import net.netcoding.niftybukkit._new_.api.signs.events.SignInteractEvent;
import net.netcoding.niftybukkit._new_.api.signs.events.SignUpdateEvent;

public interface SignListener {

	void onSignBreak(SignBreakEvent event);

	void onSignCreate(SignCreateEvent event);

	void onSignInteract(SignInteractEvent event);

	void onSignUpdate(SignUpdateEvent event);

}