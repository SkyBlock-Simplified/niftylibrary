package net.netcoding.niftybukkit.mojang.exceptions;

import java.util.List;

import net.netcoding.niftybukkit.util.StringUtil;

@SuppressWarnings("serial")
public class ProfileNotFoundException extends Exception {

	public ProfileNotFoundException(String name) {
		super(String.format("The mojang profile data for user %s could not be found!", name));
	}

	public ProfileNotFoundException(List<String> names) {
		super(String.format("The mojang profile data for users %s could not be found!", StringUtil.implode(", ", names)));
	}

}