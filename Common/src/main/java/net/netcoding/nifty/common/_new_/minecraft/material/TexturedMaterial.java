package net.netcoding.nifty.common._new_.minecraft.material;

import java.util.List;

public interface TexturedMaterial extends MaterialData {

	@Override
	TexturedMaterial clone();

	default Material getMaterial() {
		int index = this.getTextureIndex();

		if (index > this.getTextures().size() - 1)
			index = 0;

		return this.getTextures().get(index);
	}

	default int getTextureIndex() {
		return this.getData();
	}

	List<Material> getTextures();

	default void setMaterial(Material material) {
		if (this.getTextures().contains(material))
			this.setTextureIndex(this.getTextures().indexOf(material));
		else
			this.setTextureIndex(0);
	}

	default void setTextureIndex(int index) {
		this.setData((byte)index);
	}

}