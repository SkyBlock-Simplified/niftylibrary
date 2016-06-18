package net.netcoding.niftybukkit._new_.api.plugin.messaging.exceptions;

import net.netcoding.niftycore.util.StringUtil;

public final class IllegalServerNameException extends PluginMessageException {

	public IllegalServerNameException(String serverName) {
		super(StringUtil.format("The server with name ''{0}'' does not exist!", serverName));
	}

}