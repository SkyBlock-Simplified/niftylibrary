package net.netcoding.nifty.common._new_.api.inventory;

import net.netcoding.niftycore.util.StringUtil;

public enum NbtKeys {

	ITEMOPENER_UUID("fakeitem"),
	ITEMOPENER_DESTRUCTABLE("fakeitem"),
	PAGING,
	SIGNATURE,
	TRADE_COMPLETE,
	TRADE_ITEM,
	TRADE_SLOT,
	TRADE_SWITCH;

	private final String path;

	NbtKeys() {
		this("fakeinv");
	}

	NbtKeys(String key) {
		this.path = StringUtil.format("{0}.{1}", key, name().toLowerCase().replaceAll("_", "."));

	}

	public String getPath() {
		return this.path;
	}

}