package net.netcoding.nifty.common._new_.yaml;

import net.netcoding.nifty.common._new_.yaml.converters.Block;
import net.netcoding.nifty.common._new_.yaml.converters.ItemStack;
import net.netcoding.nifty.common._new_.yaml.converters.Location;
import net.netcoding.nifty.common._new_.yaml.converters.Vector;
import net.netcoding.nifty.common.yaml.converters.PotionEffect;
import net.netcoding.niftycore.util.concurrent.ConcurrentSet;
import net.netcoding.niftycore.yaml.YamlConfig;
import net.netcoding.niftycore.yaml.converters.Converter;

import java.io.File;

public abstract class BukkitConfig extends YamlConfig {

	protected static final transient ConcurrentSet<Class<? extends Converter>> GLOBAL_CUSTOM_CONVERTERS = new ConcurrentSet<>();
	private static transient boolean acceptingNewConverters = true;

	static {
		addGlobalCustomConverter(Block.class);
		addGlobalCustomConverter(ItemStack.class);
		addGlobalCustomConverter(Location.class);
		addGlobalCustomConverter(PotionEffect.class);
		addGlobalCustomConverter(Vector.class);
	}

	public BukkitConfig(File folder, String fileName, String... header) {
		this(folder, fileName, false, header);
	}

	public BukkitConfig(File folder, String fileName, boolean skipFailedConversion, String... header) {
		super(folder, fileName, skipFailedConversion, header);
		GLOBAL_CUSTOM_CONVERTERS.forEach(this::addCustomConverter);
	}

	public static void addGlobalCustomConverter(Class<? extends Converter> converter) {
		if (!acceptingNewConverters)
			throw new UnsupportedOperationException("No longer accepting custom converters!");

		GLOBAL_CUSTOM_CONVERTERS.add(converter);
	}

	public static void stopAcceptingGlobalConverters() {
		acceptingNewConverters = false;
	}

}