package net.netcoding.niftybukkit.mojang;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import net.netcoding.niftybukkit.NiftyBukkit;
import net.netcoding.niftybukkit.yaml.Config;

class MojangProfileCache extends Config {

	private Map<String, Set<String>> users = new HashMap<>();

	MojangProfileCache() {
		super(NiftyBukkit.getPlugin(), "uuids.yml");
	}

	void add(MojangProfile profile) {
		if (this.users.get(profile.getUniqueId()) == null) this.users.put(profile.getUniqueId(), new HashSet<String>());
		this.users.get(profile.getUniqueId()).add(profile.getName());
	}

	public Set<String> getUUIDs() {
		return this.users.keySet();
	}

	public Set<String> getNames(String uuid) {
		return this.users.get(uuid);
	}

	public String findUUID(String username) {
		for (String uuid : this.getUUIDs()) {
			for (String search : this.getNames(uuid)) {
				if (search.equals(username))
					return uuid;
			}
		}

		return null;
	}

}