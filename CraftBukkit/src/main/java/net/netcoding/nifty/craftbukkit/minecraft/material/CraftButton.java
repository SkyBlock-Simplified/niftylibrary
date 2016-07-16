package net.netcoding.nifty.craftbukkit.minecraft.material;

import net.netcoding.nifty.common.minecraft.material.Button;

public final class CraftButton extends CraftMaterialData implements Button {

	public CraftButton(org.bukkit.material.Button button) {
		super(button);
	}

	@Override
	public Button clone() {
		return (Button)super.clone();
	}

}