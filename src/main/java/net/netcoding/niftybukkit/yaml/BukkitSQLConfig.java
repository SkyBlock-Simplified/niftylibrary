package net.netcoding.niftybukkit.yaml;

import net.netcoding.niftybukkit.yaml.converters.Block;
import net.netcoding.niftybukkit.yaml.converters.ItemStack;
import net.netcoding.niftybukkit.yaml.converters.Location;
import net.netcoding.niftycore.database.factory.SQLWrapper;
import net.netcoding.niftycore.yaml.SQLConfig;

import java.io.File;

public class BukkitSQLConfig extends SQLConfig<SQLWrapper> {

	public BukkitSQLConfig(File folder, String fileName, String... header) {
		this(folder, fileName, false, header);
	}

	public BukkitSQLConfig(File folder, String fileName, boolean skipFailedConversion, String... header) {
		super(folder, fileName, skipFailedConversion, header);
		this.addCustomConverter(Block.class);
		this.addCustomConverter(ItemStack.class);
		this.addCustomConverter(Location.class);
	}

}