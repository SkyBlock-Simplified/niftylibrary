package net.netcoding.niftybukkit.mojang;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.netcoding.niftybukkit.NiftyBukkit;
import net.netcoding.niftybukkit.yaml.Config;

class MojangProfileCache extends Config {

	private Map<String, String> users = new HashMap<>();

	MojangProfileCache() {
		super(NiftyBukkit.getPlugin(), "uuids.yml");
	}

	void add(MojangProfile profile) {
		this.users.put(profile.getUniqueId(), profile.getName());
	}

	public List<String> getUUIDs() {
		return new ArrayList<>(this.users.keySet());
	}

	public List<String> getNames() {
		return new ArrayList<>(this.users.keySet());
	}

	public String getUsername(String uuid) {
		return this.users.get(uuid);
	}

	public String getUUID(String username) {
		if (this.users.containsValue(username)) {
			for (String uuid : this.getUUIDs()) {
				if (this.users.get(uuid).equalsIgnoreCase(username))
					return uuid;
			}
		}

		return null;
	}

}