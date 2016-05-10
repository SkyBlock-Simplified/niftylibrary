package net.netcoding.niftybukkit.minecraft.inventory;

import net.netcoding.niftycore.util.StringUtil;

enum NbtKeys {

	ITEM_OPENER,
	ITEM_OPENER_DESTRUCTABLE,
	PAGING,
	SIGNATURE,
	TRADE_COMPLETE,
	TRADE_ITEM,
	TRADE_SLOT,
	TRADE_SWITCH;

	private final String key;

	NbtKeys() {
		this.key = StringUtil.format("FAKEINV_{0}", name());
	}

	public String getKey() {
		return this.key;
	}

}