package net.netcoding.niftybukkit.minecraft.inventory;

import net.netcoding.niftycore.util.StringUtil;

enum NbtKeys {

	ITEM_OPENER,
	PAGE_LEFT,
	PAGE_RIGHT,
	SIGNATURE;

	private final String key;

	NbtKeys() {
		this.key = StringUtil.format("FAKEINV_{0}", name());
	}

	public String getKey() {
		return this.key;
	}

}