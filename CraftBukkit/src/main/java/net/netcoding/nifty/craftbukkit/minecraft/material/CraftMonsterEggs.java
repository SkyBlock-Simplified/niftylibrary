package net.netcoding.nifty.craftbukkit.minecraft.material;

import net.netcoding.nifty.common.minecraft.material.MonsterEggs;

public final class CraftMonsterEggs extends CraftMaterialData implements MonsterEggs {

	public CraftMonsterEggs(org.bukkit.material.MonsterEggs monsterEggs) {
		super(monsterEggs);
	}

	@Override
	public MonsterEggs clone() {
		return (MonsterEggs)super.clone();
	}

}