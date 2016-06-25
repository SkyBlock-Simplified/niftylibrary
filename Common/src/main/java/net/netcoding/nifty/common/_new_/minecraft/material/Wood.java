package net.netcoding.nifty.common._new_.minecraft.material;

import net.netcoding.nifty.common._new_.minecraft.TreeSpecies;

public interface Wood extends MaterialData {

	@Override
	Wood clone();

	default TreeSpecies getSpecies() {
		switch (this.getItemType()) {
			case WOOD:
			case WOOD_DOUBLE_STEP:
				return TreeSpecies.getByData(this.getData());
			case LOG:
			case LEAVES:
				return TreeSpecies.getByData((byte)(this.getData() & 0x3));
			case LOG_2:
			case LEAVES_2:
				return TreeSpecies.getByData((byte)((this.getData() & 0x3) | 0x4));
			case SAPLING:
			case WOOD_STEP:
				return TreeSpecies.getByData((byte)(this.getData() & 0x7));
		}

		throw new IllegalStateException("Invalid block type for tree species!");
	}

	default void setSpecies(final TreeSpecies species) {
		boolean firstType = false;

		switch (this.getItemType()) {
			case WOOD:
			case WOOD_DOUBLE_STEP:
				this.setData(species.getData());
				break;
			case LOG:
			case LEAVES:
				firstType = true;
				// fall through to next switch statement below
			case LOG_2:
			case LEAVES_2:
				switch (species) {
					case GENERIC:
					case REDWOOD:
					case BIRCH:
					case JUNGLE:
						if (!firstType)
							throw new IllegalStateException("Invalid tree species for block type, use block type 2 instead!");

						break;
					case ACACIA:
					case DARK_OAK:
						if (firstType)
							throw new IllegalStateException("Invalid tree species for block type 2, use block type instead!");

						break;
				}

				this.setData((byte) ((this.getData() & 0xC) | (species.getData() & 0x3)));
				break;
			case SAPLING:
			case WOOD_STEP:
				this.setData((byte) ((this.getData() & 0x8) | species.getData()));
				break;
			default:
				throw new IllegalStateException("Invalid block type for tree species!");
		}
	}

}