package net.netcoding.niftybukkit.inventory.items;

public class ItemData {

	private final int itemId;
	private final short itemData;

	public ItemData(int itemId) {
		this(itemId, (short)0);
	}

	public ItemData(int itemId, short itemData) {
		this.itemId = itemId;
		this.itemData = itemData;
	}

	@Override
	public boolean equals(Object o) {
		if (o == null) return false;
		if (!(o instanceof ItemData)) return false;
		ItemData pairo = (ItemData)o;
		return this.itemId == pairo.getId() && this.itemData == pairo.getData();
	}

	public int getId() {
		return itemId;
	}

	public short getData() {
		return itemData;
	}

	@Override
	public int hashCode() {
		return (31 * itemId) ^ itemData;
	}

}