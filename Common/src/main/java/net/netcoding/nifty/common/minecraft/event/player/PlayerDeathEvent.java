package net.netcoding.nifty.common.minecraft.event.player;

import net.netcoding.nifty.common.minecraft.entity.living.human.Player;
import net.netcoding.nifty.common.minecraft.event.entity.EntityDeathEvent;

public interface PlayerDeathEvent extends EntityDeathEvent {

	String getDeathMessage();

	@Override
	Player getEntity();

	int getNewExperience();

	int getNewLevel();

	int getNewTotalExperience();

	boolean isKeepingInventory();

	boolean isKeepingLevel();

	void setDeathMessage(String deathMessage);

	void setKeepInventory(boolean keepInventory);

	void setKeepLevel(boolean keepLevel);

	void setNewExperience(int experience);

	void setNewLevel(int level);

	void setNewTotalExperience(int totalExp);

}