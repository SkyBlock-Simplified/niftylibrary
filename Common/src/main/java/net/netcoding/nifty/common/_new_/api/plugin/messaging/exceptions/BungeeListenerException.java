package net.netcoding.nifty.common._new_.api.plugin.messaging.exceptions;

import net.netcoding.nifty.common._new_.api.plugin.messaging.BungeeHelper;
import net.netcoding.nifty.core.util.StringUtil;

public final class BungeeListenerException extends UnsupportedOperationException {

	public BungeeListenerException() {
		super(StringUtil.format("No {0} listener available to query!", BungeeHelper.BUNGEE_CHANNEL));
	}

}