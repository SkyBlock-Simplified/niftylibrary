package net.netcoding.niftybukkit.mojang.exceptions;

import net.netcoding.niftybukkit.util.StringUtil;

import org.bukkit.OfflinePlayer;

/**
 * Custom exception for use by the MojangRepository
 */
@SuppressWarnings("serial")
public class ProfileNotFoundException extends RuntimeException {

	private final String details;
	private final Reason reason;
	private final LookupType type;

	/**
	 * Create a new exception instance.
	 * 
	 * @param reason Mojang response type.
	 * @param type   Lookup request type.
	 * @param obj    Object to be used in the exception message.
	 */
	public ProfileNotFoundException(Reason reason, LookupType type, Object obj) {
		this(reason, type, null, obj);
	}

	/**
	 * Create a new exception instance.
	 * 
	 * @param reason    Mojang response type.
	 * @param throwable Error that occured.
	 * @param type      Lookup request type.
	 * @param obj       Object to be used in the exception message.
	 */
	public ProfileNotFoundException(Reason reason, LookupType type, Throwable throwable, Object obj) {
		super(throwable == null ? getCustomMessage(type, obj) : StringUtil.format("{0}: {1}: {2}", getCustomMessage(type, obj), throwable.getClass().getName(), throwable.getMessage()), throwable);
		this.reason = reason;
		this.type = type;
		this.details = getCustomMessage(this.type, obj);
	}

	public String getDetails() {
		return this.details;
	}

	public Reason getReason() {
		return this.reason;
	}

	public LookupType getType() {
		return this.type;
	}

	public enum Reason {

		NO_PREMIUM_PLAYER("Unable to locate profile data for a valid premium player!"),
		EXCEPTION("An unknown error has occurred!"),
		RATE_LIMITED("You have been rate limited!");

		private final String cause;

		private Reason(String cause) {
			this.cause = cause;
		}

		public String getCause() {
			return this.cause;
		}

	}

	public enum LookupType {

		OFFLINE_PLAYERS,
		OFFLINE_PLAYER,
		UNIQUE_ID,
		USERNAMES,
		USERNAME;

	}

	private static String getCustomMessage(LookupType type, Object obj) {
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
			return StringUtil.format("The profile data for ''{0}'' could not be found!", obj);
		}
	}

}