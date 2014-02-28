package net.netcoding.niftybukkit.hologram;

import net.netcoding.niftybukkit.hologram.wrapper.WrapperPlayServerAttachEntity;
import net.netcoding.niftybukkit.hologram.wrapper.WrapperPlayServerSpawnEntity;
import net.netcoding.niftybukkit.hologram.wrapper.WrapperPlayServerSpawnEntityLiving;

import org.bukkit.Location;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;

import com.comphenix.protocol.wrappers.WrappedDataWatcher;

/**
 * Represents a spawner of name tags.
 */
public class NameTagSpawner {

	private static final int SHARED_ENTITY_ID = Short.MAX_VALUE;
	private static final int WITHER_SKULL = 66;

	// Shared entity ID allocator

	// The starting entity ID
	//private int startEntityId;
	//private int nameTagCount;

	/**
	 * Construct a new name tag spawner.
	 * <p>
	 * Specify a number of name tags to spawn.
	 * @param nameTags - the maximum number of name tags we will spawn at any given time.
	 */
	public NameTagSpawner(int nameTagCount) {
		//this.startEntityId = SHARED_ENTITY_ID;
		//this.nameTagCount = nameTagCount;

		// We need to reserve two entity IDs per name tag
		//SHARED_ENTITY_ID += nameTagCount * 2;
	}

	/**
	 * Retrieve the maximum number of name tags we can spawn.
	 * @return The maximum number.
	 */
	public int getNameTagCount() {
		return 0;
	}

	/**
	 * Set the location and message of a name tag.
	 * @param index - index of the name tag. Cannot exceeed {@link #getNameTagCount()}.
	 * @param observer - the observing player.
	 * @param location - the location in the same world as the player.
	 * @param dY - Y value to add to the final location.
	 * @param message - the message to display.
	 */
	public void setNameTag(int index, Player observer, Location location, double dY, String message) {
		WrapperPlayServerAttachEntity attach = new WrapperPlayServerAttachEntity();
		WrapperPlayServerSpawnEntityLiving horse = createHorsePacket(index, location, dY, message);
		WrapperPlayServerSpawnEntity skull = createSkullPacket(index, location, dY);

		// The horse is riding on the skull
		attach.setEntityId(horse.getEntityID());
		attach.setVehicleId(skull.getEntityID());

		horse.sendPacket(observer);
		skull.sendPacket(observer);
		attach.sendPacket(observer);
	}

	// Construct the invisible horse packet
	private WrapperPlayServerSpawnEntityLiving createHorsePacket(int index, Location location, double dY, String message) {
		WrapperPlayServerSpawnEntityLiving horse = new WrapperPlayServerSpawnEntityLiving();
		horse.setEntityID(SHARED_ENTITY_ID + index * 2);
		horse.setType(EntityType.HORSE);
		horse.setX(location.getX());
		horse.setY(location.getY() + dY + 55);
		horse.setZ(location.getZ());

		WrappedDataWatcher wdw = new WrappedDataWatcher();
		wdw.setObject(10, message);
		wdw.setObject(11, (byte) 1);
		wdw.setObject(12, -1700000);
		horse.setMetadata(wdw);
		return horse;
	}

	// Construct the wither skull packet
	private WrapperPlayServerSpawnEntity createSkullPacket(int index, Location location, double dY) {
		WrapperPlayServerSpawnEntity skull = new WrapperPlayServerSpawnEntity();
		skull.setEntityID(SHARED_ENTITY_ID + index * 2 + 1);
		skull.setType(WITHER_SKULL);
		skull.setX(location.getX());
		skull.setY(location.getY() + dY + 55);
		skull.setZ(location.getZ());
		return skull;
	}
}