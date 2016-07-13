package net.netcoding.nifty.craftbukkit.minecraft.inventory.item;

import com.google.common.base.Preconditions;
import net.netcoding.nifty.common.minecraft.inventory.item.ItemFactory;
import net.netcoding.nifty.common.minecraft.inventory.item.meta.ItemMeta;
import net.netcoding.nifty.common.minecraft.material.Material;
import net.netcoding.nifty.core.util.StringUtil;
import net.netcoding.nifty.craftbukkit.minecraft.inventory.item.meta.*;

public final class CraftItemFactory implements ItemFactory {

	private static final CraftItemFactory INSTANCE = new CraftItemFactory();

	private CraftItemFactory() { }

	@Override
	public ItemMeta asMetaFor(ItemMeta meta, Material material) {
		Preconditions.checkArgument(material != null, "Material cannot be NULL!");

		if (!(meta instanceof CraftItemMeta))
			throw new IllegalArgumentException(StringUtil.format("Meta of ''{0}'' not created by ''{1}''!", (meta != null ? meta.getClass().toString() : "NULL"), CraftItemFactory.class.getName()));
		else
			return this.getItemMeta(material, (CraftItemMeta)meta);
	}

	@Override
	public boolean equals(ItemMeta meta1, ItemMeta meta2) {
		Preconditions.checkArgument(meta1 != null, "Meta 1 cannot be NULL!");
		Preconditions.checkArgument(meta2 != null, "Meta 2 cannot be NULL!");
		return this.getHandle().equals(((CraftItemMeta)meta1).getHandle(), ((CraftItemMeta)meta2).getHandle());
	}

	public org.bukkit.inventory.ItemFactory getHandle() {
		return org.bukkit.Bukkit.getServer().getItemFactory();
	}

	public static CraftItemFactory getInstance() {
		return INSTANCE;
	}

	@Override
	public ItemMeta getItemMeta(Material material) {
		Preconditions.checkArgument(material != null, "Material cannot be NULL!");
		return this.getItemMeta(material, null);
	}

	private ItemMeta getItemMeta(Material material, CraftItemMeta craftMeta) {
		org.bukkit.inventory.meta.ItemMeta bukkitMeta = (craftMeta == null ? this.getHandle().getItemMeta(org.bukkit.Material.valueOf(material.name())) : craftMeta.getHandle());

		switch (material) {
			case AIR:
				return null;
			case DISPENSER:
			case NOTE_BLOCK:
			case PISTON_BASE:
			case MOB_SPAWNER:
			case CHEST:
			case FURNACE:
			case JUKEBOX:
			case ENCHANTMENT_TABLE:
			case COMMAND:
			case BEACON:
			case TRAPPED_CHEST:
			case DAYLIGHT_DETECTOR:
			case HOPPER:
			case DROPPER:
			case DAYLIGHT_DETECTOR_INVERTED:
			case COMMAND_REPEATING:
			case COMMAND_CHAIN:
			case SIGN:
			case BREWING_STAND_ITEM:
			case FLOWER_POT_ITEM:
			case REDSTONE_COMPARATOR:
			case SHIELD:
				return new CraftBlockStateMeta((org.bukkit.inventory.meta.BlockStateMeta)bukkitMeta);
			case LEATHER_HELMET:
			case LEATHER_CHESTPLATE:
			case LEATHER_LEGGINGS:
			case LEATHER_BOOTS:
				return new CraftLeatherArmorMeta((org.bukkit.inventory.meta.LeatherArmorMeta)bukkitMeta);
			case MAP:
				return new CraftMapMeta((org.bukkit.inventory.meta.MapMeta)bukkitMeta);
			case POTION:
			case SPLASH_POTION:
			case TIPPED_ARROW:
			case LINGERING_POTION:
				return new CraftPotionMeta((org.bukkit.inventory.meta.PotionMeta)bukkitMeta);
			case BOOK_AND_QUILL:
			case WRITTEN_BOOK:
				return new CraftBookMeta((org.bukkit.inventory.meta.BookMeta)bukkitMeta);
			//case WRITTEN_BOOK:
			//	return new CraftItemMeta(bukkitMeta); // CraftSignedBookMeta
			case SKULL_ITEM:
				return new CraftSkullMeta((org.bukkit.inventory.meta.SkullMeta)bukkitMeta);
			case FIREWORK:
				return new CraftFireworkMeta((org.bukkit.inventory.meta.FireworkMeta)bukkitMeta);
			case FIREWORK_CHARGE:
				return new CraftFireworkEffectMeta((org.bukkit.inventory.meta.FireworkEffectMeta)bukkitMeta);
			case ENCHANTED_BOOK:
				return new CraftEnchantedBookMeta((org.bukkit.inventory.meta.EnchantmentStorageMeta)bukkitMeta);
			case BANNER:
				return new CraftBannerMeta((org.bukkit.inventory.meta.BannerMeta)bukkitMeta);
			default:
				return new CraftItemMeta(bukkitMeta);
		}
	}

	@Override
	public boolean isApplicable(ItemMeta meta, Material material) {
		return this.getHandle().isApplicable(((CraftItemMeta)meta).getHandle(), org.bukkit.Material.valueOf(material.name()));
	}

}