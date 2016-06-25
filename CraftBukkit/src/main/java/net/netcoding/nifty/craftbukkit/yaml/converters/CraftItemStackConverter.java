package net.netcoding.nifty.craftbukkit.yaml.converters;

import net.netcoding.nifty.common.minecraft.inventory.item.ItemStack;
import net.netcoding.nifty.core.yaml.InternalConverter;
import net.netcoding.nifty.core.yaml.converters.Converter;

import java.lang.reflect.ParameterizedType;

@SuppressWarnings("unchecked")
public class CraftItemStackConverter extends Converter {

	public CraftItemStackConverter(InternalConverter converter) {
		super(converter);
	}

	@Override
	public Object fromConfig(Class<?> type, Object section, ParameterizedType genericType) throws Exception {
		return ((net.netcoding.nifty.craftbukkit.api.inventory.item.CraftItemStack)this.getConverter(ItemStack.class).fromConfig(type, section, genericType)).getBukkitItem();
	}

	@Override
	public Object toConfig(Class<?> type, Object obj, ParameterizedType genericType) throws Exception {
		net.netcoding.nifty.craftbukkit.api.inventory.item.CraftItemStack item = new net.netcoding.nifty.craftbukkit.api.inventory.item.CraftItemStack((org.bukkit.inventory.ItemStack)obj);
		return this.getConverter(ItemStack.class).toConfig(type, item, genericType);
	}

	@Override
	public boolean supports(Class<?> type) {
		return org.bukkit.inventory.ItemStack.class.isAssignableFrom(type);
	}

}