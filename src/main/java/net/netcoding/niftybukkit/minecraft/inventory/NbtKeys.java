package net.netcoding.niftybukkit.minecraft.inventory;

import net.netcoding.niftycore.util.StringUtil;

enum NbtKeys {

	ITEMOPENER,
	ITEMOPENER_DESTRUCTABLE,
	PAGING,
	SIGNATURE,
	TRADE_COMPLETE,
	TRADE_ITEM,
	TRADE_SLOT,
	TRADE_SWITCH;

	private final String path;

	NbtKeys() {
		this.path = StringUtil.format("fakeinv.{0}", name().toLowerCase().replaceAll("_", "."));
	}

	public String getPath() {
		return this.path;
	}

}