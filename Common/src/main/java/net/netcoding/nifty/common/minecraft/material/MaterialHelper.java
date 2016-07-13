package net.netcoding.nifty.common.minecraft.material;

import net.netcoding.nifty.core.util.concurrent.Concurrent;
import net.netcoding.nifty.core.util.concurrent.ConcurrentMap;
import net.netcoding.nifty.core.util.concurrent.linked.ConcurrentLinkedList;

class MaterialHelper {

	private static final ConcurrentMap<Class<? extends MaterialData>, ConcurrentLinkedList<Material>> TEXTURES = Concurrent.newMap();

	static {
		TEXTURES.put(MonsterEggs.class, new ConcurrentLinkedList<>(
				Material.STONE,
		        Material.COBBLESTONE,
		        Material.SMOOTH_BRICK
		));
		TEXTURES.put(SmoothBrick.class, new ConcurrentLinkedList<>(
				Material.STONE,
		        Material.MOSSY_COBBLESTONE,
		        Material.COBBLESTONE,
		        Material.SMOOTH_BRICK
		));
		TEXTURES.put(Step.class, new ConcurrentLinkedList<>(
				Material.STONE,
				Material.SANDSTONE,
				Material.WOOD,
				Material.COBBLESTONE,
				Material.BRICK,
				Material.SMOOTH_BRICK,
				Material.NETHER_BRICK,
				Material.QUARTZ_BLOCK
		));

	}

	public static ConcurrentLinkedList<Material> getTextures(Class<? extends MaterialData> clazz) {
		return TEXTURES.get(clazz);
	}

}