package net.netcoding.niftybukkit._new_.api.inventory.item;

import net.netcoding.niftybukkit.Nifty;
import net.netcoding.niftycore.util.misc.CSVStorage;

public final class MiniBlockDatabase extends CSVStorage {

	// https://www.spigotmc.org/resources/mini-blocks-library.9155/
	private static MiniBlockDatabase INSTANCE;

	private MiniBlockDatabase() {
		super(Nifty.getPlugin().getPluginDescription().getStorage(), "miniblocks");
	}

	public static MiniBlockDatabase getInstance() {
		if (INSTANCE == null)
			INSTANCE = new MiniBlockDatabase();

		return INSTANCE;
	}

	@Override
	public void reload() {
		// TODO
	}

}