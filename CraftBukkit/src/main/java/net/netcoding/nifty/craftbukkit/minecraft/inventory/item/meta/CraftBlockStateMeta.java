package net.netcoding.nifty.craftbukkit.minecraft.inventory.item.meta;

import net.netcoding.nifty.common.Nifty;
import net.netcoding.nifty.common.minecraft.block.state.BlockState;
import net.netcoding.nifty.common.minecraft.inventory.item.meta.BlockStateMeta;
import net.netcoding.nifty.craftbukkit.minecraft.block.state.CraftBlockState;

public final class CraftBlockStateMeta extends CraftItemMeta implements BlockStateMeta {

	public CraftBlockStateMeta(org.bukkit.inventory.meta.BlockStateMeta blockStateMeta) {
		super(blockStateMeta);
	}

	@Override
	public BlockStateMeta clone() {
		BlockState state = this.getBlockState();
		CraftBlockStateMeta stateMeta = (CraftBlockStateMeta)Nifty.getItemFactory().getItemMeta(state.getType());
		stateMeta.addItemFlags(this.getItemFlags());
		stateMeta.setBlockState(state);
		stateMeta.setDisplayName(this.getDisplayName());
		this.getEnchants().forEach(stateMeta::addEnchant);
		stateMeta.setLore(this.getLore());
		return stateMeta;
	}

	@Override
	public BlockState getBlockState() {
		return CraftBlockState.convertBukkitState(this.getHandle().getBlockState());
	}

	@Override
	public org.bukkit.inventory.meta.BlockStateMeta getHandle() {
		return (org.bukkit.inventory.meta.BlockStateMeta)super.getHandle();
	}

	@Override
	public boolean hasBlockState() {
		return this.getHandle().hasBlockState();
	}

	@Override
	public void setBlockState(BlockState blockState) {
		this.getHandle().setBlockState(((CraftBlockState)blockState).getHandle());
	}

}