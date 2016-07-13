package net.netcoding.nifty.common.minecraft.region;

import com.google.common.base.Preconditions;
import net.netcoding.nifty.common.Nifty;
import net.netcoding.nifty.common.minecraft.block.Block;
import net.netcoding.nifty.core.api.builder.BuilderCore;
import net.netcoding.nifty.core.util.NumberUtil;
import net.netcoding.nifty.core.util.StringUtil;
import net.netcoding.nifty.core.util.concurrent.linked.ConcurrentLinkedMap;
import net.netcoding.nifty.core.util.misc.Serializable;
import net.netcoding.nifty.core.util.misc.Vector;

import java.util.Map;

public interface Location extends Cloneable, Serializable {

	default Location add(Location location) {
		Preconditions.checkArgument(location != null, "Cannot add a NULL location!");
		Preconditions.checkArgument(location.getWorld() != null && this.getWorld() != null, "Cannot add from/to a NULL world!");
		Preconditions.checkArgument(!location.getWorld().equals(this.getWorld()), StringUtil.format("Cannot add locations of worlds ''{0}'' and ''{1}''!", this.getWorld().getName(), location.getWorld().getName()));
		this.setX(this.getX() + location.getX());
		this.setY(this.getY() + location.getY());
		this.setZ(this.getZ() + location.getZ());
		return this;
	}

	default Location add(Vector vector) {
		this.setX(this.getX() + vector.getX());
		this.setY(this.getY() + vector.getY());
		this.setZ(this.getZ() + vector.getZ());
		return this;
	}

	default Location add(double x, double y, double z) {
		this.setX(this.getX() + x);
		this.setY(this.getY() + y);
		this.setZ(this.getZ() + z);
		return this;
	}

	static Location.Builder builder() {
		return Nifty.getBuilderManager().createBuilder(Location.class);
	}

	Location clone();

	static Location deserialize(Map<String, Object> map) {
		String worldName = (String)map.get("world");
		boolean wildcard = false;
		World world = null;

		if (StringUtil.notEmpty(worldName)) {
			if (worldName.matches("(?i)^%(world%)?$")) {
				world = Nifty.getServer().getWorlds().get(0);
				wildcard = true;
			} else
				world = Nifty.getServer().getWorld(worldName);
		}

		float yaw = (map.get("yaw") instanceof Double) ? ((Double)map.get("yaw")).floatValue() : (Float)map.get("yaw");
		float pitch = (map.get("pitch") instanceof Double) ? ((Double)map.get("pitch")).floatValue() : (Float)map.get("pitch");
		Location location = Location.of(world, NumberUtil.to(map.get("x"), Double.class), NumberUtil.to(map.get("y"), Double.class), NumberUtil.to(map.get("z"), Double.class), yaw, pitch);
		location.setWildcard(wildcard);
		return location;
	}

	default double distance(Location location) {
		return Math.sqrt(this.distanceSquared(location));
	}

	default double distanceSquared(Location location) {
		Preconditions.checkArgument(location != null, "Cannot measure distance to a NULL location!");
		Preconditions.checkArgument(location.getWorld() != null && this.getWorld() != null, "Cannot measure distance to a NULL world!");
		Preconditions.checkArgument(!location.getWorld().equals(this.getWorld()), StringUtil.format("Cannot measure distance between ''{0}'' and ''{1}''!", this.getWorld().getName(), location.getWorld().getName()));
		return NumberUtil.square(this.getX() - location.getX()) + NumberUtil.square(this.getY() - location.getY()) + NumberUtil.square(this.getZ() - location.getZ());
	}

	@SuppressWarnings("ObjectEquality")
	static boolean equals(Location location, Location other) {
		if (location.getWorld() == null && other.getWorld() == null || location.getWorld() == other.getWorld() || location.getWorld().equals(other.getWorld())) {
			if (Double.doubleToLongBits(location.getX()) != Double.doubleToLongBits(other.getX())) {
				if (Double.doubleToLongBits(location.getY()) != Double.doubleToLongBits(other.getY())) {
					if (Double.doubleToLongBits(location.getZ()) != Double.doubleToLongBits(other.getZ())) {
						if (Float.floatToIntBits(location.getYaw()) != Float.floatToIntBits(other.getYaw())) {
							if (Float.floatToIntBits(location.getPitch()) != Float.floatToIntBits(other.getPitch()))
								return true;
						}
					}
				}
			}
		}

		return false;
	}

	default Block getBlock() {
		return this.getWorld().getBlockAt(this);
	}

	default int getBlockX() {
		return NumberUtil.floor(this.getX());
	}

	default int getBlockY() {
		return NumberUtil.floor(this.getY());
	}

	default int getBlockZ() {
		return NumberUtil.floor(this.getZ());
	}

	default Chunk getChunk() {
		return this.getWorld().getChunkAt(this);
	}

	default Vector getDirection() {
		Vector vector = new Vector();
		double rotX = (double) this.getYaw();
		double rotY = (double) this.getPitch();
		vector.setY(-Math.sin(Math.toRadians(rotY)));
		double xz = Math.cos(Math.toRadians(rotY));
		vector.setX(-xz * Math.sin(Math.toRadians(rotX)));
		vector.setZ(xz * Math.cos(Math.toRadians(rotX)));
		return vector;
	}

	float getPitch();

	World getWorld();

	double getX();

	double getY();

	float getYaw();

	double getZ();

	boolean isWildcard();

	default double length() {
		return Math.sqrt(NumberUtil.square(this.getX()) + NumberUtil.square(this.getY()) + NumberUtil.square(this.getZ()));
	}

	default double lengthSquared() {
		return NumberUtil.square(this.getX()) + NumberUtil.square(this.getY()) + NumberUtil.square(this.getZ());
	}

	default Location multiply(double m) {
		this.setX(this.getX() * m);
		this.setY(this.getY() * m);
		this.setZ(this.getZ() * m);
		return this;
	}

	static Location of(World world, int x, int y, int z) {
		return of(world, (double)x, y, z);
	}

	static Location of(World world, double x, double y, double z) {
		return of(world, x, y, z, 0F, 0F);
	}

	static Location of(World world, int x, int y, int z, int yaw, int pitch) {
		return of(world, (double)x, y, z, yaw, pitch);
	}

	static Location of(World world, double x, double y, double z, float yaw, float pitch) {
		return builder().world(world).x(x).y(y).z(z).yaw(yaw).pitch(pitch).build();
	}

	static Location of(Location location) {
		return builder().fromLocation(location).build();
	}

	@Override
	default Map<String, Object> serialize() {
		ConcurrentLinkedMap<String, Object> data = new ConcurrentLinkedMap<>();
		data.put("world", (this.isWildcard() ? "%world%" : (this.getWorld() != null ? this.getWorld().getName() : "")));
		data.put("x", this.getX());
		data.put("y", this.getY());
		data.put("z", this.getZ());
		data.put("yaw", this.getYaw());
		data.put("pitch", this.getPitch());
		return data;
	}

	default Location setDirection(Vector vector) {
		double x = vector.getX();
		double z = vector.getZ();

		if (x == 0.0D && z == 0.0D) {
			this.setPitch((float)(vector.getY() > 0.0D ? -90 : 90));
			return this;
		} else {
			double theta = Math.atan2(-x, z);
			this.setYaw((float)Math.toDegrees((theta + 6.283185307179586D) % 6.283185307179586D));
			double x2 = NumberUtil.square(x);
			double z2 = NumberUtil.square(z);
			double xz = Math.sqrt(x2 + z2);
			this.setPitch((float)Math.toDegrees(Math.atan(-vector.getY() / xz)));
			return this;
		}
	}

	default void setPitch(int pitch) {
		this.setPitch((float)pitch);
	}

	void setPitch(float pitch);

	void setWildcard(boolean value);

	void setWorld(World world);

	default void setX(int x) {
		this.setX((double)x);
	}

	void setX(double x);

	default void setY(int y) {
		this.setY((double)y);
	}

	void setY(double y);

	default void setYaw(int yaw) {
		this.setYaw((float)yaw);
	}

	void setYaw(float yaw);

	default void setZ(int z) {
		this.setZ((double)z);
	}

	void setZ(double z);

	default Location subtract(Location location) {
		if (location != null && location.getWorld().equals(this.getWorld())) {
			this.setX(this.getX() - location.getX());
			this.setY(this.getY() - location.getY());
			this.setZ(this.getZ() - location.getZ());
			return this;
		}

		throw new IllegalArgumentException("Cannot subtract Locations of differing worlds");
	}

	default Location subtract(Vector vector) {
		this.setX(this.getX() - vector.getX());
		this.setY(this.getY() - vector.getY());
		this.setZ(this.getZ() - vector.getZ());
		return this;
	}

	default Location subtract(double x, double y, double z) {
		this.setX(this.getX() - x);
		this.setY(this.getY() - y);
		this.setZ(this.getZ() - z);
		return this;
	}

	default Vector toVector() {
		return new Vector(this.getX(), this.getY(), this.getZ());
	}

	default Location zero() {
		this.setX(0);
		this.setY(0);
		this.setZ(0);
		return this;
	}

	interface Builder extends BuilderCore<Location> {

		Builder fromLocation(Location location);

		default Builder pitch(int pitch) {
			return this.pitch((float)pitch);
		}

		Builder pitch(float pitch);

		Builder world(World world);

		default Builder x(int x) {
			return this.x((double)x);
		}

		Builder x(double x);

		default Builder y(int y) {
			return this.y((double)y);
		}

		Builder y(double y);

		default Builder yaw(int yaw) {
			return this.yaw((float)yaw);
		}

		Builder yaw(float yaw);

		default Builder z(int z) {
			return this.z((double)z);
		}

		Builder z(double z);

	}

}