package net.netcoding.nifty.common._new_.minecraft.event.entity;

import net.netcoding.nifty.common._new_.minecraft.entity.living.LivingEntity;
import net.netcoding.nifty.common._new_.minecraft.inventory.item.ItemStack;

import java.util.List;

public interface EntityDeathEvent extends EntityEvent {

	@Override
	LivingEntity getEntity();

	int getDroppedExperience();

	List<ItemStack> getDrops();

	void setDroppedExperience(int experience);

}