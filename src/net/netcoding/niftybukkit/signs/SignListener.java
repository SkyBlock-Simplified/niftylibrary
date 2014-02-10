package net.netcoding.niftybukkit.signs;

import net.netcoding.niftybukkit.signs.events.*;

public interface SignListener {

	public void onSignBreak(SignBreakEvent event);

	public void onSignCreate(SignCreateEvent event);

	public void onSignInteract(SignInteractEvent event);

	public void onSignUpdate(SignUpdateEvent event);

}