package net.netcoding.nifty.common.minecraft.material;

import com.google.common.base.Preconditions;
import net.netcoding.nifty.core.util.NumberUtil;
import net.netcoding.nifty.core.util.StringUtil;
import net.netcoding.nifty.core.util.concurrent.Concurrent;
import net.netcoding.nifty.core.util.concurrent.ConcurrentMap;

public enum Material {

	AIR(0, 0),
	STONE(1),
	GRASS(2),
	DIRT(3),
	COBBLESTONE(4),
	WOOD(5, Wood.class),
	SAPLING(6, Sapling.class),
	BEDROCK(7),
	WATER(8, MaterialData.class),
	STATIONARY_WATER(9, MaterialData.class),
	LAVA(10, MaterialData.class),
	STATIONARY_LAVA(11, MaterialData.class),
	SAND(12),
	GRAVEL(13),
	GOLD_ORE(14),
	IRON_ORE(15),
	COAL_ORE(16),
	LOG(17, Tree.class),
	LEAVES(18, Leaves.class),
	SPONGE(19),
	GLASS(20),
	LAPIS_ORE(21),
	LAPIS_BLOCK(22),
	DISPENSER(23, Dispenser.class),
	SANDSTONE(24, Sandstone.class),
	NOTE_BLOCK(25),
	BED_BLOCK(26, Bed.class),
	POWERED_RAIL(27, PoweredRail.class),
	DETECTOR_RAIL(28, DetectorRail.class),
	PISTON_STICKY_BASE(29, PistonBaseMaterial.class),
	WEB(30),
	LONG_GRASS(31, LongGrass.class),
	DEAD_BUSH(32),
	PISTON_BASE(33, PistonBaseMaterial.class),
	PISTON_EXTENSION(34, PistonExtensionMaterial.class),
	WOOL(35, Wool.class),
	PISTON_MOVING_PIECE(36),
	YELLOW_FLOWER(37),
	RED_ROSE(38),
	BROWN_MUSHROOM(39),
	RED_MUSHROOM(40),
	GOLD_BLOCK(41),
	IRON_BLOCK(42),
	DOUBLE_STEP(43, Step.class),
	STEP(44, Step.class),
	BRICK(45),
	TNT(46),
	BOOKSHELF(47),
	MOSSY_COBBLESTONE(48),
	OBSIDIAN(49),
	TORCH(50, Torch.class),
	FIRE(51),
	MOB_SPAWNER(52),
	WOOD_STAIRS(53, Stairs.class),
	CHEST(54, Chest.class),
	REDSTONE_WIRE(55, RedstoneWire.class),
	DIAMOND_ORE(56),
	DIAMOND_BLOCK(57),
	WORKBENCH(58),
	CROPS(59, Crops.class),
	SOIL(60, MaterialData.class),
	FURNACE(61, Furnace.class),
	BURNING_FURNACE(62, Furnace.class),
	SIGN_POST(63, 64, Sign.class),
	WOODEN_DOOR(64, Door.class),
	LADDER(65, Ladder.class),
	RAILS(66, Rails.class),
	COBBLESTONE_STAIRS(67, Stairs.class),
	WALL_SIGN(68, 64, Sign.class),
	LEVER(69, Lever.class),
	STONE_PLATE(70, PressurePlate.class),
	IRON_DOOR_BLOCK(71, Door.class),
	WOOD_PLATE(72, PressurePlate.class),
	REDSTONE_ORE(73),
	GLOWING_REDSTONE_ORE(74),
	REDSTONE_TORCH_OFF(75, RedstoneTorch.class),
	REDSTONE_TORCH_ON(76, RedstoneTorch.class),
	STONE_BUTTON(77, Button.class),
	SNOW(78),
	ICE(79),
	SNOW_BLOCK(80),
	CACTUS(81, MaterialData.class),
	CLAY(82),
	SUGAR_CANE_BLOCK(83, MaterialData.class),
	JUKEBOX(84),
	FENCE(85),
	PUMPKIN(86, Pumpkin.class),
	NETHERRACK(87),
	SOUL_SAND(88),
	GLOWSTONE(89),
	PORTAL(90),
	JACK_O_LANTERN(91, Pumpkin.class),
	CAKE_BLOCK(92, 64, Cake.class),
	DIODE_BLOCK_OFF(93, Diode.class),
	DIODE_BLOCK_ON(94, Diode.class),
	STAINED_GLASS(95),
	TRAP_DOOR(96, TrapDoor.class),
	MONSTER_EGGS(97, MonsterEggs.class),
	SMOOTH_BRICK(98, SmoothBrick.class),
	HUGE_MUSHROOM_1(99, Mushroom.class),
	HUGE_MUSHROOM_2(100, Mushroom.class),
	IRON_FENCE(101),
	THIN_GLASS(102),
	MELON_BLOCK(103),
	PUMPKIN_STEM(104, MaterialData.class),
	MELON_STEM(105, MaterialData.class),
	VINE(106, Vine.class),
	FENCE_GATE(107, Gate.class),
	BRICK_STAIRS(108, Stairs.class),
	SMOOTH_STAIRS(109, Stairs.class),
	MYCEL(110),
	WATER_LILY(111),
	NETHER_BRICK(112),
	NETHER_FENCE(113),
	NETHER_BRICK_STAIRS(114, Stairs.class),
	NETHER_WARTS(115, NetherWarts.class),
	ENCHANTMENT_TABLE(116),
	BREWING_STAND(117, MaterialData.class),
	CAULDRON(118, Cauldron.class),
	ENDER_PORTAL(119),
	ENDER_PORTAL_FRAME(120),
	ENDER_STONE(121),
	DRAGON_EGG(122),
	REDSTONE_LAMP_OFF(123),
	REDSTONE_LAMP_ON(124),
	WOOD_DOUBLE_STEP(125, Wood.class),
	WOOD_STEP(126, WoodenStep.class),
	COCOA(127, CocoaPlant.class),
	SANDSTONE_STAIRS(128, Stairs.class),
	EMERALD_ORE(129),
	ENDER_CHEST(130, EnderChest.class),
	TRIPWIRE_HOOK(131, TripwireHook.class),
	TRIPWIRE(132, Tripwire.class),
	EMERALD_BLOCK(133),
	SPRUCE_WOOD_STAIRS(134, Stairs.class),
	BIRCH_WOOD_STAIRS(135, Stairs.class),
	JUNGLE_WOOD_STAIRS(136, Stairs.class),
	COMMAND(137, Command.class),
	BEACON(138),
	COBBLE_WALL(139),
	FLOWER_POT(140, FlowerPot.class),
	CARROT(141, Crops.class),
	POTATO(142, Crops.class),
	WOOD_BUTTON(143, Button.class),
	SKULL(144, Skull.class),
	ANVIL(145),
	TRAPPED_CHEST(146, Chest.class),
	GOLD_PLATE(147),
	IRON_PLATE(148),
	REDSTONE_COMPARATOR_OFF(149, Comparator.class),
	REDSTONE_COMPARATOR_ON(150, Comparator.class),
	DAYLIGHT_DETECTOR(151),
	REDSTONE_BLOCK(152),
	QUARTZ_ORE(153),
	HOPPER(154, Hopper.class),
	QUARTZ_BLOCK(155),
	QUARTZ_STAIRS(156, Stairs.class),
	ACTIVATOR_RAIL(157, PoweredRail.class),
	DROPPER(158, Dispenser.class),
	STAINED_CLAY(159),
	STAINED_GLASS_PANE(160),
	LEAVES_2(161, Leaves.class),
	LOG_2(162, Tree.class),
	ACACIA_STAIRS(163, Stairs.class),
	DARK_OAK_STAIRS(164, Stairs.class),
	SLIME_BLOCK(165),
	BARRIER(166),
	IRON_TRAPDOOR(167, TrapDoor.class),
	PRISMARINE(168),
	SEA_LANTERN(169),
	HAY_BLOCK(170),
	CARPET(171),
	HARD_CLAY(172),
	COAL_BLOCK(173),
	PACKED_ICE(174),
	DOUBLE_PLANT(175),
	STANDING_BANNER(176, Banner.class),
	WALL_BANNER(177, Banner.class),
	DAYLIGHT_DETECTOR_INVERTED(178),
	RED_SANDSTONE(179),
	RED_SANDSTONE_STAIRS(180, Stairs.class),
	DOUBLE_STONE_SLAB2(181),
	STONE_SLAB2(182),
	SPRUCE_FENCE_GATE(183, Gate.class),
	BIRCH_FENCE_GATE(184, Gate.class),
	JUNGLE_FENCE_GATE(185, Gate.class),
	DARK_OAK_FENCE_GATE(186, Gate.class),
	ACACIA_FENCE_GATE(187, Gate.class),
	SPRUCE_FENCE(188),
	BIRCH_FENCE(189),
	JUNGLE_FENCE(190),
	DARK_OAK_FENCE(191),
	ACACIA_FENCE(192),
	SPRUCE_DOOR(193, Door.class),
	BIRCH_DOOR(194, Door.class),
	JUNGLE_DOOR(195, Door.class),
	ACACIA_DOOR(196, Door.class),
	DARK_OAK_DOOR(197, Door.class),
	END_ROD(198),
	CHORUS_PLANT(199),
	CHORUS_FLOWER(200),
	PURPUR_BLOCK(201),
	PURPUR_PILLAR(202),
	PURPUR_STAIRS(203, Stairs.class),
	PURPUR_DOUBLE_SLAB(204),
	PURPUR_SLAB(205),
	END_BRICKS(206),
	BEETROOT_BLOCK(207, Crops.class),
	GRASS_PATH(208),
	END_GATEWAY(209),
	COMMAND_REPEATING(210, Command.class),
	COMMAND_CHAIN(211, Command.class),
	FROSTED_ICE(212),
	STRUCTURE_BLOCK(255),
	IRON_SPADE(256, 1, 250),
	IRON_PICKAXE(257, 1, 250),
	IRON_AXE(258, 1, 250),
	FLINT_AND_STEEL(259, 1, 64),
	APPLE(260),
	BOW(261, 1, 384),
	ARROW(262),
	COAL(263, Coal.class),
	DIAMOND(264),
	IRON_INGOT(265),
	GOLD_INGOT(266),
	IRON_SWORD(267, 1, 250),
	WOOD_SWORD(268, 1, 59),
	WOOD_SPADE(269, 1, 59),
	WOOD_PICKAXE(270, 1, 59),
	WOOD_AXE(271, 1, 59),
	STONE_SWORD(272, 1, 131),
	STONE_SPADE(273, 1, 131),
	STONE_PICKAXE(274, 1, 131),
	STONE_AXE(275, 1, 131),
	DIAMOND_SWORD(276, 1, 1561),
	DIAMOND_SPADE(277, 1, 1561),
	DIAMOND_PICKAXE(278, 1, 1561),
	DIAMOND_AXE(279, 1, 1561),
	STICK(280),
	BOWL(281),
	MUSHROOM_SOUP(282, 1),
	GOLD_SWORD(283, 1, 32),
	GOLD_SPADE(284, 1, 32),
	GOLD_PICKAXE(285, 1, 32),
	GOLD_AXE(286, 1, 32),
	STRING(287),
	FEATHER(288),
	SULPHUR(289),
	WOOD_HOE(290, 1, 59),
	STONE_HOE(291, 1, 131),
	IRON_HOE(292, 1, 250),
	DIAMOND_HOE(293, 1, 1561),
	GOLD_HOE(294, 1, 32),
	SEEDS(295),
	WHEAT(296),
	BREAD(297),
	LEATHER_HELMET(298, 1, 55),
	LEATHER_CHESTPLATE(299, 1, 80),
	LEATHER_LEGGINGS(300, 1, 75),
	LEATHER_BOOTS(301, 1, 65),
	CHAINMAIL_HELMET(302, 1, 165),
	CHAINMAIL_CHESTPLATE(303, 1, 240),
	CHAINMAIL_LEGGINGS(304, 1, 225),
	CHAINMAIL_BOOTS(305, 1, 195),
	IRON_HELMET(306, 1, 165),
	IRON_CHESTPLATE(307, 1, 240),
	IRON_LEGGINGS(308, 1, 225),
	IRON_BOOTS(309, 1, 195),
	DIAMOND_HELMET(310, 1, 363),
	DIAMOND_CHESTPLATE(311, 1, 528),
	DIAMOND_LEGGINGS(312, 1, 495),
	DIAMOND_BOOTS(313, 1, 429),
	GOLD_HELMET(314, 1, 77),
	GOLD_CHESTPLATE(315, 1, 112),
	GOLD_LEGGINGS(316, 1, 105),
	GOLD_BOOTS(317, 1, 91),
	FLINT(318),
	PORK(319),
	GRILLED_PORK(320),
	PAINTING(321),
	GOLDEN_APPLE(322),
	SIGN(323, 16),
	WOOD_DOOR(324, 64),
	BUCKET(325, 16),
	WATER_BUCKET(326, 1),
	LAVA_BUCKET(327, 1),
	MINECART(328, 1),
	SADDLE(329, 1),
	IRON_DOOR(330, 64),
	REDSTONE(331),
	SNOW_BALL(332, 16),
	BOAT(333, 1),
	LEATHER(334),
	MILK_BUCKET(335, 1),
	CLAY_BRICK(336),
	CLAY_BALL(337),
	SUGAR_CANE(338),
	PAPER(339),
	BOOK(340),
	SLIME_BALL(341),
	STORAGE_MINECART(342, 1),
	POWERED_MINECART(343, 1),
	EGG(344, 16),
	COMPASS(345),
	FISHING_ROD(346, 1, 64),
	WATCH(347),
	GLOWSTONE_DUST(348),
	RAW_FISH(349),
	COOKED_FISH(350),
	INK_SACK(351, Dye.class),
	BONE(352),
	SUGAR(353),
	CAKE(354, 1),
	BED(355, 1),
	DIODE(356),
	COOKIE(357),
	MAP(358, MaterialData.class),
	SHEARS(359, 1, 238),
	MELON(360),
	PUMPKIN_SEEDS(361),
	MELON_SEEDS(362),
	RAW_BEEF(363),
	COOKED_BEEF(364),
	RAW_CHICKEN(365),
	COOKED_CHICKEN(366),
	ROTTEN_FLESH(367),
	ENDER_PEARL(368, 16),
	BLAZE_ROD(369),
	GHAST_TEAR(370),
	GOLD_NUGGET(371),
	NETHER_STALK(372),
	POTION(373, 1, MaterialData.class),
	GLASS_BOTTLE(374),
	SPIDER_EYE(375),
	FERMENTED_SPIDER_EYE(376),
	BLAZE_POWDER(377),
	MAGMA_CREAM(378),
	BREWING_STAND_ITEM(379),
	CAULDRON_ITEM(380),
	EYE_OF_ENDER(381),
	SPECKLED_MELON(382),
	MONSTER_EGG(383, 64, SpawnEgg.class),
	EXP_BOTTLE(384, 64),
	FIREBALL(385, 64),
	BOOK_AND_QUILL(386, 1),
	WRITTEN_BOOK(387, 16),
	EMERALD(388, 64),
	ITEM_FRAME(389),
	FLOWER_POT_ITEM(390),
	CARROT_ITEM(391),
	POTATO_ITEM(392),
	BAKED_POTATO(393),
	POISONOUS_POTATO(394),
	EMPTY_MAP(395),
	GOLDEN_CARROT(396),
	SKULL_ITEM(397),
	CARROT_STICK(398, 1, 25),
	NETHER_STAR(399),
	PUMPKIN_PIE(400),
	FIREWORK(401),
	FIREWORK_CHARGE(402),
	ENCHANTED_BOOK(403, 1),
	REDSTONE_COMPARATOR(404),
	NETHER_BRICK_ITEM(405),
	QUARTZ(406),
	EXPLOSIVE_MINECART(407, 1),
	HOPPER_MINECART(408, 1),
	PRISMARINE_SHARD(409),
	PRISMARINE_CRYSTALS(410),
	RABBIT(411),
	COOKED_RABBIT(412),
	RABBIT_STEW(413, 1),
	RABBIT_FOOT(414),
	RABBIT_HIDE(415),
	ARMOR_STAND(416, 16),
	IRON_BARDING(417, 1),
	GOLD_BARDING(418, 1),
	DIAMOND_BARDING(419, 1),
	LEASH(420),
	NAME_TAG(421),
	COMMAND_MINECART(422, 1),
	MUTTON(423),
	COOKED_MUTTON(424),
	BANNER(425, 16),
	END_CRYSTAL(426),
	SPRUCE_DOOR_ITEM(427),
	BIRCH_DOOR_ITEM(428),
	JUNGLE_DOOR_ITEM(429),
	ACACIA_DOOR_ITEM(430),
	DARK_OAK_DOOR_ITEM(431),
	CHORUS_FRUIT(432),
	CHORUS_FRUIT_POPPED(433),
	BEETROOT(434),
	BEETROOT_SEEDS(435),
	BEETROOT_SOUP(436, 1),
	DRAGONS_BREATH(437),
	SPLASH_POTION(438, 1),
	SPECTRAL_ARROW(439),
	TIPPED_ARROW(440),
	LINGERING_POTION(441, 1),
	SHIELD(442, 1, 336),
	ELYTRA(443, 1, 431),
	BOAT_SPRUCE(444, 1),
	BOAT_BIRCH(445, 1),
	BOAT_JUNGLE(446, 1),
	BOAT_ACACIA(447, 1),
	BOAT_DARK_OAK(448, 1),
	GOLD_RECORD(2256, 1),
	GREEN_RECORD(2257, 1),
	RECORD_3(2258, 1),
	RECORD_4(2259, 1),
	RECORD_5(2260, 1),
	RECORD_6(2261, 1),
	RECORD_7(2262, 1),
	RECORD_8(2263, 1),
	RECORD_9(2264, 1),
	RECORD_10(2265, 1),
	RECORD_11(2266, 1),
	RECORD_12(2267, 1);

	private static final ConcurrentMap<Integer, Material> BY_ID = Concurrent.newMap();
	private static final ConcurrentMap<String, Material> BY_NAME = Concurrent.newMap();
	private final int id;
	private final int maxStack;
	private final short durability;
	private final Class<? extends MaterialData> ctor;

	static {
		for (Material material : Material.values()) {
			BY_ID.put(material.getId(), material);
			BY_NAME.put(material.name(), material);
		}
	}

	Material(int id) {
		this(id, 64);
	}

	Material(int id, int maxStack) {
		this(id, maxStack, MaterialData.class);
	}

	Material(int id, int maxStack, int durability) {
		this(id, maxStack, durability, MaterialData.class);
	}

	Material(int id, Class<? extends MaterialData> data) {
		this(id, 64, data);
	}

	Material(int id, int maxStack, Class<? extends MaterialData> data) {
		this(id, maxStack, 0, data);
	}

	Material(int id, int maxStack, int durability, Class<? extends MaterialData> data) {
		this.id = id;
		this.durability = (short)durability;
		this.maxStack = maxStack;
		this.ctor = data;
	}

	/**
	 * Gets the item ID or block ID of this Material
	 *
	 * @return ID of this material
	 */
	public final int getId() {
		return this.id;
	}

	/**
	 * Gets the maximum amount of this material that can be held in a stack
	 *
	 * @return Maximum stack size for this material
	 */
	public final int getMaxStackSize() {
		return this.maxStack;
	}

	/**
	 * Gets the maximum durability of this material
	 *
	 * @return Maximum durability for this material
	 */
	public final short getMaxDurability() {
		return this.durability;
	}

	/**
	 * Gets the MaterialData class associated with this Material
	 *
	 * @return MaterialData associated with this Material
	 */
	public final Class<? extends MaterialData> getData() {
		return this.ctor;
	}

	/**
	 * Checks if this Material is a placable block
	 *
	 * @return true if this material is a block
	 */
	public boolean isBlock() {
		return this.getId() < 256;
	}

	/**
	 * Checks if this Material is edible.
	 *
	 * @return true if this Material is edible.
	 */
	public boolean isEdible() {
		switch (this) {
			case BREAD:
			case CARROT_ITEM:
			case BAKED_POTATO:
			case POTATO_ITEM:
			case POISONOUS_POTATO:
			case GOLDEN_CARROT:
			case PUMPKIN_PIE:
			case COOKIE:
			case MELON:
			case MUSHROOM_SOUP:
			case RAW_CHICKEN:
			case COOKED_CHICKEN:
			case RAW_BEEF:
			case COOKED_BEEF:
			case RAW_FISH:
			case COOKED_FISH:
			case PORK:
			case GRILLED_PORK:
			case APPLE:
			case GOLDEN_APPLE:
			case ROTTEN_FLESH:
			case SPIDER_EYE:
			case RABBIT:
			case COOKED_RABBIT:
			case RABBIT_STEW:
			case MUTTON:
			case COOKED_MUTTON:
			case BEETROOT:
			case CHORUS_FRUIT:
			case BEETROOT_SOUP:
				return true;
			default:
				return false;
		}
	}

	/**
	 * Attempts to get the Material with the given ID
	 *
	 * @param id ID of the material to get
	 * @return Material if found, or null
	 */
	public static Material getMaterial(int id) {
		return id >= 0 && BY_ID.containsKey(id) ? BY_ID.get(id) : null;
	}

	/**
	 * Attempts to get the Material with the given name.
	 * <p>
	 * This is a normal lookup, names must be the precise name they are given
	 * in the enum.
	 *
	 * @param name Name of the material to get
	 * @return Material if found, or null
	 */
	public static Material getMaterial(String name) {
		Preconditions.checkArgument(StringUtil.notEmpty(name), "Name cannot be NULL!");
		return BY_NAME.get(name);
	}

	/**
	 * Attempts to match the Material with the given name.
	 * <p>
	 * This is a match lookup; names will be converted to uppercase, then
	 * stripped of special characters in an attempt to format it like the
	 * enum.
	 * <p>
	 * Using this for match by ID is deprecated.
	 *
	 * @param name Name of the material to get
	 * @return Material if found, or null
	 */
	public static Material matchMaterial(String name) {
		Preconditions.checkArgument(StringUtil.notEmpty(name), "Name cannot be NULL!");
		Material result = null;

		if (NumberUtil.isNumber(name))
			result = getMaterial(Integer.parseInt(name));

		if (result == null) {
			String filtered = name.toUpperCase();
			filtered = filtered.replaceAll("\\s+", "_").replaceAll("\\W", "");
			result = BY_NAME.get(filtered);
		}

		return result;
	}

	/**
	 * @return True if this material represents a playable music disk.
	 */
	public boolean isRecord() {
		return this.getId() >= GOLD_RECORD.getId() && this.getId() <= RECORD_12.getId();
	}

	/**
	 * Check if the material is a block and solid (cannot be passed through by
	 * a player)
	 *
	 * @return True if this material is a block and solid
	 */
	public boolean isSolid() {
		if (!this.isBlock() || this.getId() == 0)
			return false;

		switch (this) {
			case STONE:
			case GRASS:
			case DIRT:
			case COBBLESTONE:
			case WOOD:
			case BEDROCK:
			case SAND:
			case GRAVEL:
			case GOLD_ORE:
			case IRON_ORE:
			case COAL_ORE:
			case LOG:
			case LEAVES:
			case SPONGE:
			case GLASS:
			case LAPIS_ORE:
			case LAPIS_BLOCK:
			case DISPENSER:
			case SANDSTONE:
			case NOTE_BLOCK:
			case BED_BLOCK:
			case PISTON_STICKY_BASE:
			case PISTON_BASE:
			case PISTON_EXTENSION:
			case WOOL:
			case PISTON_MOVING_PIECE:
			case GOLD_BLOCK:
			case IRON_BLOCK:
			case DOUBLE_STEP:
			case STEP:
			case BRICK:
			case TNT:
			case BOOKSHELF:
			case MOSSY_COBBLESTONE:
			case OBSIDIAN:
			case MOB_SPAWNER:
			case WOOD_STAIRS:
			case CHEST:
			case DIAMOND_ORE:
			case DIAMOND_BLOCK:
			case WORKBENCH:
			case SOIL:
			case FURNACE:
			case BURNING_FURNACE:
			case SIGN_POST:
			case WOODEN_DOOR:
			case COBBLESTONE_STAIRS:
			case WALL_SIGN:
			case STONE_PLATE:
			case IRON_DOOR_BLOCK:
			case WOOD_PLATE:
			case REDSTONE_ORE:
			case GLOWING_REDSTONE_ORE:
			case ICE:
			case SNOW_BLOCK:
			case CACTUS:
			case CLAY:
			case JUKEBOX:
			case FENCE:
			case PUMPKIN:
			case NETHERRACK:
			case SOUL_SAND:
			case GLOWSTONE:
			case JACK_O_LANTERN:
			case CAKE_BLOCK:
			case STAINED_GLASS:
			case TRAP_DOOR:
			case MONSTER_EGGS:
			case SMOOTH_BRICK:
			case HUGE_MUSHROOM_1:
			case HUGE_MUSHROOM_2:
			case IRON_FENCE:
			case THIN_GLASS:
			case MELON_BLOCK:
			case FENCE_GATE:
			case BRICK_STAIRS:
			case SMOOTH_STAIRS:
			case MYCEL:
			case NETHER_BRICK:
			case NETHER_FENCE:
			case NETHER_BRICK_STAIRS:
			case ENCHANTMENT_TABLE:
			case BREWING_STAND:
			case CAULDRON:
			case ENDER_PORTAL_FRAME:
			case ENDER_STONE:
			case DRAGON_EGG:
			case REDSTONE_LAMP_OFF:
			case REDSTONE_LAMP_ON:
			case WOOD_DOUBLE_STEP:
			case WOOD_STEP:
			case SANDSTONE_STAIRS:
			case EMERALD_ORE:
			case ENDER_CHEST:
			case EMERALD_BLOCK:
			case SPRUCE_WOOD_STAIRS:
			case BIRCH_WOOD_STAIRS:
			case JUNGLE_WOOD_STAIRS:
			case COMMAND:
			case BEACON:
			case COBBLE_WALL:
			case ANVIL:
			case TRAPPED_CHEST:
			case GOLD_PLATE:
			case IRON_PLATE:
			case DAYLIGHT_DETECTOR:
			case REDSTONE_BLOCK:
			case QUARTZ_ORE:
			case HOPPER:
			case QUARTZ_BLOCK:
			case QUARTZ_STAIRS:
			case DROPPER:
			case STAINED_CLAY:
			case HAY_BLOCK:
			case HARD_CLAY:
			case COAL_BLOCK:
			case STAINED_GLASS_PANE:
			case LEAVES_2:
			case LOG_2:
			case ACACIA_STAIRS:
			case DARK_OAK_STAIRS:
			case PACKED_ICE:
			case RED_SANDSTONE:
			case SLIME_BLOCK:
			case BARRIER:
			case IRON_TRAPDOOR:
			case PRISMARINE:
			case SEA_LANTERN:
			case DOUBLE_STONE_SLAB2:
			case RED_SANDSTONE_STAIRS:
			case STONE_SLAB2:
			case SPRUCE_FENCE_GATE:
			case BIRCH_FENCE_GATE:
			case JUNGLE_FENCE_GATE:
			case DARK_OAK_FENCE_GATE:
			case ACACIA_FENCE_GATE:
			case SPRUCE_FENCE:
			case BIRCH_FENCE:
			case JUNGLE_FENCE:
			case DARK_OAK_FENCE:
			case ACACIA_FENCE:
			case STANDING_BANNER:
			case WALL_BANNER:
			case DAYLIGHT_DETECTOR_INVERTED:
			case SPRUCE_DOOR:
			case BIRCH_DOOR:
			case JUNGLE_DOOR:
			case ACACIA_DOOR:
			case DARK_OAK_DOOR:
			case PURPUR_BLOCK:
			case PURPUR_PILLAR:
			case PURPUR_STAIRS:
			case PURPUR_DOUBLE_SLAB:
			case PURPUR_SLAB:
			case END_BRICKS:
			case GRASS_PATH:
			case STRUCTURE_BLOCK:
			case COMMAND_REPEATING:
			case COMMAND_CHAIN:
			case FROSTED_ICE:
				return true;
			default:
				return false;
		}
	}

	/**
	 * Check if the material is a block and does not block any light
	 *
	 * @return True if this material is a block and does not block any light
	 */
	public boolean isTransparent() {
		if (!this.isBlock())
			return false;

		switch (this) {
			case AIR:
			case SAPLING:
			case POWERED_RAIL:
			case DETECTOR_RAIL:
			case LONG_GRASS:
			case DEAD_BUSH:
			case YELLOW_FLOWER:
			case RED_ROSE:
			case BROWN_MUSHROOM:
			case RED_MUSHROOM:
			case TORCH:
			case FIRE:
			case REDSTONE_WIRE:
			case CROPS:
			case LADDER:
			case RAILS:
			case LEVER:
			case REDSTONE_TORCH_OFF:
			case REDSTONE_TORCH_ON:
			case STONE_BUTTON:
			case SNOW:
			case SUGAR_CANE_BLOCK:
			case PORTAL:
			case DIODE_BLOCK_OFF:
			case DIODE_BLOCK_ON:
			case PUMPKIN_STEM:
			case MELON_STEM:
			case VINE:
			case WATER_LILY:
			case NETHER_WARTS:
			case ENDER_PORTAL:
			case COCOA:
			case TRIPWIRE_HOOK:
			case TRIPWIRE:
			case FLOWER_POT:
			case CARROT:
			case POTATO:
			case WOOD_BUTTON:
			case SKULL:
			case REDSTONE_COMPARATOR_OFF:
			case REDSTONE_COMPARATOR_ON:
			case ACTIVATOR_RAIL:
			case CARPET:
			case DOUBLE_PLANT:
			case END_ROD:
			case CHORUS_PLANT:
			case CHORUS_FLOWER:
			case BEETROOT_BLOCK:
			case END_GATEWAY:
				return true;
			default:
				return false;
		}
	}

	/**
	 * Check if the material is a block and can catch fire
	 *
	 * @return True if this material is a block and can catch fire
	 */
	public boolean isFlammable() {
		if (!this.isBlock())
			return false;

		switch (this) {
			case WOOD:
			case LOG:
			case LEAVES:
			case NOTE_BLOCK:
			case BED_BLOCK:
			case LONG_GRASS:
			case DEAD_BUSH:
			case WOOL:
			case TNT:
			case BOOKSHELF:
			case WOOD_STAIRS:
			case CHEST:
			case WORKBENCH:
			case SIGN_POST:
			case WOODEN_DOOR:
			case WALL_SIGN:
			case WOOD_PLATE:
			case JUKEBOX:
			case FENCE:
			case TRAP_DOOR:
			case HUGE_MUSHROOM_1:
			case HUGE_MUSHROOM_2:
			case VINE:
			case FENCE_GATE:
			case WOOD_DOUBLE_STEP:
			case WOOD_STEP:
			case SPRUCE_WOOD_STAIRS:
			case BIRCH_WOOD_STAIRS:
			case JUNGLE_WOOD_STAIRS:
			case TRAPPED_CHEST:
			case DAYLIGHT_DETECTOR:
			case CARPET:
			case LEAVES_2:
			case LOG_2:
			case ACACIA_STAIRS:
			case DARK_OAK_STAIRS:
			case DOUBLE_PLANT:
			case SPRUCE_FENCE_GATE:
			case BIRCH_FENCE_GATE:
			case JUNGLE_FENCE_GATE:
			case DARK_OAK_FENCE_GATE:
			case ACACIA_FENCE_GATE:
			case SPRUCE_FENCE:
			case BIRCH_FENCE:
			case JUNGLE_FENCE:
			case DARK_OAK_FENCE:
			case ACACIA_FENCE:
			case STANDING_BANNER:
			case WALL_BANNER:
			case DAYLIGHT_DETECTOR_INVERTED:
			case SPRUCE_DOOR:
			case BIRCH_DOOR:
			case JUNGLE_DOOR:
			case ACACIA_DOOR:
			case DARK_OAK_DOOR:
				return true;
			default:
				return false;
		}
	}

	/**
	 * Check if the material is a block and can burn away
	 *
	 * @return True if this material is a block and can burn away
	 */
	public boolean isBurnable() {
		if (!this.isBlock())
			return false;

		switch (this) {
			case WOOD:
			case LOG:
			case LEAVES:
			case LONG_GRASS:
			case WOOL:
			case YELLOW_FLOWER:
			case RED_ROSE:
			case TNT:
			case BOOKSHELF:
			case WOOD_STAIRS:
			case FENCE:
			case VINE:
			case WOOD_DOUBLE_STEP:
			case WOOD_STEP:
			case SPRUCE_WOOD_STAIRS:
			case BIRCH_WOOD_STAIRS:
			case JUNGLE_WOOD_STAIRS:
			case HAY_BLOCK:
			case COAL_BLOCK:
			case LEAVES_2:
			case LOG_2:
			case CARPET:
			case DOUBLE_PLANT:
			case DEAD_BUSH:
			case FENCE_GATE:
			case SPRUCE_FENCE_GATE:
			case BIRCH_FENCE_GATE:
			case JUNGLE_FENCE_GATE:
			case DARK_OAK_FENCE_GATE:
			case ACACIA_FENCE_GATE:
			case SPRUCE_FENCE:
			case BIRCH_FENCE:
			case JUNGLE_FENCE:
			case DARK_OAK_FENCE:
			case ACACIA_FENCE:
			case ACACIA_STAIRS:
			case DARK_OAK_STAIRS:
				return true;
			default:
				return false;
		}
	}

	/**
	 * Check if the material is a block and completely blocks vision
	 *
	 * @return True if this material is a block and completely blocks vision
	 */
	public boolean isOccluding() {
		if (!this.isBlock())
			return false;

		switch (this) {
			case STONE:
			case GRASS:
			case DIRT:
			case COBBLESTONE:
			case WOOD:
			case BEDROCK:
			case SAND:
			case GRAVEL:
			case GOLD_ORE:
			case IRON_ORE:
			case COAL_ORE:
			case LOG:
			case SPONGE:
			case LAPIS_ORE:
			case LAPIS_BLOCK:
			case DISPENSER:
			case SANDSTONE:
			case NOTE_BLOCK:
			case WOOL:
			case GOLD_BLOCK:
			case IRON_BLOCK:
			case DOUBLE_STEP:
			case BRICK:
			case BOOKSHELF:
			case MOSSY_COBBLESTONE:
			case OBSIDIAN:
			case MOB_SPAWNER:
			case DIAMOND_ORE:
			case DIAMOND_BLOCK:
			case WORKBENCH:
			case FURNACE:
			case BURNING_FURNACE:
			case REDSTONE_ORE:
			case GLOWING_REDSTONE_ORE:
			case SNOW_BLOCK:
			case CLAY:
			case JUKEBOX:
			case PUMPKIN:
			case NETHERRACK:
			case SOUL_SAND:
			case JACK_O_LANTERN:
			case MONSTER_EGGS:
			case SMOOTH_BRICK:
			case HUGE_MUSHROOM_1:
			case HUGE_MUSHROOM_2:
			case MELON_BLOCK:
			case MYCEL:
			case NETHER_BRICK:
			case ENDER_STONE:
			case REDSTONE_LAMP_OFF:
			case REDSTONE_LAMP_ON:
			case WOOD_DOUBLE_STEP:
			case EMERALD_ORE:
			case EMERALD_BLOCK:
			case COMMAND:
			case QUARTZ_ORE:
			case QUARTZ_BLOCK:
			case DROPPER:
			case STAINED_CLAY:
			case HAY_BLOCK:
			case HARD_CLAY:
			case COAL_BLOCK:
			case LOG_2:
			case PACKED_ICE:
			case SLIME_BLOCK:
			case BARRIER:
			case PRISMARINE:
			case RED_SANDSTONE:
			case DOUBLE_STONE_SLAB2:
			case PURPUR_BLOCK:
			case PURPUR_PILLAR:
			case PURPUR_DOUBLE_SLAB:
			case END_BRICKS:
			case STRUCTURE_BLOCK:
			case COMMAND_REPEATING:
			case COMMAND_CHAIN:
				return true;
			default:
				return false;
		}
	}

	/**
	 * @return True if this material is affected by gravity.
	 */
	public boolean hasGravity() {
		if (!this.isBlock())
			return false;

		switch (this) {
			case SAND:
			case GRAVEL:
			case ANVIL:
				return true;
			default:
				return false;
		}
	}

}