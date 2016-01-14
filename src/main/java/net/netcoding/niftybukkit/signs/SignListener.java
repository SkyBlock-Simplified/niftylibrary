package net.netcoding.niftybukkit.signs;

import net.netcoding.niftybukkit.signs.events.SignBreakEvent;
import net.netcoding.niftybukkit.signs.events.SignCreateEvent;
import net.netcoding.niftybukkit.signs.events.SignInteractEvent;
import net.netcoding.niftybukkit.signs.events.SignUpdateEvent;

public interface SignListener {

	void onSignBreak(SignBreakEvent event);

	void onSignCreate(SignCreateEvent event);

	void onSignInteract(SignInteractEvent event);

	void onSignUpdate(SignUpdateEvent event);

}