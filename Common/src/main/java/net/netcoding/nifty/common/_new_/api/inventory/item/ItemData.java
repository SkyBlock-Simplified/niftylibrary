package net.netcoding.nifty.common._new_.api.inventory.item;

import net.netcoding.nifty.core.util.comparator.LengthCompare;
import net.netcoding.nifty.core.util.concurrent.ConcurrentList;
import net.netcoding.nifty.core.util.concurrent.ConcurrentSet;

import java.util.Collections;
import java.util.List;
import java.util.Set;

class ItemData {

	private static final LengthCompare LENGTH_COMPARE = new LengthCompare();
	private final ConcurrentSet<Short> durabilities = new ConcurrentSet<>();
	private final ConcurrentList<String> names = new ConcurrentList<>();
	private final String primaryName;
	private final int id;

	public ItemData(int id, String primaryName) {
		this.id = id;
		this.primaryName = primaryName;
		this.names.add(primaryName);
	}

	public void addDurability(short durability) {
		this.durabilities.add(durability);
	}

	public void addName(String name) {
		this.names.add(name);
		Collections.sort(this.names, LENGTH_COMPARE);
	}

	public Set<Short> getDurabilities() {
		return this.durabilities;
	}

	public final int getId() {
		return this.id;
	}

	public List<String> getNames() {
		return this.names;
	}

	public String getPrimaryName() {
		return this.primaryName;
	}

}