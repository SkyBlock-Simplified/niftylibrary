package net.netcoding.nifty.craftbukkit.minecraft.entity;

import net.netcoding.nifty.common.minecraft.entity.ExperienceOrb;

public final class CraftExperienceOrb extends CraftEntity implements ExperienceOrb {

	public CraftExperienceOrb(org.bukkit.entity.ExperienceOrb experienceOrb) {
		super(experienceOrb);
	}

	@Override
	public int getExperience() {
		return this.getHandle().getExperience();
	}

	@Override
	public org.bukkit.entity.ExperienceOrb getHandle() {
		return (org.bukkit.entity.ExperienceOrb)super.getHandle();
	}

	@Override
	public void setExperience(int experience) {
		this.getHandle().setExperience(experience);
	}

}