package net.netcoding.niftybukkit.mojang.exceptions;

import java.util.List;

import net.netcoding.niftybukkit.util.StringUtil;

@SuppressWarnings("serial")
public class ProfileNotFoundException extends Exception {

	ProfileNotFoundException(String message) {
		super(message);
	}

	public static ProfileNotFoundException InvalidUUID(String uuid) {
		return new ProfileNotFoundException(String.format("The mojang profile data for uuid [%s] could not be found!", uuid));
	}

	public static ProfileNotFoundException InvalidUsername(String name) {
		return new ProfileNotFoundException(String.format("The mojang profile data for user [%s] could not be found!", name));
	}

	public static ProfileNotFoundException InvalidUsernames(List<String> names) {
		return new ProfileNotFoundException(String.format("The mojang profile data for users [%s] could not be found!", StringUtil.implode(", ", names)));
	}

}