package net.netcoding.nifty.craftbukkit.minecraft.entity.projectile.potion;

import net.netcoding.nifty.common.minecraft.entity.projectile.potion.LingeringPotion;

public final class CraftLingeringPotion extends CraftThrownPotion implements LingeringPotion {

	public CraftLingeringPotion(org.bukkit.entity.LingeringPotion lingeringPotion) {
		super(lingeringPotion);
	}

	@Override
	public org.bukkit.entity.LingeringPotion getHandle() {
		return (org.bukkit.entity.LingeringPotion)super.getHandle();
	}

}