package net.netcoding.nifty.craftbukkit.minecraft.material;

import net.netcoding.nifty.common.minecraft.material.*;

public enum CraftMaterial {

	WOOD(Material.WOOD, CraftWood.class),
	SAPLING(Material.SAPLING, CraftSapling.class),
	LOG(Material.LOG, CraftTree.class),
	LEAVES(Material.LEAVES, CraftLeaves.class),
	DISPENSER(Material.DISPENSER, CraftDispenser.class),
	SANDSTONE(Material.SANDSTONE, CraftSandstone.class),
	BED_BLOCK(Material.BED_BLOCK, CraftBed.class),
	POWERED_RAIL(Material.POWERED_RAIL, CraftPoweredRail.class),
	DETECTOR_RAIL(Material.DETECTOR_RAIL, CraftDetectorRail.class),
	PISTON_STICKY_BASE(Material.PISTON_STICKY_BASE, CraftPistonBaseMaterial.class),
	LONG_GRASS(Material.LONG_GRASS, CraftLongGrass.class),
	PISTON_BASE(Material.PISTON_BASE, CraftPistonBaseMaterial.class),
	PISTON_EXTENSION(Material.PISTON_EXTENSION, CraftPistonExtensionMaterial.class),
	WOOL(Material.WOOL, CraftWool.class),
	DOUBLE_STEP(Material.DOUBLE_STEP, CraftStep.class),
	STEP(Material.STEP, CraftStep.class),
	TORCH(Material.TORCH, CraftTorch.class),
	WOOD_STAIRS(Material.WOOD_STAIRS, CraftStairs.class),
	CHEST(Material.CHEST, CraftChest.class),
	REDSTONE_WIRE(Material.REDSTONE_WIRE, CraftRedstoneWire.class),
	CROPS(Material.CROPS, CraftCrops.class),
	FURNACE(Material.FURNACE, CraftFurnace.class),
	BURNING_FURNACE(Material.BURNING_FURNACE, CraftFurnace.class),
	SIGN_POST(Material.SIGN_POST, CraftSign.class),
	WOODEN_DOOR(Material.WOODEN_DOOR, CraftDoor.class),
	LADDER(Material.LADDER, CraftLadder.class),
	RAILS(Material.RAILS, CraftRails.class),
	COBBLESTONE_STAIRS(Material.COBBLESTONE_STAIRS, CraftStairs.class),
	WALL_SIGN(Material.WALL_SIGN, CraftSign.class),
	LEVER(Material.LEVER, CraftLever.class),
	STONE_PLATE(Material.STONE_PLATE, CraftPressurePlate.class),
	IRON_DOOR_BLOCK(Material.IRON_DOOR_BLOCK, CraftDoor.class),
	WOOD_PLATE(Material.WOOD_PLATE, CraftPressurePlate.class),
	REDSTONE_TORCH_OFF(Material.REDSTONE_TORCH_OFF, CraftRedstoneTorch.class),
	REDSTONE_TORCH_ON(Material.REDSTONE_TORCH_ON, CraftRedstoneTorch.class),
	STONE_BUTTON(Material.STONE_BUTTON, CraftButton.class),
	PUMPKIN(Material.PUMPKIN, CraftPumpkin.class),
	JACK_O_LANTERN(Material.JACK_O_LANTERN, CraftPumpkin.class),
	CAKE_BLOCK(Material.CAKE_BLOCK, CraftCake.class),
	DIODE_BLOCK_OFF(Material.DIODE_BLOCK_OFF, CraftDiode.class),
	DIODE_BLOCK_ON(Material.DIODE_BLOCK_ON, CraftDiode.class),
	TRAP_DOOR(Material.TRAP_DOOR, CraftTrapDoor.class),
	MONSTER_EGGS(Material.MONSTER_EGGS, CraftMonsterEggs.class),
	SMOOTH_BRICK(Material.SMOOTH_BRICK, CraftSmoothBrick.class),
	HUGE_MUSHROOM_1(Material.HUGE_MUSHROOM_1, CraftMushroom.class),
	HUGE_MUSHROOM_2(Material.HUGE_MUSHROOM_2, CraftMushroom.class),
	VINE(Material.VINE, CraftVine.class),
	FENCE_GATE(Material.FENCE_GATE, CraftGate.class),
	BRICK_STAIRS(Material.BRICK_STAIRS, CraftStairs.class),
	SMOOTH_STAIRS(Material.SMOOTH_STAIRS, CraftStairs.class),
	NETHER_BRICK_STAIRS(Material.NETHER_BRICK_STAIRS, CraftStairs.class),
	NETHER_WARTS(Material.NETHER_WARTS, CraftNetherWarts.class),
	CAULDRON(Material.CAULDRON, CraftCauldron.class),
	WOOD_DOUBLE_STEP(Material.WOOD_DOUBLE_STEP, CraftWood.class),
	WOOD_STEP(Material.WOOD_STEP, CraftWoodenStep.class),
	COCOA(Material.COCOA, CraftCocoaPlant.class),
	SANDSTONE_STAIRS(Material.SANDSTONE_STAIRS, CraftStairs.class),
	ENDER_CHEST(Material.ENDER_CHEST, CraftEnderChest.class),
	TRIPWIRE_HOOK(Material.TRIPWIRE_HOOK, CraftTripwireHook.class),
	TRIPWIRE(Material.TRIPWIRE, CraftTripwire.class),
	SPRUCE_WOOD_STAIRS(Material.SPRUCE_WOOD_STAIRS, CraftStairs.class),
	BIRCH_WOOD_STAIRS(Material.BIRCH_WOOD_STAIRS, CraftStairs.class),
	JUNGLE_WOOD_STAIRS(Material.JUNGLE_WOOD_STAIRS, CraftStairs.class),
	COMMAND(Material.COMMAND, CraftCommand.class),
	FLOWER_POT(Material.FLOWER_POT, CraftFlowerPot.class),
	CARROT(Material.CARROT, CraftCrops.class),
	POTATO(Material.POTATO, CraftCrops.class),
	WOOD_BUTTON(Material.WOOD_BUTTON, CraftButton.class),
	SKULL(Material.SKULL, CraftSkull.class),
	TRAPPED_CHEST(Material.TRAPPED_CHEST, CraftChest.class),
	GOLD_PLATE(Material.GOLD_PLATE, CraftPressurePlate.class),
	IRON_PLATE(Material.IRON_PLATE, CraftPressurePlate.class),
	REDSTONE_COMPARATOR_OFF(Material.REDSTONE_COMPARATOR_OFF, CraftComparator.class),
	REDSTONE_COMPARATOR_ON(Material.REDSTONE_COMPARATOR_ON, CraftComparator.class),
	HOPPER(Material.HOPPER, CraftHopper.class),
	QUARTZ_STAIRS(Material.QUARTZ_STAIRS, CraftStairs.class),
	ACTIVATOR_RAIL(Material.ACTIVATOR_RAIL, CraftPoweredRail.class),
	DROPPER(Material.DROPPER, CraftDispenser.class),
	LEAVES_2(Material.LEAVES_2, CraftLeaves.class),
	LOG_2(Material.LOG_2, CraftTree.class),
	ACACIA_STAIRS(Material.ACACIA_STAIRS, CraftStairs.class),
	DARK_OAK_STAIRS(Material.DARK_OAK_STAIRS, CraftStairs.class),
	IRON_TRAPDOOR(Material.IRON_TRAPDOOR, CraftTrapDoor.class),
	STANDING_BANNER(Material.STANDING_BANNER, CraftBanner.class),
	WALL_BANNER(Material.WALL_BANNER, CraftBanner.class),
	RED_SANDSTONE_STAIRS(Material.RED_SANDSTONE_STAIRS, CraftStairs.class),
	SPRUCE_FENCE_GATE(Material.SPRUCE_FENCE_GATE, CraftGate.class),
	BIRCH_FENCE_GATE(Material.BIRCH_FENCE_GATE, CraftGate.class),
	JUNGLE_FENCE_GATE(Material.JUNGLE_FENCE_GATE, CraftGate.class),
	DARK_OAK_FENCE_GATE(Material.DARK_OAK_FENCE_GATE, CraftGate.class),
	ACACIA_FENCE_GATE(Material.ACACIA_FENCE_GATE, CraftGate.class),
	SPRUCE_DOOR(Material.SPRUCE_DOOR, CraftDoor.class),
	BIRCH_DOOR(Material.BIRCH_DOOR, CraftDoor.class),
	JUNGLE_DOOR(Material.JUNGLE_DOOR, CraftDoor.class),
	ACACIA_DOOR(Material.ACACIA_DOOR, CraftDoor.class),
	DARK_OAK_DOOR(Material.DARK_OAK_DOOR, CraftDoor.class),
	PURPUR_STAIRS(Material.PURPUR_STAIRS, CraftStairs.class),
	BEETROOT_BLOCK(Material.BEETROOT_BLOCK, CraftCrops.class),
	COMMAND_REPEATING(Material.COMMAND_REPEATING, CraftCommand.class),
	COMMAND_CHAIN(Material.COMMAND_CHAIN, CraftCommand.class),
	COAL(Material.COAL, CraftCoal.class),
	INK_SACK(Material.INK_SACK, CraftDye.class),
	MONSTER_EGG(Material.MONSTER_EGG, CraftSpawnEgg.class);

	private final Material material;
	private final Class<? extends CraftMaterialData> data;

	CraftMaterial(Material material, Class<? extends CraftMaterialData> data) {
		this.material = material;
		this.data = data;
	}

	public Class<? extends CraftMaterialData> getData() {
		return this.data;
	}

	public static Class<? extends CraftMaterialData> getDataByMaterial(Material material) {
		for (CraftMaterial craftMaterial : values()) {
			if (craftMaterial.getMaterial() == material)
				return craftMaterial.getData();
		}

		return CraftMaterialData.class;
	}

	public Material getMaterial() {
		return this.material;
	}

}