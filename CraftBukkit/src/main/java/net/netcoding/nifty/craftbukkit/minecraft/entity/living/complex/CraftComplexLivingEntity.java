package net.netcoding.nifty.craftbukkit.minecraft.entity.living.complex;

import net.netcoding.nifty.common.minecraft.entity.living.complex.ComplexEntityPart;
import net.netcoding.nifty.common.minecraft.entity.living.complex.ComplexLivingEntity;
import net.netcoding.nifty.core.util.concurrent.Concurrent;
import net.netcoding.nifty.craftbukkit.minecraft.entity.CraftEntity;
import net.netcoding.nifty.craftbukkit.minecraft.entity.living.CraftLivingEntity;

import java.util.Set;

public class CraftComplexLivingEntity extends CraftLivingEntity implements ComplexLivingEntity {

	public CraftComplexLivingEntity(org.bukkit.entity.ComplexLivingEntity complexLivingEntity) {
		super(complexLivingEntity);
	}

	@Override
	public org.bukkit.entity.ComplexLivingEntity getHandle() {
		return (org.bukkit.entity.ComplexLivingEntity)super.getHandle();
	}

	@Override
	public Set<ComplexEntityPart> getParts() {
		return this.getHandle().getParts().stream().map(part -> CraftEntity.convertBukkitEntity(part, ComplexEntityPart.class)).collect(Concurrent.toSet());
	}

}