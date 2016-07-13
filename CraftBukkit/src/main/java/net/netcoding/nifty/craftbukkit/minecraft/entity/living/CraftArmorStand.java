package net.netcoding.nifty.craftbukkit.minecraft.entity.living;

import net.netcoding.nifty.common.minecraft.entity.living.ArmorStand;
import net.netcoding.nifty.common.minecraft.inventory.item.ItemStack;
import net.netcoding.nifty.core.util.misc.Vector;
import net.netcoding.nifty.craftbukkit.minecraft.inventory.item.CraftItemStack;
import net.netcoding.nifty.craftbukkit.util.CraftConverter;

public final class CraftArmorStand extends CraftLivingEntity implements ArmorStand {

	public CraftArmorStand(org.bukkit.entity.ArmorStand armorStand) {
		super(armorStand);
	}

	@Override
	public org.bukkit.entity.ArmorStand getHandle() {
		return (org.bukkit.entity.ArmorStand)super.getHandle();
	}

	@Override
	public Vector getBodyPose() {
		return CraftConverter.fromBukkitEuler(this.getHandle().getBodyPose());
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
	public Vector getHeadPose() {
		return CraftConverter.fromBukkitEuler(this.getHandle().getHeadPose());
	}

	@Override
	public ItemStack getHelmet() {
		return new CraftItemStack(this.getHandle().getHelmet());
	}

	@Override
	public ItemStack getItemInHand() {
		return new CraftItemStack(this.getHandle().getItemInHand());
	}

	@Override
	public Vector getLeftArmPose() {
		return CraftConverter.fromBukkitEuler(this.getHandle().getLeftArmPose());
	}

	@Override
	public Vector getLeftLegPose() {
		return CraftConverter.fromBukkitEuler(this.getHandle().getLeftLegPose());
	}

	@Override
	public ItemStack getLeggings() {
		return new CraftItemStack(this.getHandle().getLeggings());
	}

	@Override
	public Vector getRightArmPose() {
		return CraftConverter.fromBukkitEuler(this.getHandle().getRightArmPose());
	}

	@Override
	public Vector getRightLegPose() {
		return CraftConverter.fromBukkitEuler(this.getHandle().getRightLegPose());
	}

	@Override
	public boolean hasArms() {
		return this.getHandle().hasArms();
	}

	@Override
	public boolean hasBasePlate() {
		return this.getHandle().hasBasePlate();
	}

	@Override
	public boolean isMarker() {
		return this.getHandle().isMarker();
	}

	@Override
	public boolean isSmall() {
		return this.getHandle().isSmall();
	}

	@Override
	public boolean isVisible() {
		return this.getHandle().isVisible();
	}

	@Override
	public void setArms(boolean value) {
		this.getHandle().setArms(value);
	}

	@Override
	public void setBodyPose(Vector pose) {
		this.getHandle().setBodyPose(CraftConverter.toBukkitEuler(pose));
	}

	@Override
	public void setBasePlate(boolean value) {
		this.getHandle().setBasePlate(value);
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
	public void setHeadPose(Vector pose) {
		this.getHandle().setHeadPose(CraftConverter.toBukkitEuler(pose));
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
	public void setLeftArmPose(Vector pose) {
		this.getHandle().setLeftArmPose(CraftConverter.toBukkitEuler(pose));
	}

	@Override
	public void setLeftLegPose(Vector pose) {
		this.getHandle().setLeftLegPose(CraftConverter.toBukkitEuler(pose));
	}

	@Override
	public void setLeggings(ItemStack item) {
		this.getHandle().setLeggings(((CraftItemStack)item).getHandle());
	}

	@Override
	public void setSmall(boolean value) {
		this.getHandle().setSmall(value);
	}

	@Override
	public void setMarker(boolean value) {
		this.getHandle().setMarker(value);
	}

	@Override
	public void setRightArmPose(Vector pose) {
		this.getHandle().setRightArmPose(CraftConverter.toBukkitEuler(pose));
	}

	@Override
	public void setRightLegPose(Vector pose) {
		this.getHandle().setRightLegPose(CraftConverter.toBukkitEuler(pose));
	}

	@Override
	public void setVisible(boolean value) {
		this.getHandle().setVisible(value);
	}

}