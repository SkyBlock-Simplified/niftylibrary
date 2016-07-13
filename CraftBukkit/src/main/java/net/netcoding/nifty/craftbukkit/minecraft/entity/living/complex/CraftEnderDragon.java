package net.netcoding.nifty.craftbukkit.minecraft.entity.living.complex;

import net.netcoding.nifty.common.minecraft.entity.living.complex.EnderDragon;

public final class CraftEnderDragon extends CraftComplexLivingEntity implements EnderDragon {

	public CraftEnderDragon(org.bukkit.entity.EnderDragon enderDragon) {
		super(enderDragon);
	}

	@Override
	public org.bukkit.entity.EnderDragon getHandle() {
		return (org.bukkit.entity.EnderDragon)super.getHandle();
	}

	@Override
	public Phase getPhase() {
		return Phase.valueOf(this.getHandle().getPhase().name());
	}

	@Override
	public void setPhase(Phase phase) {
		this.getHandle().setPhase(org.bukkit.entity.EnderDragon.Phase.valueOf(phase.name()));
	}

}