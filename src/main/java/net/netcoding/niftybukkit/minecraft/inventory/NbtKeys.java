package net.netcoding.niftybukkit.minecraft.inventory;

import net.netcoding.niftycore.util.StringUtil;

enum NbtKeys {

	ITEM_OPENER,
	PAGING,
	SIGNATURE,
	TOTAL_SLOTS;

	private final String key;

	NbtKeys() {
		this.key = StringUtil.format("FAKEINV_{0}", name());
	}

	public String getKey() {
		return this.key;
	}

}