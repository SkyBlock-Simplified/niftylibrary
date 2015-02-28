package net.netcoding.niftybukkit.mojang.exceptions;

import net.netcoding.niftybukkit.util.StringUtil;

import org.bukkit.OfflinePlayer;

@SuppressWarnings("serial")
public class ProfileNotFoundException extends RuntimeException {

	public ProfileNotFoundException(TYPE type, Object obj) {
		super(getCustomMessage(type, obj));
	}

	public enum TYPE {
		OFFLINE_PLAYERS,
		OFFLINE_PLAYER,
		UNIQUE_ID,
		USERNAMES,
		USERNAME,
		NULL
	}

	private static String getCustomMessage(TYPE type, Object obj) {
		switch (type) {
		case OFFLINE_PLAYERS:
			String players = "";
			OfflinePlayer[] oplayers = (OfflinePlayer[])obj;

			for (OfflinePlayer oplayer : oplayers)
				players += StringUtil.format("'{'{0},{1}'}'", oplayer.getUniqueId(), oplayer.getName());

			return StringUtil.format("The profile data for offline players '{'{0}'}' could not be found!", players);
		case OFFLINE_PLAYER:
			OfflinePlayer oplayer = (OfflinePlayer)obj;
			return StringUtil.format("The profile data for offline player '{'{0},{1}'}' could not be found!", oplayer.getUniqueId(), oplayer.getName());
		case UNIQUE_ID:
			return StringUtil.format("The profile data for uuid {0} could not be found!", obj);
		case USERNAMES:
			return StringUtil.format("The profile data for users '{'{0}'}' could not be found!", StringUtil.implode(", ", (String[])obj));
		case USERNAME:
			return StringUtil.format("The profile data for user {0} could not be found!", obj);
		default:
			return StringUtil.format("The profile data for {0} could not be found!", obj);
		}
	}

}