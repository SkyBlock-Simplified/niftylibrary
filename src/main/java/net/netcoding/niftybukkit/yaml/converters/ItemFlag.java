package net.netcoding.niftybukkit.yaml.converters;

import net.netcoding.niftycore.yaml.InternalConverter;
import net.netcoding.niftycore.yaml.converters.Converter;

import java.lang.reflect.ParameterizedType;

public class ItemFlag extends Converter {

	public ItemFlag(InternalConverter converter) {
		super(converter);
	}

	@Override
	public Object fromConfig(Class<?> type, Object obj, ParameterizedType genericType) throws Exception {
		return org.bukkit.inventory.ItemFlag.valueOf(String.valueOf(obj));
	}

	@Override
	public Object toConfig(Class<?> type, Object obj, ParameterizedType genericType) throws Exception {
		return ((org.bukkit.inventory.ItemFlag)obj).name();
	}

	@Override
	public boolean supports(Class<?> type) {
		return org.bukkit.inventory.ItemFlag.class.isAssignableFrom(type);
	}

}