package net.netcoding.niftybukkit.mojang;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

public class MojangProfile {

	private static final transient Pattern UUID_PATTERN = Pattern.compile("([0-9a-fA-F]{8})([0-9a-fA-F]{4})([0-9a-fA-F]{4})([0-9a-fA-F]{4})([0-9a-fA-F]+)");
	private String id;
	private String name;
	private final List<String> names = new ArrayList<>();

	MojangProfile(String name, String id) {
		this.name = name;
		this.id = id;
	}

	public String getUniqueId() {
		return this.getUniqueId(false);
	}

	public String getUniqueId(boolean hash) {
		return hash ? this.id : UUID_PATTERN.matcher(this.id).replaceFirst("$1-$2-$3-$4-$5");
	}

	public String getName() {
		return this.name;
	}

	public List<String> getNames() {
		return this.names;
	}

	void setNames(List<String> names) {
		this.names.addAll(names);
	}

}