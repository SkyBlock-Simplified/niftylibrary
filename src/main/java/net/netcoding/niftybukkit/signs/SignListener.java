package net.netcoding.niftybukkit.signs;

import net.netcoding.niftybukkit.signs.events.SignBreakEvent;
import net.netcoding.niftybukkit.signs.events.SignCreateEvent;
import net.netcoding.niftybukkit.signs.events.SignInteractEvent;
import net.netcoding.niftybukkit.signs.events.SignUpdateEvent;

public interface SignListener {

	public void onSignBreak(SignBreakEvent event);

	public void onSignCreate(SignCreateEvent event);

	public void onSignInteract(SignInteractEvent event);

	public void onSignUpdate(SignUpdateEvent event);

}