package net.netcoding.niftybukkit._new_.minecraft.material;

import net.netcoding.niftybukkit._new_.minecraft.inventory.item.ItemStack;

public class MaterialData implements Cloneable {

	private final int type;
	private byte data;

	public MaterialData(int type) {
		this(type, (byte)0);
	}

	public MaterialData(Material type) {
		this(type, (byte)0);
	}

	public MaterialData(Material type, byte data) {
		this(type.getId(), data);
	}

	public MaterialData(int type, byte data) {
		this.data = 0;
		this.type = type;
		this.data = data;
	}

	@Override
	public MaterialData clone() {
		try {
			return (MaterialData)super.clone();
		} catch (CloneNotSupportedException var2) {
			throw new Error(var2);
		}
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof MaterialData) {
			MaterialData md = (MaterialData)obj;
			return md.getItemTypeId() == this.getItemTypeId() && md.getData() == this.getData();
		}

		return false;
	}

	public byte getData() {
		return this.data;
	}

	public Material getItemType() {
		return Material.getMaterial(this.type);
	}

	public int getItemTypeId() {
		return this.type;
	}

	@Override
	public int hashCode() {
		return this.getItemTypeId() << 8 ^ this.getData();
	}

	public void setData(byte data) {
		this.data = data;
	}

	public ItemStack toItemStack() {
		return ItemStack.of(this.type, 0, this.data);
	}

	public ItemStack toItemStack(int amount) {
		return ItemStack.of(this.type, amount, this.data);
	}

	@Override
	public String toString() {
		return this.getItemType() + "(" + this.getData() + ")";
	}

}