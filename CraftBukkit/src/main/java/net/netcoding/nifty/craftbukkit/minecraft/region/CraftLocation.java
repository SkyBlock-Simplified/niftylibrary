package net.netcoding.nifty.craftbukkit.minecraft.region;

import net.netcoding.nifty.common.minecraft.region.Location;
import net.netcoding.nifty.common.minecraft.region.World;

public final class CraftLocation implements Location {

	private final org.bukkit.Location location;
	private World world;
	private boolean wildcard;

	public CraftLocation(org.bukkit.Location location) {
		this.location = location;

		if (location.getWorld() != null)
			this.world = new CraftWorld(location.getWorld());
	}

	@Override
	public Location clone() {
		return new CraftLocation(this.getHandle().clone());
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == this)
			return true;
		else if (!(obj instanceof Location))
			return false;
		else {
			Location other = (Location)obj;
			return Location.equals(this, other);
		}
	}

	public org.bukkit.Location getHandle() {
		return this.location;
	}

	@Override
	public float getPitch() {
		return this.getHandle().getPitch();
	}

	@Override
	public World getWorld() {
		return this.world;
	}

	@Override
	public double getX() {
		return this.getHandle().getX();
	}

	@Override
	public double getY() {
		return getHandle().getY();
	}

	@Override
	public float getYaw() {
		return this.getHandle().getYaw();
	}

	@Override
	public double getZ() {
		return this.getHandle().getZ();
	}

	@Override
	public int hashCode() {
		return this.getHandle().hashCode();
	}

	@Override
	public boolean isWildcard() {
		return this.wildcard;
	}

	@Override
	public void setWildcard(boolean value) {
		this.wildcard = value;
		this.world = null;
	}

	@Override
	public void setPitch(float pitch) {
		this.getHandle().setPitch(pitch);
	}

	@Override
	public void setWorld(World world) {
		if (world == null)
			this.setWildcard(true);
		else {
			this.world = world;
			this.getHandle().setWorld(((CraftWorld)world).getHandle());
		}
	}

	@Override
	public void setX(double x) {
		this.getHandle().setX(x);
	}

	@Override
	public void setY(double y) {
		this.getHandle().setY(y);
	}

	@Override
	public void setYaw(float yaw) {
		this.getHandle().setYaw(yaw);
	}

	@Override
	public void setZ(double z) {
		this.getHandle().setZ(z);
	}

	@Override
	public String toString() {
		return this.getHandle().toString();
	}

	public static final class Builder implements Location.Builder {

		private final CraftLocation location = new CraftLocation(new org.bukkit.Location(null, 0, 0, 0));

		@Override
		public Location build() {
			return this.location;
		}

		@Override
		public Location.Builder fromLocation(Location location) {
			this.location.setPitch(location.getPitch());
			this.location.setWorld(location.getWorld());
			this.location.setX(location.getX());
			this.location.setY(location.getY());
			this.location.setYaw(location.getYaw());
			this.location.setZ(location.getZ());
			return this;
		}

		@Override
		public Location.Builder pitch(float pitch) {
			this.location.setPitch(pitch);
			return this;
		}

		@Override
		public Location.Builder world(World world) {
			this.location.setWorld(world);
			return this;
		}

		@Override
		public Location.Builder x(double x) {
			this.location.setX(x);
			return this;
		}

		@Override
		public Location.Builder y(double y) {
			this.location.setY(y);
			return this;
		}

		@Override
		public Location.Builder yaw(float yaw) {
			this.location.setYaw(yaw);
			return this;
		}

		@Override
		public Location.Builder z(double z) {
			this.location.setZ(z);
			return this;
		}

	}

}