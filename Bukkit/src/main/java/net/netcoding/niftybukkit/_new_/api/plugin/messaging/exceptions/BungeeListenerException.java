package net.netcoding.niftybukkit._new_.api.plugin.messaging.exceptions;

import net.netcoding.niftybukkit._new_.api.plugin.messaging.BungeeHelper;
import net.netcoding.niftycore.util.StringUtil;

public final class BungeeListenerException extends UnsupportedOperationException {

	public BungeeListenerException() {
		super(StringUtil.format("No {0} listener available to query!", BungeeHelper.BUNGEE_CHANNEL));
	}

}