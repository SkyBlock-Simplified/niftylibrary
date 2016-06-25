package net.netcoding.nifty.common._new_.minecraft.material;

import net.netcoding.nifty.common._new_.minecraft.GrassSpecies;
import net.netcoding.nifty.common._new_.minecraft.TreeSpecies;

public interface FlowerPot extends MaterialData {

	@Override
	FlowerPot clone();

	MaterialData getContents();

	default void setContents(MaterialData materialData) {
		Material mat = materialData.getItemType();

		if (mat == Material.RED_ROSE)
			this.setData((byte)1);
		else if (mat == Material.YELLOW_FLOWER)
			this.setData((byte)2);
		else if (mat == Material.RED_MUSHROOM)
			this.setData((byte)7);
		else if (mat == Material.BROWN_MUSHROOM)
			this.setData((byte)8);
		else if (mat == Material.CACTUS)
			this.setData((byte)9);
		else if (mat == Material.DEAD_BUSH)
			this.setData((byte)10);
		else if (mat == Material.SAPLING) {
			TreeSpecies species = ((Tree)materialData).getSpecies();

			if (species == TreeSpecies.GENERIC)
				this.setData((byte)3);
			else if (species == TreeSpecies.REDWOOD)
				this.setData((byte)4);
			else if (species == TreeSpecies.BIRCH)
				this.setData((byte)5);
			else
				this.setData((byte)6);
		} else if (mat == Material.LONG_GRASS) {
			GrassSpecies species = ((LongGrass)materialData).getSpecies();

			if (species == GrassSpecies.FERN_LIKE) {
				this.setData((byte)11);
			}
		}
	}

}