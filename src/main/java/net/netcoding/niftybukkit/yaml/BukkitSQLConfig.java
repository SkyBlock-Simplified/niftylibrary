package net.netcoding.niftybukkit.yaml;

import net.netcoding.niftybukkit.yaml.converters.Block;
import net.netcoding.niftybukkit.yaml.converters.ItemStack;
import net.netcoding.niftybukkit.yaml.converters.Location;
import net.netcoding.niftybukkit.yaml.converters.PotionEffect;
import net.netcoding.niftycore.database.factory.SQLWrapper;
import net.netcoding.niftycore.yaml.SQLYamlConfig;

import java.io.File;

public abstract class BukkitSQLConfig<T extends SQLWrapper> extends SQLYamlConfig<T> {

	public BukkitSQLConfig(File folder, String fileName, String... header) {
		this(folder, fileName, false, header);
	}

	public BukkitSQLConfig(File folder, String fileName, boolean skipFailedConversion, String... header) {
		super(folder, fileName, skipFailedConversion, header);
		this.addCustomConverter(Block.class);
		this.addCustomConverter(ItemStack.class);
		this.addCustomConverter(Location.class);
		this.addCustomConverter(PotionEffect.class);
	}

}