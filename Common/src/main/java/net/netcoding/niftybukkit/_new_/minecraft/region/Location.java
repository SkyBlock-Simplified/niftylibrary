package net.netcoding.niftybukkit._new_.minecraft.region;

import com.google.common.base.Preconditions;
import net.netcoding.niftybukkit.Nifty;
import net.netcoding.niftybukkit._new_.minecraft.block.Block;
import net.netcoding.niftycore.util.NumberUtil;
import net.netcoding.niftycore.util.misc.Serializable;
import net.netcoding.niftycore.util.StringUtil;
import net.netcoding.niftycore.util.misc.Vector;
import net.netcoding.niftycore.util.concurrent.linked.ConcurrentLinkedMap;

import java.util.Map;

public class Location implements Cloneable, Serializable {

	private World world;
	private double x;
	private double y;
	private double z;
	private float pitch;
	private float yaw;
	private boolean wildcard;

	public Location(World world, double x, double y, double z) {
		this(world, x, y, z, 0.0F, 0.0F);
	}

	public Location(World world, double x, double y, double z, float yaw, float pitch) {
		this.world = world;
		this.x = x;
		this.y = y;
		this.z = z;
		this.pitch = pitch;
		this.yaw = yaw;
	}

	public final Location add(Location location) {
		Preconditions.checkArgument(location != null, "Cannot add a NULL location!");
		Preconditions.checkArgument(location.getWorld() != null && this.getWorld() != null, "Cannot add from/to a NULL world!");
		Preconditions.checkArgument(!location.getWorld().equals(this.getWorld()), StringUtil.format("Cannot add locations of worlds ''{0}'' and ''{1}''!", this.getWorld().getName(), location.getWorld().getName()));
		this.x += location.x;
		this.y += location.y;
		this.z += location.z;
		return this;
	}

	public final Location add(Vector vec) {
		this.x += vec.getX();
		this.y += vec.getY();
		this.z += vec.getZ();
		return this;
	}

	public final Location add(double x, double y, double z) {
		this.x += x;
		this.y += y;
		this.z += z;
		return this;
	}

	@Override
	public final Location clone() {
		try {
			return (Location) super.clone();
		} catch (CloneNotSupportedException var2) {
			throw new Error(var2);
		}
	}

	public static Location deserialize(Map<String, Object> map) {
		String worldName = (String)map.get("world");
		Preconditions.checkArgument(worldName != null, "World cannot be NULL!");
		boolean wildcard = false;
		World world;

		if (worldName.matches("(?i)^%(world%)?$")) {
			world = Nifty.getServer().getWorlds().get(0);
			wildcard = true;
		} else
			world = Nifty.getServer().getWorld(worldName);

		Preconditions.checkArgument(world != null, StringUtil.format("Unknown world with name ''{0}''!", worldName));
		float yaw = (map.get("yaw") instanceof Double) ? ((Double)map.get("yaw")).floatValue() : (Float)map.get("yaw");
		float pitch = (map.get("pitch") instanceof Double) ? ((Double)map.get("pitch")).floatValue() : (Float)map.get("pitch");
		Location location = new Location(world, NumberUtil.to(map.get("x"), Double.class), NumberUtil.to(map.get("y"), Double.class), NumberUtil.to(map.get("z"), Double.class), yaw, pitch);
		location.wildcard = wildcard;
		return location;
	}

	public final double distance(Location location) {
		return Math.sqrt(this.distanceSquared(location));
	}

	public final double distanceSquared(Location location) {
		Preconditions.checkArgument(location != null, "Cannot measure distance to a NULL location!");
		Preconditions.checkArgument(location.getWorld() != null && this.getWorld() != null, "Cannot measure distance to a NULL world!");
		Preconditions.checkArgument(!location.getWorld().equals(this.getWorld()), StringUtil.format("Cannot measure distance between ''{0}'' and ''{1}''!", this.getWorld().getName(), location.getWorld().getName()));
		return NumberUtil.square(this.getX() - location.getX()) + NumberUtil.square(this.getY() - location.getY()) + NumberUtil.square(this.getZ() - location.getZ());
	}

	@Override
	public final boolean equals(Object obj) {
		if (obj == null)
			return false;
		else if (Location.class.isAssignableFrom(obj.getClass()))
			return true;
		else {
			Location other = (Location) obj;
			return this.world != other.world && (this.world == null || !this.world.equals(other.world)) ? false : (Double.doubleToLongBits(this.x) != Double.doubleToLongBits(other.x) ? false : (Double.doubleToLongBits(this.y) != Double.doubleToLongBits(other.y) ? false : (Double.doubleToLongBits(this.z) != Double.doubleToLongBits(other.z) ? false : (Float.floatToIntBits(this.pitch) != Float.floatToIntBits(other.pitch) ? false : Float.floatToIntBits(this.yaw) == Float.floatToIntBits(other.yaw)))));
		}
	}

	public final Block getBlock() {
		return this.world.getBlockAt(this);
	}

	public final int getBlockX() {
		return locToBlock(this.x);
	}

	public final int getBlockY() {
		return locToBlock(this.y);
	}

	public final int getBlockZ() {
		return locToBlock(this.z);
	}

	public final Chunk getChunk() {
		return this.world.getChunkAt(this);
	}

	public final Vector getDirection() {
		Vector vector = new Vector();
		double rotX = (double) this.getYaw();
		double rotY = (double) this.getPitch();
		vector.setY(-Math.sin(Math.toRadians(rotY)));
		double xz = Math.cos(Math.toRadians(rotY));
		vector.setX(-xz * Math.sin(Math.toRadians(rotX)));
		vector.setZ(xz * Math.cos(Math.toRadians(rotX)));
		return vector;
	}

	public final float getPitch() {
		return this.pitch;
	}

	public final World getWorld() {
		return this.world;
	}

	public final double getX() {
		return this.x;
	}

	public final double getY() {
		return this.y;
	}

	public final float getYaw() {
		return this.yaw;
	}

	public final double getZ() {
		return this.z;
	}

	@Override
	public final int hashCode() {
		byte hash = 3;
		int hash1 = 19 * hash + (this.world != null ? this.world.hashCode() : 0);
		hash1 = 19 * hash1 + (int) (Double.doubleToLongBits(this.x) ^ Double.doubleToLongBits(this.x) >>> 32);
		hash1 = 19 * hash1 + (int) (Double.doubleToLongBits(this.y) ^ Double.doubleToLongBits(this.y) >>> 32);
		hash1 = 19 * hash1 + (int) (Double.doubleToLongBits(this.z) ^ Double.doubleToLongBits(this.z) >>> 32);
		hash1 = 19 * hash1 + Float.floatToIntBits(this.pitch);
		hash1 = 19 * hash1 + Float.floatToIntBits(this.yaw);
		return hash1;
	}

	public final boolean isWildcard() {
		return this.wildcard;
	}

	public final double length() {
		return Math.sqrt(NumberUtil.square(this.x) + NumberUtil.square(this.y) + NumberUtil.square(this.z));
	}

	public final double lengthSquared() {
		return NumberUtil.square(this.x) + NumberUtil.square(this.y) + NumberUtil.square(this.z);
	}

	public static int locToBlock(double loc) {
		return NumberUtil.floor(loc);
	}

	public final Location multiply(double m) {
		this.x *= m;
		this.y *= m;
		this.z *= m;
		return this;
	}

	@Override
	public final Map<String, Object> serialize() {
		ConcurrentLinkedMap<String, Object> data = new ConcurrentLinkedMap<>();
		data.put("world", (this.isWildcard() ? "%world%" : this.world.getName()));
		data.put("x", this.x);
		data.put("y", this.y);
		data.put("z", this.z);
		data.put("yaw", this.yaw);
		data.put("pitch", this.pitch);
		return data;
	}

	public final Location setDirection(Vector vector) {
		double x = vector.getX();
		double z = vector.getZ();

		if (x == 0.0D && z == 0.0D) {
			this.pitch = (float) (vector.getY() > 0.0D ? -90 : 90);
			return this;
		} else {
			double theta = Math.atan2(-x, z);
			this.yaw = (float) Math.toDegrees((theta + 6.283185307179586D) % 6.283185307179586D);
			double x2 = NumberUtil.square(x);
			double z2 = NumberUtil.square(z);
			double xz = Math.sqrt(x2 + z2);
			this.pitch = (float) Math.toDegrees(Math.atan(-vector.getY() / xz));
			return this;
		}
	}

	public final void setPitch(float pitch) {
		this.pitch = pitch;
	}

	public final void setWorld(World world) {
		this.world = world;
		this.wildcard = false;
	}

	public final void setX(double x) {
		this.x = x;
	}

	public final void setY(double y) {
		this.y = y;
	}

	public final void setYaw(float yaw) {
		this.yaw = yaw;
	}

	public final void setZ(double z) {
		this.z = z;
	}

	public final Location subtract(Location vec) {
		if (vec != null && vec.getWorld().equals(this.getWorld())) {
			this.x -= vec.x;
			this.y -= vec.y;
			this.z -= vec.z;
			return this;
		}

		throw new IllegalArgumentException("Cannot add Locations of differing worlds");
	}

	public final Location subtract(Vector vec) {
		this.x -= vec.getX();
		this.y -= vec.getY();
		this.z -= vec.getZ();
		return this;
	}

	public final Location subtract(double x, double y, double z) {
		this.x -= x;
		this.y -= y;
		this.z -= z;
		return this;
	}

	@Override
	public String toString() {
		return "Location{world=" + this.world + ",x=" + this.x + ",y=" + this.y + ",z=" + this.z + ",pitch=" + this.pitch + ",yaw=" + this.yaw + '}';
	}

	public final Vector toVector() {
		return new Vector(this.x, this.y, this.z);
	}

	public final Location zero() {
		this.x = 0.0D;
		this.y = 0.0D;
		this.z = 0.0D;
		return this;
	}

}