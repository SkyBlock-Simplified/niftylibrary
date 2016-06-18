package net.netcoding.niftybukkit._new_.yaml;

import net.netcoding.niftycore.database.factory.SQLWrapper;
import net.netcoding.niftycore.yaml.SQLYamlConfig;

import java.io.File;

public abstract class BukkitSQLConfig<T extends SQLWrapper> extends SQLYamlConfig<T> {

	public BukkitSQLConfig(File folder, String fileName, String... header) {
		this(folder, fileName, false, header);
	}

	public BukkitSQLConfig(File folder, String fileName, boolean skipFailedConversion, String... header) {
		super(folder, fileName, skipFailedConversion, header);
		BukkitConfig.GLOBAL_CUSTOM_CONVERTERS.forEach(this::addCustomConverter);
	}

}