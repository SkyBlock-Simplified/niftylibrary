package net.netcoding.nifty.common._new_.minecraft.entity.living;

import net.netcoding.nifty.common._new_.minecraft.source.projectiles.ProjectileSource;
import net.netcoding.nifty.common._new_.minecraft.entity.Damageable;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.EntityEquipment;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public interface LivingEntity extends Damageable, ProjectileSource {

	double getEyeHeight();

	double getEyeHeight(boolean var1);

	Location getEyeLocation();

	/** @deprecated */
	@Deprecated
	List<Block> getLineOfSight(HashSet<Byte> var1, int var2);

	List<Block> getLineOfSight(Set<Material> var1, int var2);

	/** @deprecated */
	@Deprecated
	Block getTargetBlock(HashSet<Byte> var1, int var2);

	Block getTargetBlock(Set<Material> var1, int var2);

	/** @deprecated */
	@Deprecated
	List<Block> getLastTwoTargetBlocks(HashSet<Byte> var1, int var2);

	List<Block> getLastTwoTargetBlocks(Set<Material> var1, int var2);

	int getRemainingAir();

	void setRemainingAir(int var1);

	int getMaximumAir();

	void setMaximumAir(int var1);

	int getMaximumNoDamageTicks();

	void setMaximumNoDamageTicks(int var1);

	double getLastDamage();

	/** @deprecated */
	@Deprecated
	int _INVALID_getLastDamage();

	void setLastDamage(double var1);

	/** @deprecated */
	@Deprecated
	void _INVALID_setLastDamage(int var1);

	int getNoDamageTicks();

	void setNoDamageTicks(int var1);

	Player getKiller();

	boolean addPotionEffect(PotionEffect var1);

	boolean addPotionEffect(PotionEffect var1, boolean var2);

	boolean addPotionEffects(Collection<PotionEffect> var1);

	boolean hasPotionEffect(PotionEffectType var1);

	void removePotionEffect(PotionEffectType var1);

	Collection<PotionEffect> getActivePotionEffects();

	boolean hasLineOfSight(Entity var1);

	boolean getRemoveWhenFarAway();

	void setRemoveWhenFarAway(boolean var1);

	EntityEquipment getEquipment();

	void setCanPickupItems(boolean var1);

	boolean getCanPickupItems();

	boolean isLeashed();

	Entity getLeashHolder() throws IllegalStateException;

	boolean setLeashHolder(Entity var1);

	boolean isGliding();

	void setGliding(boolean var1);

	void setAI(boolean var1);

	boolean hasAI();

	void setCollidable(boolean var1);

	boolean isCollidable();

}