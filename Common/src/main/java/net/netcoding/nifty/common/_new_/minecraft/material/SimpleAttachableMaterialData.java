package net.netcoding.nifty.common._new_.minecraft.material;

import net.netcoding.nifty.common._new_.minecraft.block.BlockFace;

/**
 * Simple utility class for attachable MaterialData subclasses.
 */
interface SimpleAttachableMaterialData extends MaterialData, Attachable {

	@Override
	SimpleAttachableMaterialData clone();

	@Override
	default BlockFace getFacing() {
		BlockFace attachedFace = this.getAttachedFace();
		return (attachedFace == null ? null : attachedFace.getOppositeFace());
	}

}