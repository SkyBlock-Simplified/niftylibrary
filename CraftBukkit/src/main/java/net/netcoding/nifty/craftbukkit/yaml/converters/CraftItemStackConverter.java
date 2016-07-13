package net.netcoding.nifty.craftbukkit.yaml.converters;

import net.netcoding.nifty.common.minecraft.inventory.item.ItemStack;
import net.netcoding.nifty.core.yaml.InternalConverter;
import net.netcoding.nifty.core.yaml.converters.Converter;
import net.netcoding.nifty.craftbukkit.minecraft.inventory.item.CraftItemStack;

import java.lang.reflect.ParameterizedType;

@SuppressWarnings("unchecked")
public final class CraftItemStackConverter extends Converter {

	public CraftItemStackConverter(InternalConverter converter) {
		super(converter);
	}

	@Override
	public Object fromConfig(Class<?> type, Object section, ParameterizedType genericType) throws Exception {
		return ((CraftItemStack)this.getConverter(ItemStack.class).fromConfig(type, section, genericType)).getHandle();
	}

	@Override
	public Object toConfig(Class<?> type, Object obj, ParameterizedType genericType) throws Exception {
		CraftItemStack item = new CraftItemStack((org.bukkit.inventory.ItemStack)obj);
		return this.getConverter(ItemStack.class).toConfig(type, item, genericType);
	}

	@Override
	public boolean supports(Class<?> type) {
		return org.bukkit.inventory.ItemStack.class.isAssignableFrom(type);
	}

}