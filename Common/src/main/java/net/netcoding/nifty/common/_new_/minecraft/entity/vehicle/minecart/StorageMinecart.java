package net.netcoding.nifty.common._new_.minecraft.entity.vehicle.minecart;

import net.netcoding.nifty.common._new_.minecraft.inventory.InventoryHolder;

/**
 * Represents a minecart with a chest. These types of {@link Minecart
 * minecarts} have their own inventory that can be accessed using methods
 * from the {@link InventoryHolder} interface.
 */
public interface StorageMinecart extends Minecart, InventoryHolder {

}