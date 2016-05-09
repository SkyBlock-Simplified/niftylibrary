package net.netcoding.niftybukkit.yaml;

import net.netcoding.niftybukkit.reflection.MinecraftProtocol;
import net.netcoding.niftybukkit.yaml.converters.Block;
import net.netcoding.niftybukkit.yaml.converters.ItemFlag;
import net.netcoding.niftybukkit.yaml.converters.ItemStack;
import net.netcoding.niftybukkit.yaml.converters.Location;
import net.netcoding.niftycore.yaml.YamlConfig;

import java.io.File;

public class BukkitConfig extends YamlConfig {

	public BukkitConfig(File folder, String fileName, String... header) {
		this(folder, fileName, false, header);
	}

	public BukkitConfig(File folder, String fileName, boolean skipFailedConversion, String... header) {
		super(folder, fileName, skipFailedConversion, header);
		this.addCustomConverter(Block.class);
		this.addCustomConverter(ItemStack.class);
		this.addCustomConverter(Location.class);

		if (MinecraftProtocol.getCurrentProtocol() >= MinecraftProtocol.v1_8_pre1.getProtocol())
			this.addCustomConverter(ItemFlag.class);
	}

}