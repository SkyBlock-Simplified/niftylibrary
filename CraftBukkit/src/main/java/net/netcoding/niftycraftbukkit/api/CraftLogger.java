package net.netcoding.niftycraftbukkit.api;

import net.netcoding.niftybukkit._new_.api.BukkitLogger;

import java.util.logging.Level;
import java.util.logging.Logger;

public final class CraftLogger extends BukkitLogger {

	private final Logger logger;

	public CraftLogger(Logger logger) {
		this.logger = logger;
	}

	@Override
	protected void logInfo(String message) {
		this.logger.log(Level.INFO, message);
	}

	@Override
	protected void logSevere(String message, Throwable exception) {
		this.logger.log(Level.SEVERE, message, exception);
	}

	@Override
	protected void logWarn(String message) {
		this.logger.log(Level.WARNING, message);
	}

}