package net.netcoding.niftybukkit.mojang;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.netcoding.niftybukkit.NiftyBukkit;
import net.netcoding.niftybukkit.yaml.Config;

import org.bukkit.Bukkit;

class MojangProfileCache extends Config {

	@SuppressWarnings("unused")
	private org.bukkit.block.Block block = Bukkit.getServer().getWorld("world").getBlockAt(3, 72, -107);

	private Map<String, List<String>> users = new HashMap<>();

	MojangProfileCache() {
		super(NiftyBukkit.getPlugin(), "uuids.yml");
	}

	void add(MojangProfile profile) {
		if (this.users.get(profile.getUniqueId()) == null) this.users.put(profile.getUniqueId(), new ArrayList<String>());
		this.users.get(profile.getUniqueId()).add(profile.getName());
	}

	public List<String> getUUIDs() {
		return new ArrayList<>(this.users.keySet());
	}

	public List<String> getNames(String uuid) {
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