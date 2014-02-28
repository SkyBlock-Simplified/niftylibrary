package net.netcoding.niftybukkit.utilities;

@SuppressWarnings("serial")
public class ProtocolLibNotFoundException extends ClassNotFoundException {

	public ProtocolLibNotFoundException() {
		super("The Bukkit plugin ProtocolLib was not found!");
	}

}