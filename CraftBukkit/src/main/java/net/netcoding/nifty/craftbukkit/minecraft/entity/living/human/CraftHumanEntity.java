package net.netcoding.nifty.craftbukkit.minecraft.entity.living.human;

import net.netcoding.nifty.common.minecraft.GameMode;
import net.netcoding.nifty.common.minecraft.entity.living.Villager;
import net.netcoding.nifty.common.minecraft.entity.living.human.HumanEntity;
import net.netcoding.nifty.common.minecraft.inventory.Inventory;
import net.netcoding.nifty.common.minecraft.inventory.InventoryView;
import net.netcoding.nifty.common.minecraft.inventory.item.ItemStack;
import net.netcoding.nifty.common.minecraft.inventory.types.PlayerInventory;
import net.netcoding.nifty.common.minecraft.region.Location;
import net.netcoding.nifty.craftbukkit.minecraft.entity.living.CraftLivingEntity;
import net.netcoding.nifty.craftbukkit.minecraft.entity.living.CraftVillager;
import net.netcoding.nifty.craftbukkit.minecraft.inventory.CraftInventory;
import net.netcoding.nifty.craftbukkit.minecraft.inventory.CraftInventoryView;
import net.netcoding.nifty.craftbukkit.minecraft.inventory.item.CraftItemStack;
import net.netcoding.nifty.craftbukkit.minecraft.region.CraftLocation;

public abstract class CraftHumanEntity extends CraftLivingEntity implements HumanEntity {

	private final PlayerInventory inventory;
	private final Inventory enderChest;

	protected CraftHumanEntity(org.bukkit.entity.HumanEntity entity) {
		super(entity);
		this.inventory = CraftInventory.convertBukkitInventory(entity.getInventory(), PlayerInventory.class);
		this.enderChest = CraftInventory.convertBukkitInventory(entity.getEnderChest());
	}

	@Override
	public void closeInventory() {
		this.getHandle().closeInventory();
	}

	@Override
	public Inventory getEnderChest() {
		return this.enderChest;
	}

	@Override
	public GameMode getGameMode() {
		return GameMode.valueOf(this.getHandle().getGameMode().name());
	}

	@Override
	public org.bukkit.entity.HumanEntity getHandle() {
		return (org.bukkit.entity.HumanEntity)super.getHandle();
	}

	@Override
	public PlayerInventory getInventory() {
		return this.inventory;
	}

	@Override
	public ItemStack getItemOnCursor() {
		return new CraftItemStack(this.getHandle().getItemOnCursor());
	}

	@Override
	public int getExpToLevel() {
		return this.getHandle().getExpToLevel();
	}

	@Override
	public int getSleepTicks() {
		return this.getHandle().getSleepTicks();
	}

	@Override
	public boolean isBlocking() {
		return this.getHandle().isBlocking();
	}

	@Override
	public boolean isSleeping() {
		return this.getHandle().isSleeping();
	}

	@Override
	public InventoryView getOpenInventory() {
		return this.getHandle().getOpenInventory() != null ? new CraftInventoryView(this.getHandle().getOpenInventory()) : null;
	}

	@Override
	public InventoryView openEnchanting(Location location, boolean force) {
		return new CraftInventoryView(this.getHandle().openEnchanting(((CraftLocation)location).getHandle(), force));
	}

	@Override
	public InventoryView openInventory(Inventory inventory) {
		return new CraftInventoryView(this.getHandle().openInventory(((CraftInventory)inventory).getHandle()));
	}

	@Override
	public void openInventory(InventoryView view) {
		this.getHandle().openInventory(((CraftInventoryView)view).getHandle());
	}

	@Override
	public InventoryView openMerchant(Villager trader, boolean force) {
		return new CraftInventoryView(this.getHandle().openMerchant(((CraftVillager)trader).getHandle(), force));
	}

	@Override
	public InventoryView openWorkbench(Location location, boolean force) {
		return new CraftInventoryView(this.getHandle().openWorkbench(((CraftLocation)location).getHandle(), force));
	}

	@Override
	public void setGameMode(GameMode gameMode) {
		this.getHandle().setGameMode(org.bukkit.GameMode.valueOf(gameMode.name()));
	}

	@Override
	public void setItemOnCursor(ItemStack item) {
		this.getHandle().setItemOnCursor(((CraftItemStack)item).getHandle());
	}

	@Override
	public boolean setWindowProperty(InventoryView.Property property, int value) {
		return this.getHandle().setWindowProperty(org.bukkit.inventory.InventoryView.Property.valueOf(property.name()), value);
	}

}