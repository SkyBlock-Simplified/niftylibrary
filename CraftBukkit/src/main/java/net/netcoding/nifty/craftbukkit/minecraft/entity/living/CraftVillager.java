package net.netcoding.nifty.craftbukkit.minecraft.entity.living;

import net.netcoding.nifty.common.minecraft.entity.living.Villager;
import net.netcoding.nifty.common.minecraft.entity.living.human.HumanEntity;
import net.netcoding.nifty.common.minecraft.inventory.Inventory;
import net.netcoding.nifty.common.minecraft.inventory.recipe.MerchantRecipe;
import net.netcoding.nifty.core.util.concurrent.Concurrent;
import net.netcoding.nifty.craftbukkit.minecraft.entity.CraftEntity;
import net.netcoding.nifty.craftbukkit.minecraft.inventory.CraftInventory;
import net.netcoding.nifty.craftbukkit.minecraft.inventory.recipe.CraftMerchantRecipe;
import net.netcoding.nifty.craftbukkit.util.CraftConverter;

import java.util.List;

public class CraftVillager extends CraftAgeable implements Villager {

	public CraftVillager(org.bukkit.entity.Villager villager) {
		super(villager);
	}

	@Override
	public org.bukkit.entity.Villager getHandle() {
		return (org.bukkit.entity.Villager)super.getHandle();
	}

	@Override
	public Inventory getInventory() {
		return CraftInventory.convertBukkitInventory(this.getHandle().getInventory());
	}

	@Override
	public Profession getProfession() {
		return Profession.valueOf(this.getHandle().getProfession().name());
	}

	@Override
	public List<MerchantRecipe> getRecipes() {
		return this.getHandle().getRecipes().stream().map(CraftMerchantRecipe::new).collect(Concurrent.toList());
	}

	@Override
	public int getRiches() {
		return this.getHandle().getRiches();
	}

	@Override
	public HumanEntity getTrader() {
		return CraftEntity.convertBukkitEntity(this.getHandle().getTrader(), HumanEntity.class);
	}

	@Override
	public boolean isTrading() {
		return this.getHandle().isTrading();
	}

	@Override
	public void setProfession(Profession profession) {
		this.getHandle().setProfession(org.bukkit.entity.Villager.Profession.valueOf(profession.name()));
	}

	@Override
	public void setRecipe(int index, MerchantRecipe recipe) {
		this.getHandle().setRecipe(index, CraftConverter.toBukkitRecipe(recipe));
	}

	@Override
	public void setRecipes(List<MerchantRecipe> recipes) {
		this.getHandle().setRecipes(recipes.stream().map(CraftConverter::toBukkitRecipe).collect(Concurrent.toList()));
	}

	@Override
	public void setRiches(int riches) {
		this.getHandle().setRiches(riches);
	}

}