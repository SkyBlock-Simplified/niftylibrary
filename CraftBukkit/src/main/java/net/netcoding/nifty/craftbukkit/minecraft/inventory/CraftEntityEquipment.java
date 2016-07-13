package net.netcoding.nifty.craftbukkit.minecraft.inventory;

import net.netcoding.nifty.common.minecraft.entity.Entity;
import net.netcoding.nifty.common.minecraft.inventory.EntityEquipment;
import net.netcoding.nifty.common.minecraft.inventory.item.ItemStack;
import net.netcoding.nifty.craftbukkit.minecraft.entity.CraftEntity;
import net.netcoding.nifty.craftbukkit.minecraft.inventory.item.CraftItemStack;

import java.util.Arrays;

public final class CraftEntityEquipment implements EntityEquipment {

	private final org.bukkit.inventory.EntityEquipment entityEquipment;

	public CraftEntityEquipment(org.bukkit.inventory.EntityEquipment entityEquipment) {
		this.entityEquipment = entityEquipment;
	}

	@Override
	public void clear() {
		this.getHandle().clear();
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
	public float getBootsDropChance() {
		return this.getHandle().getBootsDropChance();
	}

	@Override
	public ItemStack getChestplate() {
		return new CraftItemStack(this.getHandle().getChestplate());
	}

	@Override
	public float getChestplateDropChance() {
		return this.getHandle().getChestplateDropChance();
	}

	public org.bukkit.inventory.EntityEquipment getHandle() {
		return this.entityEquipment;
	}

	@Override
	public ItemStack getHelmet() {
		return new CraftItemStack(this.getHandle().getHelmet());
	}

	@Override
	public float getHelmetDropChance() {
		return this.getHandle().getHelmetDropChance();
	}

	@Override
	public Entity getHolder() {
		return CraftEntity.convertBukkitEntity(this.getHandle().getHolder());
	}

	@Override
	public ItemStack getItemInHand() {
		return new CraftItemStack(this.getHandle().getItemInHand());
	}

	@Override
	public float getItemInHandDropChance() {
		return this.getHandle().getItemInHandDropChance();
	}

	@Override
	public ItemStack getItemInOffHand() {
		return new CraftItemStack(this.getHandle().getItemInOffHand());
	}

	@Override
	public float getItemInOffHandDropChance() {
		return this.getHandle().getItemInOffHandDropChance();
	}

	@Override
	public ItemStack getLeggings() {
		return new CraftItemStack(this.getHandle().getLeggings());
	}

	@Override
	public float getLeggingsDropChance() {
		return this.getHandle().getLeggingsDropChance();
	}

	@Override
	public void setArmorContents(ItemStack[] items) {
		this.getHandle().setArmorContents(Arrays.stream(items).map(item -> ((CraftItemStack)item).getHandle()).toArray(org.bukkit.inventory.ItemStack[]::new));
	}

	@Override
	public void setBoots(ItemStack item) {
		this.getHandle().setBoots(((CraftItemStack)item).getHandle());
	}

	@Override
	public void setBootsDropChance(float chance) {
		this.getHandle().setBootsDropChance(chance);
	}

	@Override
	public void setChestplate(ItemStack item) {
		this.getHandle().setChestplate(((CraftItemStack)item).getHandle());
	}

	@Override
	public void setChestplateDropChance(float chance) {
		this.getHandle().setChestplateDropChance(chance);
	}

	@Override
	public void setHelmet(ItemStack item) {
		this.getHandle().setHelmet(((CraftItemStack)item).getHandle());
	}

	@Override
	public void setHelmetDropChance(float chance) {
		this.getHandle().setHelmetDropChance(chance);
	}

	@Override
	public void setItemInHand(ItemStack item) {
		this.getHandle().setItemInHand(((CraftItemStack)item).getHandle());
	}

	@Override
	public void setItemInHandDropChance(float chance) {
		this.getHandle().setItemInHandDropChance(chance);
	}

	@Override
	public void setItemInOffHand(ItemStack item) {
		this.getHandle().setItemInOffHand(((CraftItemStack)item).getHandle());
	}

	@Override
	public void setItemInOffHandDropChance(float chance) {
		this.getHandle().setItemInOffHandDropChance(chance);
	}

	@Override
	public void setLeggings(ItemStack item) {
		this.getHandle().setLeggings(((CraftItemStack)item).getHandle());
	}

	@Override
	public void setLeggingsDropChance(float chance) {
		this.getHandle().setLeggingsDropChance(chance);
	}

}