package net.netcoding.nifty.common.yaml;

import net.netcoding.nifty.common.yaml.converters.BlockConverter;
import net.netcoding.nifty.common.yaml.converters.ItemStackConverter;
import net.netcoding.nifty.common.yaml.converters.LocationConverter;
import net.netcoding.nifty.common.yaml.converters.VectorConverter;
import net.netcoding.nifty.common.yaml.converters.PotionEffectConverter;
import net.netcoding.nifty.core.util.concurrent.ConcurrentSet;
import net.netcoding.nifty.core.yaml.YamlConfig;
import net.netcoding.nifty.core.yaml.converters.Converter;

import java.io.File;

public abstract class BukkitConfig extends YamlConfig {

	protected static final transient ConcurrentSet<Class<? extends Converter>> GLOBAL_CUSTOM_CONVERTERS = new ConcurrentSet<>();
	private static transient boolean acceptingNewConverters = true;

	static {
		addGlobalCustomConverter(BlockConverter.class);
		// TODO: addGlobalCustomConverter(FakeLocationConverter.class);
		addGlobalCustomConverter(ItemStackConverter.class);
		addGlobalCustomConverter(LocationConverter.class);
		addGlobalCustomConverter(PotionEffectConverter.class);
		addGlobalCustomConverter(VectorConverter.class);
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