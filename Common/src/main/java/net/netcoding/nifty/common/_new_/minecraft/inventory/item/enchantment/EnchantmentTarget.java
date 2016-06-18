package net.netcoding.nifty.common._new_.minecraft.inventory.item.enchantment;

import net.netcoding.nifty.common._new_.minecraft.material.Material;
import net.netcoding.nifty.common._new_.minecraft.inventory.item.ItemStack;

public enum EnchantmentTarget {
	ALL {
		public boolean includes(Material material) {
			return true;
		}
	},
	ARMOR {
		public boolean includes(Material material) {
			return ARMOR_FEET.includes(material) || ARMOR_LEGS.includes(material) || ARMOR_HEAD.includes(material) || ARMOR_TORSO.includes(material);
		}
	},
	ARMOR_FEET {
		public boolean includes(Material material) {
			return Material.LEATHER_BOOTS == material || Material.CHAINMAIL_BOOTS == material || Material.IRON_BOOTS == material ||
					Material.DIAMOND_BOOTS == material || Material.GOLD_BOOTS == material;
		}
	},
	ARMOR_LEGS {
		public boolean includes(Material material) {
			return Material.LEATHER_LEGGINGS == material || Material.CHAINMAIL_LEGGINGS == material || Material.IRON_LEGGINGS == material ||
					Material.DIAMOND_LEGGINGS == material || Material.GOLD_LEGGINGS == material;
		}
	},
	ARMOR_TORSO {
		public boolean includes(Material material) {
			return Material.LEATHER_CHESTPLATE == material || Material.CHAINMAIL_CHESTPLATE == material || Material.IRON_CHESTPLATE == material ||
					Material.DIAMOND_CHESTPLATE == material || Material.GOLD_CHESTPLATE == material;
		}
	},
	ARMOR_HEAD {
		public boolean includes(Material material) {
			return Material.LEATHER_HELMET == material || Material.CHAINMAIL_HELMET == material || Material.DIAMOND_HELMET == material ||
					Material.IRON_HELMET == material || Material.GOLD_HELMET == material;
		}
	},
	WEAPON {
		public boolean includes(Material material) {
			return Material.WOOD_SWORD == material || Material.STONE_SWORD == material || Material.IRON_SWORD == material ||
					Material.DIAMOND_SWORD == material || Material.GOLD_SWORD == material;
		}
	},
	TOOL {
		public boolean includes(Material material) {
			return Material.WOOD_SPADE == material || Material.STONE_SPADE == material || Material.IRON_SPADE == material ||
					Material.DIAMOND_SPADE == material || Material.GOLD_SPADE == material || Material.WOOD_PICKAXE == material ||
					Material.STONE_PICKAXE == material || Material.IRON_PICKAXE == material || Material.DIAMOND_PICKAXE == material ||
					Material.GOLD_PICKAXE == material || Material.WOOD_HOE == material || Material.STONE_HOE == material ||
					Material.IRON_HOE == material || Material.DIAMOND_HOE == material || Material.GOLD_HOE == material ||
					Material.WOOD_AXE == material || Material.STONE_AXE == material || Material.IRON_AXE == material ||
					Material.DIAMOND_AXE == material || Material.GOLD_AXE == material || Material.SHEARS == material ||
					Material.FLINT_AND_STEEL == material;
		}
	},
	BOW {
		public boolean includes(Material material) {
			return Material.BOW == material;
		}
	},
	FISHING_ROD {
		public boolean includes(Material material) {
			return Material.FISHING_ROD == material;
		}
	};

	public abstract boolean includes(Material material);

	public boolean includes(ItemStack itemStack) {
		return this.includes(itemStack.getType());
	}
}