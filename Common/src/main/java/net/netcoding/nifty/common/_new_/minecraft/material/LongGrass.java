package net.netcoding.nifty.common._new_.minecraft.material;

import net.netcoding.nifty.common._new_.minecraft.GrassSpecies;

public interface LongGrass extends MaterialData {

	@Override
	LongGrass clone();

	default GrassSpecies getSpecies() {
		return GrassSpecies.getByData(getData());
	}

	default void setSpecies(GrassSpecies species) {
		this.setData(species.getData());
	}

}