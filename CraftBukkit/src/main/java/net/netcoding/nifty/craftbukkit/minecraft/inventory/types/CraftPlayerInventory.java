package net.netcoding.nifty.craftbukkit.minecraft.inventory.types;

import net.netcoding.nifty.common.minecraft.entity.living.human.HumanEntity;
import net.netcoding.nifty.common.minecraft.inventory.item.ItemStack;
import net.netcoding.nifty.common.minecraft.inventory.types.PlayerInventory;
import net.netcoding.nifty.craftbukkit.minecraft.inventory.CraftInventory;
import net.netcoding.nifty.craftbukkit.minecraft.inventory.item.CraftItemStack;

import java.util.Arrays;

@SuppressWarnings("deprecation")
public final class CraftPlayerInventory extends CraftInventory implements PlayerInventory {

	public CraftPlayerInventory(org.bukkit.inventory.PlayerInventory playerInventory) {
		super(playerInventory);
	}

	@Override
	public ItemStack[] getArmorContents() {
		return Arrays.stream(this.getHandle().getArmorContents()).map(CraftItemStack::new).toArray(CraftItemStack[]::new);
	}

	@Override
	public ItemStack getBoots() {
		return new CraftItemStack(this.getHandle().getBoots());
	}

	@Override
	public ItemStack getChestplate() {
		return new CraftItemStack(this.getHandle().getChestplate());
	}

	@Override
	public ItemStack[] getExtraContents() {
		return Arrays.stream(this.getHandle().getExtraContents()).map(CraftItemStack::new).toArray(CraftItemStack[]::new);
	}

	@Override
	public org.bukkit.inventory.PlayerInventory getHandle() {
		return (org.bukkit.inventory.PlayerInventory)super.getHandle();
	}

	@Override
	public int getHeldItemSlot() {
		return this.getHandle().getHeldItemSlot();
	}

	@Override
	public ItemStack getHelmet() {
		return new CraftItemStack(this.getHandle().getHelmet());
	}

	@Override
	public HumanEntity getHolder() {
		return (HumanEntity)super.getHolder();
	}

	@Override
	public ItemStack getItemInHand() {
		return new CraftItemStack(this.getHandle().getItemInHand());
	}

	@Override
	public ItemStack getItemInOffHand() {
		return new CraftItemStack(this.getHandle().getItemInOffHand());
	}

	@Override
	public ItemStack getLeggings() {
		return new CraftItemStack(this.getHandle().getLeggings());
	}

	@Override
	public void setArmorContents(ItemStack[] armorContents) {
		this.getHandle().setArmorContents(Arrays.stream(armorContents).map(item -> ((CraftItemStack)item).getHandle()).toArray(org.bukkit.inventory.ItemStack[]::new));
	}

	@Override
	public void setExtraContents(ItemStack[] extraContents) {
		this.getHandle().setExtraContents(Arrays.stream(extraContents).map(item -> ((CraftItemStack)item).getHandle()).toArray(org.bukkit.inventory.ItemStack[]::new));
	}

	@Override
	public void setBoots(ItemStack item) {
		this.getHandle().setBoots(((CraftItemStack)item).getHandle());
	}

	@Override
	public void setChestplate(ItemStack item) {
		this.getHandle().setChestplate(((CraftItemStack)item).getHandle());
	}

	@Override
	public void setHeldItemSlot(int slot) {
		this.getHandle().setHeldItemSlot(slot);
	}

	@Override
	public void setHelmet(ItemStack item) {
		this.getHandle().setHelmet(((CraftItemStack)item).getHandle());
	}

	@Override
	public void setItemInHand(ItemStack item) {
		this.getHandle().setItemInHand(((CraftItemStack)item).getHandle());
	}

	@Override
	public void setItemInOffHand(ItemStack item) {
		this.getHandle().setItemInOffHand(((CraftItemStack)item).getHandle());
	}

	@Override
	public void setLeggings(ItemStack item) {
		this.getHandle().setLeggings(((CraftItemStack)item).getHandle());
	}

}