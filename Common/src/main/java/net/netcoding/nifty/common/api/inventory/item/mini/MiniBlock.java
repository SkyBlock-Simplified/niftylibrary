package net.netcoding.nifty.common.api.inventory.item.mini;

import net.netcoding.nifty.common.Nifty;
import net.netcoding.nifty.common.api.nbt.NbtCompound;
import net.netcoding.nifty.common.api.nbt.NbtList;
import net.netcoding.nifty.common.minecraft.inventory.item.ItemStack;
import net.netcoding.nifty.common.minecraft.inventory.item.SkullType;
import net.netcoding.nifty.common.minecraft.material.Material;
import net.netcoding.nifty.core.util.comparator.LengthCompare;
import net.netcoding.nifty.core.util.concurrent.Concurrent;
import net.netcoding.nifty.core.util.concurrent.ConcurrentSet;

import java.util.UUID;

public final class MiniBlock {

	static final ItemStack SKULL = ItemStack.builder().type(Material.SKULL).type(SkullType.PLAYER.ordinal()).build();
	private static final String[] REPLACE = new String[] { "[^a-zA-z0-9]", "\\S", "-", "_" };
	private static final LengthCompare LENGTH_COMPARE = new LengthCompare();
	private final ConcurrentSet<String> names = Concurrent.newSet();
	private final UUID uniqueId;
	private final Category category;
	private final String skinBase64;
	private final String primaryName;

	MiniBlock(UUID uniqueId, Category category, String primaryName, String skinBase64) {
		this.uniqueId = uniqueId;
		this.category = category;
		this.primaryName = primaryName;
		this.skinBase64 = skinBase64;
	}

	boolean containsName(String name) {
		name = this.filter(name);

		for (String altName : this.getNames()) {
			if (this.filter(altName).equalsIgnoreCase(name))
				return true;
		}

		return false;
	}

	public ItemStack create() {
		ItemStack skull = SKULL.clone();
		NbtCompound skullOwner = Nifty.getNbtFactory().createCompound();
		NbtCompound properties = Nifty.getNbtFactory().createCompound();
		NbtList<NbtCompound> textures = Nifty.getNbtFactory().createList();
		NbtCompound skin = Nifty.getNbtFactory().createCompound();
		skin.put("Value", this.getSkinBase64());
		textures.add(skin);
		properties.put("textures", textures);
		skullOwner.put("Id", this.getUniqueId());
		skullOwner.put("Properties", properties);
		skull.getNbt().put("SkullOwner", skullOwner);
		return skull;
	}

	void createNames() {
		for (String replace : REPLACE) {
			this.names.add(this.getPrimaryName().replaceAll(replace, ""));
			this.names.add(this.getPrimaryName().replaceAll(replace, " "));
			this.names.add(this.getPrimaryName().replaceAll(replace, "-"));
			this.names.add(this.getPrimaryName().replaceAll(replace, "_"));
			this.names.add(this.getPrimaryName().replaceAll(replace, "."));
		}
	}

	private String filter(String value) {
		return value.replaceAll("_", "").replaceAll("-", "").replaceAll("\\.", "").replaceAll("\\S", "");
	}

	public Category getCategory() {
		return this.category;
	}

	public ConcurrentSet<String> getNames() {
		return this.names;
	}

	public String getPrimaryName() {
		return this.primaryName;
	}

	public String getSkinBase64() {
		return this.skinBase64;
	}

	public UUID getUniqueId() {
		return this.uniqueId;
	}

	public enum Category {

		ALPHABET,
		ANIMALS,
		BLOCKS,
		CHARACTERS,
		CHRISTMAS,
		DECORATION,
		ELECTRONICS,
		FLAGS,
		HALLOWEEN,
		FOOD,
		MISCELLANEOUS,
		MONSTERS,
		PLANTS,
		YOUTUBERS

	}

}