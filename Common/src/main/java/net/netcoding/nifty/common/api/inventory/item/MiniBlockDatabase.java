package net.netcoding.nifty.common.api.inventory.item;

import net.netcoding.nifty.common.Nifty;
import net.netcoding.nifty.core.util.misc.CSVStorage;

public final class MiniBlockDatabase extends CSVStorage {

	// https://www.spigotmc.org/resources/mini-blocks-library.9155/
	private static final MiniBlockDatabase INSTANCE = new MiniBlockDatabase();

	private MiniBlockDatabase() {
		super(Nifty.getPlugin().getDesc().getDataFolder(), "miniblocks");
	}

	public static MiniBlockDatabase getInstance() {
		return INSTANCE;
	}

	@Override
	public void reload() {
		// TODO
	}

}