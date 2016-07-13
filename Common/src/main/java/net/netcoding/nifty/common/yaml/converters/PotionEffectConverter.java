package net.netcoding.nifty.common.yaml.converters;

import net.netcoding.nifty.common.minecraft.potion.PotionEffect;
import net.netcoding.nifty.common.minecraft.potion.PotionEffectType;
import net.netcoding.nifty.core.api.color.Color;
import net.netcoding.nifty.core.util.NumberUtil;
import net.netcoding.nifty.core.yaml.InternalConverter;
import net.netcoding.nifty.core.yaml.converters.Converter;

import java.lang.reflect.ParameterizedType;
import java.util.HashMap;
import java.util.Map;

@SuppressWarnings("unchecked")
public class PotionEffectConverter extends Converter {

	public PotionEffectConverter(InternalConverter converter) {
		super(converter);
	}

	@Override
	public Object fromConfig(Class<?> type, Object section, ParameterizedType genericType) throws Exception {
		Map<String, Object> map = (Map<String, Object>)this.getConverter(Map.class).fromConfig(HashMap.class, section, null);
		String name = (String)map.get("name");
		int duration = NumberUtil.isNumber((String)map.get("duration")) ? (Integer)map.get("duration") : 1;
		int amplifier = NumberUtil.isNumber((String)map.get("amplifier")) ? (Integer)map.get("amplifier") : 0;
		boolean ambient = map.containsKey("ambient") ? (Boolean)map.get("ambient") : false;
		boolean particles = map.containsKey("particles") ? (Boolean)map.get("particles") : false;
		Color color = map.containsKey("color") ? Color.fromRGB(NumberUtil.toLong((String)map.get("color")).intValue()) : null;
		return new PotionEffect(PotionEffectType.getByName(name), duration, amplifier, ambient, particles, color);
	}

	@Override
	public Object toConfig(Class<?> type, Object obj, ParameterizedType genericType) throws Exception {
		PotionEffect potion = (PotionEffect)obj;
		Map<String, Object> saveMap = new HashMap<>();
		saveMap.put("name", potion.getType().getName());
		saveMap.put("duration", potion.getDuration());
		saveMap.put("amplifier", potion.getAmplifier());
		saveMap.put("ambient", potion.isAmbient());
		saveMap.put("particles", potion.hasParticles());
		saveMap.put("color", NumberUtil.toHexString(potion.getColor().asRGB()));
		return saveMap;
	}

	@Override
	public boolean supports(Class<?> type) {
		return net.netcoding.nifty.common.minecraft.potion.PotionEffect.class.isAssignableFrom(type);
	}

}