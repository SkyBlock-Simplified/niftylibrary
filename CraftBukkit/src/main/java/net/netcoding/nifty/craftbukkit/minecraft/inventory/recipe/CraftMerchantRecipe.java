package net.netcoding.nifty.craftbukkit.minecraft.inventory.recipe;

import net.netcoding.nifty.common.minecraft.inventory.item.ItemStack;
import net.netcoding.nifty.common.minecraft.inventory.recipe.MerchantRecipe;
import net.netcoding.nifty.core.util.concurrent.Concurrent;
import net.netcoding.nifty.craftbukkit.minecraft.inventory.item.CraftItemStack;
import net.netcoding.nifty.craftbukkit.util.CraftConverter;

import java.util.List;

public final class CraftMerchantRecipe extends CraftRecipe implements MerchantRecipe {

	public CraftMerchantRecipe(org.bukkit.inventory.MerchantRecipe merchantRecipe) {
		super(merchantRecipe);
	}

	@Override
	public void addIngredient(ItemStack item) {
		this.getHandle().addIngredient(CraftConverter.toBukkitItem(item));
	}

	@Override
	public org.bukkit.inventory.MerchantRecipe getHandle() {
		return (org.bukkit.inventory.MerchantRecipe)super.getHandle();
	}

	@Override
	public List<ItemStack> getIngredients() {
		return this.getHandle().getIngredients().stream().map(CraftItemStack::new).collect(Concurrent.toList());
	}

	@Override
	public int getMaxUses() {
		return this.getHandle().getMaxUses();
	}

	@Override
	public int getUses() {
		return this.getHandle().getUses();
	}

	@Override
	public boolean hasExperienceReward() {
		return this.getHandle().hasExperienceReward();
	}

	@Override
	public void removeIngredient(int index) {
		this.getHandle().removeIngredient(index);
	}

	@Override
	public void setExperienceReward(boolean flag) {
		this.getHandle().setExperienceReward(flag);
	}

	@Override
	public void setIngredients(List<ItemStack> ingredients) {
		this.getHandle().setIngredients(ingredients.stream().map(CraftConverter::toBukkitItem).collect(Concurrent.toList()));
	}

	@Override
	public void setMaxUses(int maxUses) {
		this.getHandle().setMaxUses(maxUses);
	}

	@Override
	public void setUses(int uses) {
		this.getHandle().setUses(uses);
	}

}