package net.netcoding.nifty.common._new_.minecraft.event.profile;

import net.netcoding.nifty.common._new_.mojang.BukkitMojangProfile;

public class ProfileQuitEvent extends ProfileEvent {

	private String message;

	public ProfileQuitEvent(BukkitMojangProfile profile, String message) {
		super(profile);
		this.message = message;
	}

	public String getQuitMessage() {
		return this.message;
	}

	public void setQuitMessage(String message) {
		this.message = message;
	}

}