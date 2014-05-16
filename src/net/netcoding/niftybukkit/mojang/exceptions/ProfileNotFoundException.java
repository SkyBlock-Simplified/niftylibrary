package net.netcoding.niftybukkit.mojang.exceptions;

import java.util.List;
import java.util.UUID;

import net.netcoding.niftybukkit.util.StringUtil;

@SuppressWarnings("serial")
public class ProfileNotFoundException extends RuntimeException {

	ProfileNotFoundException(String message) {
		super(message);
	}

	public static ProfileNotFoundException InvalidPlayer() {
		return new ProfileNotFoundException(String.format("The mojang profile data for the passed player could not be found!"));
	}

	public static ProfileNotFoundException InvalidUUID(UUID uuid) {
		return new ProfileNotFoundException(String.format("The mojang profile data for uuid [%s] could not be found!", uuid.toString()));
	}

	public static ProfileNotFoundException InvalidUsername(String name) {
		return new ProfileNotFoundException(String.format("The mojang profile data for user [%s] could not be found!", name));
	}

	public static ProfileNotFoundException InvalidUsernames(List<String> names) {
		return new ProfileNotFoundException(String.format("The mojang profile data for users [%s] could not be found!", StringUtil.implode(", ", names)));
	}

}