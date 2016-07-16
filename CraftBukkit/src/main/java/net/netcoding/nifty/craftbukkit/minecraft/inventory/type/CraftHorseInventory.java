package net.netcoding.nifty.craftbukkit.minecraft.inventory.type;

import net.netcoding.nifty.common.minecraft.inventory.item.ItemStack;
import net.netcoding.nifty.common.minecraft.inventory.type.HorseInventory;
import net.netcoding.nifty.craftbukkit.minecraft.inventory.CraftInventory;
import net.netcoding.nifty.craftbukkit.minecraft.inventory.item.CraftItemStack;
import net.netcoding.nifty.craftbukkit.util.CraftConverter;

public final class CraftHorseInventory extends CraftInventory implements HorseInventory {

	public CraftHorseInventory(org.bukkit.inventory.HorseInventory horseInventory) {
		super(horseInventory);
	}

	@Override
	public ItemStack getArmor() {
		return new CraftItemStack(this.getHandle().getArmor());
	}

	@Override
	public org.bukkit.inventory.HorseInventory getHandle() {
		return (org.bukkit.inventory.HorseInventory)super.getHandle();
	}

	@Override
	public ItemStack getSaddle() {
		return new CraftItemStack(this.getHandle().getSaddle());
	}

	@Override
	public void setArmor(ItemStack item) {
		this.getHandle().setArmor(CraftConverter.toBukkitItem(item));
	}

	@Override
	public void setSaddle(ItemStack item) {
		this.getHandle().setSaddle(CraftConverter.toBukkitItem(item));
	}

}