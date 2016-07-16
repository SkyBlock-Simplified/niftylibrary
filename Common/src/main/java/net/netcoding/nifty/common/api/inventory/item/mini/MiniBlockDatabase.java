package net.netcoding.nifty.common.api.inventory.item.mini;

import com.google.common.base.Preconditions;
import net.netcoding.nifty.common.Nifty;
import net.netcoding.nifty.common.minecraft.inventory.item.ItemStack;
import net.netcoding.nifty.common.minecraft.inventory.item.meta.SkullMeta;
import net.netcoding.nifty.common.mojang.MinecraftMojangProfile;
import net.netcoding.nifty.core.mojang.exceptions.ProfileNotFoundException;
import net.netcoding.nifty.core.util.StringUtil;
import net.netcoding.nifty.core.util.concurrent.Concurrent;
import net.netcoding.nifty.core.util.concurrent.ConcurrentMap;
import net.netcoding.nifty.core.util.concurrent.ConcurrentSet;
import net.netcoding.nifty.core.util.misc.CSVStorage;

import java.util.Locale;
import java.util.UUID;

public final class MiniBlockDatabase extends CSVStorage {

	// TODO
	// https://www.spigotmc.org/resources/mini-blocks-library.9155/
	// https://www.spigotmc.org/resources/letterheads.14685/
	// http://minecraft-heads.com/database
	// https://headmc.net/view/misc - BETTER
	private static final MiniBlockDatabase INSTANCE = new MiniBlockDatabase();
	private final ConcurrentMap<UUID, MiniBlock> miniBlocks = Concurrent.newMap();

	private MiniBlockDatabase() {
		super(Nifty.getPlugin().getDesc().getDataFolder(), "miniblocks");
	}

	public ItemStack create(UUID uniqueId) {
		return this.create(uniqueId, false);
	}

	public ItemStack create(UUID uniqueId, boolean queryMojang) {
		Preconditions.checkArgument(uniqueId != null, "UniqueId cannot be NULL!");
		MiniBlock miniBlock = this.miniBlocks.get(uniqueId);

		if (miniBlock == null) {
			if (!queryMojang)
				Preconditions.checkArgument(miniBlock != null, StringUtil.format("No MiniBlock with uniqueId ''{0}'' found!", uniqueId));
			else {
				try {
					MinecraftMojangProfile profile = Nifty.getMojangRepository().searchByUniqueId(uniqueId);
					ItemStack skull = MiniBlock.SKULL.clone();
					SkullMeta meta = (SkullMeta)skull.getItemMeta();
					meta.setOwner(profile.getName());
					skull.setItemMeta(meta);
					return skull;
				} catch (ProfileNotFoundException pnfex) {
					throw new IllegalArgumentException(StringUtil.format("No user with uniqueId ''{0}'' found!", uniqueId));
				}
			}
		}

		return miniBlock.create();
	}

	public ItemStack create(String name) {
		for (MiniBlock miniBlock : this.miniBlocks.values()) {
			if (miniBlock.containsName(name))
				return this.create(miniBlock.getUniqueId());
		}

		return MiniBlock.SKULL.clone();
	}

	public MiniBlock get(UUID uniqueId) {
		Preconditions.checkArgument(uniqueId != null, "UniqueId cannot be NULL!");

		for (MiniBlock miniBlock : this.miniBlocks.values()) {
			if (miniBlock.getUniqueId().equals(uniqueId))
				return miniBlock;
		}

		return null;
	}

	public MiniBlock get(String name) {
		Preconditions.checkArgument(StringUtil.notEmpty(name), "Name cannot be NULL!");

		for (MiniBlock miniBlock : this.miniBlocks.values()) {
			if (miniBlock.containsName(name))
				return miniBlock;
		}

		return null;
	}

	public ConcurrentSet<MiniBlock> get(MiniBlock.Category category) {
		return this.miniBlocks.values().stream().filter(miniBlock -> miniBlock.getCategory() == category).collect(Concurrent.toSet());
	}

	public static MiniBlockDatabase getInstance() {
		return INSTANCE;
	}

	@Override
	protected void preReload() {
		this.miniBlocks.clear();
	}

	@Override
	protected void processLine(String[] parts) {
		if (parts.length < 4) return;
		UUID uniqueId = UUID.fromString(parts[0].toLowerCase(Locale.ENGLISH));
		String category = parts[1];
		String primaryName = parts[2];
		String skinBase64 = parts[3];

		if (!this.miniBlocks.containsKey(uniqueId)) {
			MiniBlock miniBlock = new MiniBlock(uniqueId, MiniBlock.Category.valueOf(category), primaryName, skinBase64);
			this.miniBlocks.put(uniqueId, miniBlock);
			miniBlock.createNames();
		}
	}

}