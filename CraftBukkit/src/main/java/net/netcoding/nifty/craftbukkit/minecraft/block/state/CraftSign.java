package net.netcoding.nifty.craftbukkit.minecraft.block.state;

import net.netcoding.nifty.common.minecraft.block.state.Sign;

public final class CraftSign extends CraftBlockState implements Sign {

	public CraftSign(org.bukkit.block.Sign sign) {
		super(sign);
	}

	@Override
	public org.bukkit.block.Sign getHandle() {
		return (org.bukkit.block.Sign)super.getHandle();
	}

	@Override
	public String[] getLines() {
		return this.getHandle().getLines();
	}

	@Override
	public void setLine(int index, String value) throws IndexOutOfBoundsException {
		this.getHandle().setLine(index, value);
	}

}