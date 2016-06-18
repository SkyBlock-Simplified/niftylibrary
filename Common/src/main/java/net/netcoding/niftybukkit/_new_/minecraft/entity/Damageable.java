package net.netcoding.niftybukkit._new_.minecraft.entity;

public interface Damageable extends Entity {

	void damage(double value);

	void damage(double value, Entity by);

	double getHealth();

	double getMaxHealth();

	void resetMaxHealth();

	void setHealth(double value);

	void setMaxHealth(double var1);

}