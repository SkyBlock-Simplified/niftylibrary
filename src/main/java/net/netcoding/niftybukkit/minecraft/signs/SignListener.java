package net.netcoding.niftybukkit.minecraft.signs;

import net.netcoding.niftybukkit.minecraft.signs.events.SignBreakEvent;
import net.netcoding.niftybukkit.minecraft.signs.events.SignCreateEvent;
import net.netcoding.niftybukkit.minecraft.signs.events.SignInteractEvent;
import net.netcoding.niftybukkit.minecraft.signs.events.SignUpdateEvent;

public interface SignListener {

	void onSignBreak(SignBreakEvent event);

	void onSignCreate(SignCreateEvent event);

	void onSignInteract(SignInteractEvent event);

	void onSignUpdate(SignUpdateEvent event);

}