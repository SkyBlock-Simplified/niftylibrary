package net.netcoding.nifty.craftbukkit.minecraft.entity.living;

import net.netcoding.nifty.common.minecraft.entity.living.Ageable;

public abstract class CraftAgeable extends CraftCreature implements Ageable {

	public CraftAgeable(org.bukkit.entity.Ageable ageable) {
		super(ageable);
	}

	@Override
	public boolean canBreed() {
		return this.getHandle().canBreed();
	}

	@Override
	public int getAge() {
		return this.getHandle().getAge();
	}

	@Override
	public boolean getAgeLock() {
		return this.getHandle().getAgeLock();
	}

	@Override
	public org.bukkit.entity.Ageable getHandle() {
		return (org.bukkit.entity.Ageable)super.getHandle();
	}

	@Override
	public boolean isAdult() {
		return this.getHandle().isAdult();
	}

	@Override
	public void setAge(int age) {
		this.getHandle().setAge(age);
	}

	@Override
	public void setAgeLock(boolean lock) {
		this.getHandle().setAgeLock(lock);
	}

	@Override
	public void setAdult() {
		this.getHandle().setAdult();
	}

	@Override
	public void setBaby() {
		this.getHandle().setBaby();
	}

	@Override
	public void setBreed(boolean breed) {
		this.getHandle().setBreed(breed);
	}

}