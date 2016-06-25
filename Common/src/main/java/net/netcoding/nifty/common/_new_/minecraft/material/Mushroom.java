package net.netcoding.nifty.common._new_.minecraft.material;

import net.netcoding.nifty.common._new_.minecraft.block.BlockFace;
import net.netcoding.nifty.common._new_.minecraft.material.types.MushroomBlockTexture;
import net.netcoding.nifty.core.util.concurrent.Concurrent;
import net.netcoding.nifty.core.util.concurrent.ConcurrentSet;

import java.util.Set;

public interface Mushroom extends MaterialData {

	@Override
	Mushroom clone();

	default MushroomBlockTexture getBlockTexture() {
		return MushroomBlockTexture.getByData(getData());
	}

	default Set<BlockFace> getPaintedFaces() {
		ConcurrentSet<BlockFace> faces = Concurrent.newSet();

		if (isFacePainted(BlockFace.WEST))
			faces.add(BlockFace.WEST);

		if (isFacePainted(BlockFace.NORTH))
			faces.add(BlockFace.NORTH);

		if (isFacePainted(BlockFace.SOUTH))
			faces.add(BlockFace.SOUTH);

		if (isFacePainted(BlockFace.EAST))
			faces.add(BlockFace.EAST);

		if (isFacePainted(BlockFace.UP))
			faces.add(BlockFace.UP);

		if (isFacePainted(BlockFace.DOWN))
			faces.add(BlockFace.DOWN);

		return faces;
	}

	default boolean isFacePainted(BlockFace face) {
		byte data = this.getData();

		if (data == MushroomBlockTexture.ALL_PORES.getData() || data == MushroomBlockTexture.STEM_SIDES.getData() || data == MushroomBlockTexture.ALL_STEM.getData())
			return false;

		switch (face) {
			case WEST:
				return data < 0x4; // NORTH_LIMIT
			case EAST:
				return data > 0x6; // SOUTH_LIMIT
			case NORTH:
				return data % 0x3 == 0x0; // EAST_WEST_LIMIT == EAST_REMAINDER
			case SOUTH:
				return data % 0x3 == 0x1; // EAST_WEST_LIMIT == WEST_REMAINDER
			case UP:
				return true;
			case DOWN:
			case SELF:
				return data == MushroomBlockTexture.ALL_CAP.getData();
			default:
				return false;
		}
	}

	default void setBlockTexture(MushroomBlockTexture texture) {
		this.setData(texture.getData());
	}

	default void setStem() {
		this.setData(MushroomBlockTexture.STEM_SIDES.getData());
	}

}