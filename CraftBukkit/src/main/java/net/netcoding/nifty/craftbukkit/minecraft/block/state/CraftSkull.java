package net.netcoding.nifty.craftbukkit.minecraft.block.state;

import net.netcoding.nifty.common.minecraft.OfflinePlayer;
import net.netcoding.nifty.common.minecraft.block.BlockFace;
import net.netcoding.nifty.common.minecraft.block.state.Skull;
import net.netcoding.nifty.common.minecraft.inventory.item.SkullType;
import net.netcoding.nifty.craftbukkit.minecraft.CraftOfflinePlayer;

public final class CraftSkull extends CraftBlockState implements Skull {

	public CraftSkull(org.bukkit.block.Skull skull) {
		super(skull);
	}

	@Override
	public org.bukkit.block.Skull getHandle() {
		return (org.bukkit.block.Skull)super.getHandle();
	}

	@Override
	public boolean hasOwner() {
		return this.getHandle().hasOwner();
	}

	@Override
	public boolean setOwner(String name) {
		return this.getHandle().setOwner(name);
	}

	@Override
	public OfflinePlayer getOwningPlayer() {
		return new CraftOfflinePlayer(this.getHandle().getOwningPlayer());
	}

	@Override
	public void setOwningPlayer(OfflinePlayer player) {
		this.getHandle().setOwningPlayer(((CraftOfflinePlayer)player).getHandle());
	}

	@Override
	public BlockFace getRotation() {
		return BlockFace.valueOf(this.getHandle().getRotation().name());
	}

	@Override
	public void setRotation(BlockFace rotation) {
		this.getHandle().setRotation(org.bukkit.block.BlockFace.valueOf(rotation.name()));
	}

	@Override
	public SkullType getSkullType() {
		return SkullType.valueOf(this.getHandle().getSkullType().name());
	}

	@Override
	public void setSkullType(SkullType type) {
		this.getHandle().setSkullType(org.bukkit.SkullType.valueOf(type.name()));
	}

}