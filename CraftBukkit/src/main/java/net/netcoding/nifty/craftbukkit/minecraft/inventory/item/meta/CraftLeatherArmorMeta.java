package net.netcoding.nifty.craftbukkit.minecraft.inventory.item.meta;

import net.netcoding.nifty.common.minecraft.inventory.item.meta.LeatherArmorMeta;
import net.netcoding.nifty.core.api.color.Color;

public final class CraftLeatherArmorMeta extends CraftItemMeta implements LeatherArmorMeta {

	public CraftLeatherArmorMeta(org.bukkit.inventory.meta.LeatherArmorMeta leatherArmorMeta) {
		super(leatherArmorMeta);
	}

	@Override
	public LeatherArmorMeta clone() {
		return new CraftLeatherArmorMeta(this.getHandle().clone());
	}

	@Override
	public Color getColor() {
		return Color.fromRGB(this.getHandle().getColor().asRGB());
	}

	@Override
	public org.bukkit.inventory.meta.LeatherArmorMeta getHandle() {
		return (org.bukkit.inventory.meta.LeatherArmorMeta)super.getHandle();
	}


	@Override
	public void setColor(Color color) {
		this.getHandle().setColor(org.bukkit.Color.fromRGB(color.asRGB()));
	}

}