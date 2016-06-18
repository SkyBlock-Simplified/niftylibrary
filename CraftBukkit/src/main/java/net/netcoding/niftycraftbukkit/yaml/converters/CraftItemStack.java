package net.netcoding.niftycraftbukkit.yaml.converters;

import net.netcoding.niftybukkit._new_.minecraft.inventory.item.ItemStack;
import net.netcoding.niftycore.yaml.InternalConverter;
import net.netcoding.niftycore.yaml.converters.Converter;

import java.lang.reflect.ParameterizedType;

@SuppressWarnings("unchecked")
public class CraftItemStack extends Converter {

	public CraftItemStack(InternalConverter converter) {
		super(converter);
	}

	@Override
	public Object fromConfig(Class<?> type, Object section, ParameterizedType genericType) throws Exception {
		return ((net.netcoding.niftycraftbukkit.api.inventory.item.CraftItemStack)this.getConverter(ItemStack.class).fromConfig(type, section, genericType)).getBukkitItem();
	}

	@Override
	public Object toConfig(Class<?> type, Object obj, ParameterizedType genericType) throws Exception {
		net.netcoding.niftycraftbukkit.api.inventory.item.CraftItemStack item = new net.netcoding.niftycraftbukkit.api.inventory.item.CraftItemStack((org.bukkit.inventory.ItemStack)obj);
		return this.getConverter(ItemStack.class).toConfig(type, item, genericType);
	}

	@Override
	public boolean supports(Class<?> type) {
		return org.bukkit.inventory.ItemStack.class.isAssignableFrom(type);
	}

}