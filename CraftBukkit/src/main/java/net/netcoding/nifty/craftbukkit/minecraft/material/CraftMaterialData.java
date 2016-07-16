package net.netcoding.nifty.craftbukkit.minecraft.material;

import net.netcoding.nifty.common.minecraft.material.Material;
import net.netcoding.nifty.common.minecraft.material.MaterialData;
import net.netcoding.nifty.core.reflection.Reflection;
import net.netcoding.nifty.craftbukkit.util.CraftConverter;

import java.util.Arrays;

@SuppressWarnings("deprecation")
public class CraftMaterialData implements MaterialData {

	private final org.bukkit.material.MaterialData materialData;

	public CraftMaterialData(org.bukkit.material.MaterialData materialData) {
		this.materialData = materialData;

		Arrays.stream(Material.values())
				.filter(material -> material.getData().isAssignableFrom(this.getClass()))
				.forEach(material ->new Reflection(material.getClass()).setValue("ctor", material, this.getClass()));
	}

	@Override
	public MaterialData clone() {
		return CraftConverter.fromBukkitData(this.getHandle().clone());
	}

	@Override
	public byte getData() {
		return this.getHandle().getData();
	}

	public org.bukkit.material.MaterialData getHandle() {
		return this.materialData;
	}

	@Override
	public Material getItemType() {
		return Material.valueOf(this.getHandle().getItemType().name());
	}

	@Override
	public void setData(byte data) {
		this.getHandle().setData(data);
	}

}