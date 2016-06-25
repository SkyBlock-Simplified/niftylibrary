package net.netcoding.nifty.common.minecraft.event.entity;

import net.netcoding.nifty.common.minecraft.entity.living.LivingEntity;
import net.netcoding.nifty.common.minecraft.inventory.item.ItemStack;

import java.util.List;

public interface EntityDeathEvent extends EntityEvent {

	@Override
	LivingEntity getEntity();

	int getDroppedExperience();

	List<ItemStack> getDrops();

	void setDroppedExperience(int experience);

}