package net.netcoding.nifty.craftbukkit.minecraft.entity.living.complex;

import net.netcoding.nifty.common.minecraft.entity.living.complex.ComplexEntityPart;
import net.netcoding.nifty.common.minecraft.entity.living.complex.ComplexLivingEntity;
import net.netcoding.nifty.craftbukkit.minecraft.entity.CraftEntity;

public class CraftComplexEntityPart extends CraftEntity implements ComplexEntityPart {

	public CraftComplexEntityPart(org.bukkit.entity.ComplexEntityPart complexEntityPart) {
		super(complexEntityPart);
	}

	@Override
	public org.bukkit.entity.ComplexEntityPart getHandle() {
		return (org.bukkit.entity.ComplexEntityPart)super.getHandle();
	}

	@Override
	public ComplexLivingEntity getParent() {
		return CraftEntity.convertBukkitEntity(this.getHandle().getParent(), ComplexLivingEntity.class);
	}

}