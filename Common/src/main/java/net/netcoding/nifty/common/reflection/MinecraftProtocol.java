package net.netcoding.nifty.common.reflection;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import net.netcoding.nifty.core.NiftyCore;
import net.netcoding.nifty.core.http.HttpBody;
import net.netcoding.nifty.core.http.HttpClient;
import net.netcoding.nifty.core.http.HttpResponse;
import net.netcoding.nifty.core.reflection.Reflection;
import net.netcoding.nifty.core.util.StringUtil;

import java.net.URL;

/**
 * A list of all current and past protocols.
 * <p>
 * Allows for the fetching of new protocols.
 */
public enum MinecraftProtocol {

	// http://wiki.vg/Protocol_version_numbers
	v1_10_2(210),
	v1_10_1(210),
	v1_10(210, true),
	v1_10_pre2(205),
	v1_10_pre1(204),
	v16w21b(203),
	v16w21a(202),
	v16w20a(201),
	v1_9_4(110, true),
	v1_9_3(110),
	v1_9_3_pre2(110),
	v1_9_3_pre1(110),
	v16w15b(109),
	v16w15a(109),
	v16w14a(109),
	v1_9_2(109, true),
	v1_RV_pre1(108),
	v1_9_1(108, true),
	v1_9_1_pre3(108),
	v1_9_1_pre2(108),
	v1_9_1_pre1(107),
	v1_9(107, true),
	v1_9_pre4(106),
	v1_9_pre3(105),
	v1_9_pre2(104),
	v1_9_pre1(103),
	v16w07b(102),
	v16w07a(101),
	v16w06a(100),
	v16w05b(99),
	v16w05a(98),
	v16w04a(97),
	v16w03a(96),
	v16w02a(95),
	v15w51b(94),
	v15w51a(93),
	v15w50a(92),
	v15w49b(91),
	v15w49a(90),
	v15w47c(89),
	v15w47b(88),
	v15w47a(87),
	v15w46a(86),
	v15w45a(85),
	v15w44b(84),
	v15w44a(83),
	v15w43c(82),
	v15w43b(81),
	v15w43a(80),
	v15w42a(79),
	v15w41b(78),
	v15w41a(77),
	v15w40b(76),
	v15w40a(75),
	v15w39c(74),
	v15w38b(73),
	v15w38a(72),
	v15w37a(71),
	v15w36d(70),
	v15w36c(69),
	v15w36b(68),
	v15w36a(67),
	v15w35e(66),
	v15w35d(65),
	v15w35c(64),
	v15w35b(63),
	v15w35a(62),
	v15w34d(61),
	v15w34c(60),
	v15w34b(59),
	v15w34a(58),
	v15w33c(57),
	v15w33b(56),
	v15w33a(55),
	v15w32c(54),
	v15w32b(53),
	v15w32a(52),
	v15w31c(51),
	v15w31b(50),
	v15w31a(49),
	v15w14a(48),
	v1_8_9(47),
	v1_8_8(47),
	v1_8_7(47),
	v1_8_6(47),
	v1_8_3(47),
	v1_8_2(47),
	v1_8_1(47),
	v1_8(47, true),
	v1_8_pre3(46),
	v1_8_pre2(45),
	v1_8_pre1(44),
	v14w34d(43),
	v14w34c(42),
	v14w34b(41),
	v14w34a(40),
	v14w33c(39),
	v14w33b(38),
	v14w33a(37),
	v14w32d(36),
	v14w32c(35),
	v14w32b(34),
	v14w32a(33),
	v14w31a(32),
	v14w30c(31),
	v14w30a(30),
	v14w29a(29),
	v14w28b(28),
	v14w28a(27),
	v14w27b(26),
	v14w27a(26),
	v14w26c(25),
	v14w26b(24),
	v14w26a(23),
	v14w25b(22),
	v14w25a(21),
	v14w21b(20),
	v14w21a(19),
	v14w20a(18),
	v14w19a(17),
	v14w18b(16),
	v14w17a(15),
	v14w11a(14),
	v14w08a(12),
	v14w07a(11),
	v14w06a(10),
	v14w05a(9),
	v14w04b(8),
	v14w04a(7),
	v14w03a(6),
	v14w02a(5),
	v1_7_10(5, true),
	v1_7_9(5),
	v1_7_8(5),
	v1_7_7(5),
	v1_7_6(5),
	v1_7_5(4),
	v1_7_4(4),
	v1_7_3_pre(4),
	v1_7_2(4),
	v1_7_1_pre(4),
	v1_6_4(78),
	v1_6_2(74),
	v1_6_1(73),
	v1_6_pre(72),
	v13w26a(72),
	v13w25c(71),
	v13w25b(71),
	v13w25a(71),
	v13w24b(70),
	v13w24a(69),
	v13w23b(68),
	v13w23a(67),
	v13w22a(67),
	v13w21b(67),
	v13w21a(67),
	v13w19a(66),
	v13w18a(65),
	v13w17a(64),
	v13w16b(63),
	v1_5_2(61),
	v1_5_1(60),
	v1_5(60, true),
	v13w09b(59),
	v13w06a(58),
	v13w05b(57),
	v13w05a(56),
	v13w04a(55),
	v13w03a(54),
	v13w02a(53),
	v13w01a(52),
	v1_4_7(51),
	v1_4_6(51),
	v12w49a(50),
	v1_4_5(49),
	v1_4_4(49),
	v1_4_3_pre(48),
	v1_4_2(47),
	v12w41a(46),
	v12w40a(45),
	v12w34b(42),
	v12w34a(41),
	v12w32a(40),
	v1_3_2(39),
	v1_3_1(39),
	v12w27a(38),
	v12w26a(37),
	v12w25a(37),
	v12w24a(36),
	v12w23a(35),
	v12w22a(34),
	v12w21b(33),
	v12w21a(33),
	v12w19a(32),
	v12w18a(32),
	v12w17a(31),
	v12w16a(30),
	v1_2_5(29),
	v1_2_4(29),
	v1_2_3(28),
	v1_2_2(28),
	v1_2_1(28),
	v12w07a(27),
	v12w06a(25),
	v12w05a(24),
	v12w04a(24),
	v12w03a(24),
	v12w02a(24),
	v12w01a(24),
	v1_1(23),
	v1_0_0(22);

	private static final boolean IS_FORGE;
	private static final boolean IS_PRE_1_8_3;
	private static final boolean IS_SPIGOT;
	private static final String SERVER_VERSION;

	static {
		boolean isForge;
		boolean isPre1_8_3 = false;
		boolean isSpigot = false;
		String serverVersion;

		if (NiftyCore.isBukkit()) {
			try {
				Reflection spigotPlayer = new Reflection("Player$Spigot", "org.bukkit.entity");
				spigotPlayer.getClazz();
				isSpigot = true;
			} catch (Exception ignore) { }

			MinecraftReflection bukkit = new MinecraftReflection("Bukkit", "org.bukkit");
			String version = bukkit.invokeMethod("getVersion", null).toString().toUpperCase();
			isForge = (version.contains("MCPC") || version.contains("FORGE") || version.contains("CAULDRON"));

			String json = bukkit.invokeMethod("getServer", null).toString().replaceAll("^[a-zA-Z0-9]+(?=\\{)", "");
			json = json.replace("=", "':'").replace(",", "','").replace("{", "{'").replace("}", "'}");
			JsonObject bukkitVersion = new JsonParser().parse(json).getAsJsonObject();
			serverVersion = bukkitVersion.get("minecraftVersion").getAsString();
		} else if (NiftyCore.isSponge()) {
			MinecraftReflection spongeVersion = new MinecraftReflection("SpongeMinecraftVersion", "common", "org.spongepowered");
			Object minecraftVersion = new MinecraftReflection("SpongeImpl", "common", "org.spongepowered").getValue(spongeVersion.getClazz(), null);
			serverVersion = spongeVersion.getValue(String.class, minecraftVersion);
			isForge = true;
		} else
			throw new UnsupportedOperationException("Unknown server type!");

		try {
			new MinecraftReflection("ChatSerializer", MinecraftPackage.MINECRAFT_SERVER).getClazz();
			isPre1_8_3 = true;
		} catch (Exception ignore) { }

		IS_FORGE = isForge;
		IS_PRE_1_8_3 = isPre1_8_3;
		IS_SPIGOT = isSpigot;
		SERVER_VERSION = serverVersion;
	}

	private final int protocol;
	private final String version;
	private final boolean rc;

	MinecraftProtocol(int protocol) {
		this(protocol, false);
	}

	MinecraftProtocol(int protocol, boolean rc) {
		this.protocol = protocol;
		this.rc = rc;
		this.version = "v1_RV_pre1".equals(name()) /* April Fools */ ? "1.9.1" : this.name().replaceAll("^v", "").replace("_pre", "-pre").replace("_", ".");
	}

	/**
	 * Gets the protocol number.
	 *
	 * @return Protocol number.
	 */
	public final int getProtocol() {
		return this.protocol;
	}

	/**
	 * Gets the protocol version.
	 *
	 * @return Protocol version.
	 */
	public final String getVersion() {
		return this.version;
	}

	public static MinecraftProtocol getCurrent() {
		int currentProtocol = getProtocol(getServerVersion());
		MinecraftProtocol first = null;

		for (MinecraftProtocol protocol : values()) {
			if (protocol.getProtocol() == currentProtocol) {
				if (first == null)
					first = protocol;

				if (protocol.isReleaseCandidate())
					return protocol;
			}
		}

		if (first != null)
			return first;

		return MinecraftProtocol.values()[0];
	}

	/**
	 * Gets the current protocol number.
	 *
	 * @return Current protocol number.
	 */
	public static int getCurrentProtocol() {
		return getCurrent().getProtocol();
	}

	/**
	 * Gets the current protocol version.
	 *
	 * @return Current protocol version.
	 */
	public static String getCurrentVersion() {
		return getCurrent().getVersion();
	}

	/**
	 * Gets the current minecraft server version.
	 *
	 * @return Current minecraft server version.
	 */
	private static String getServerVersion() {
		return SERVER_VERSION;
	}

	/**
	 * Gets the protocol number that matches the given version.
	 * <p>
	 * Attempts to fetch the protocol number if it doesn't exist in this enum.
	 *
	 * @param version The protocol version to search with.
	 * @return Matches protocol number.
	 */
	public static int getProtocol(String version) {
		for (MinecraftProtocol protocol : values()) {
			if (protocol.getVersion().equals(version))
				return protocol.getProtocol();
		}

		return getFetchedProtocol(version);
	}

	private static int getFetchedProtocol(String version) {
		try {
			HttpBody body = new HttpBody(StringUtil.format("ver={0}", version));
			HttpResponse response = HttpClient.post(new URL("https://api.netcoding.net/minecraft/protocol/search.php"), body);
			return Integer.valueOf(response.getBody().toString());
		} catch (Exception ex) {
			return values()[0].getProtocol();
		}
	}

	/**
	 * Checks if the current server is running forge.
	 *
	 * @return True if current server is running forge.
	 */
	public static boolean isForge() {
		return IS_FORGE;
	}

	/**
	 * Checks if the current server is running 1.8+.
	 *
	 * @return True if current server is running 1.8 or higher.
	 */
	public static boolean isPost1_7() {
		return !isPre1_8();
	}

	/**
	 * Checks if the current server is running 1.8-.
	 *
	 * @return True if current server is running 1.7.10 or lower.
	 */
	public static boolean isPre1_8() {
		return getCurrentProtocol() <= v1_7_10.getProtocol();
	}

	/**
	 * Checks if the current server is before 1.8.3.
	 *
	 * @return True if current server is running 1.8.2 or lower.
	 */
	public static boolean isPre1_8_3() {
		return IS_PRE_1_8_3;
	}

	/**
	 * Checks if the current server is a release candidate (main release).
	 *
	 * @return True if current server is a release candidate.
	 */
	public boolean isReleaseCandidate() {
		return this.rc;
	}

	/**
	 * Checks if the current server is running spigot.
	 *
	 * @return True if current server is running spigot.
	 */
	public static boolean isSpigot() {
		return IS_SPIGOT;
	}

}