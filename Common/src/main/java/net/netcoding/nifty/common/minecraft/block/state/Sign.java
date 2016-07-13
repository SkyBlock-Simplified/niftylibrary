package net.netcoding.nifty.common.minecraft.block.state;

public interface Sign extends BlockState {

	String[] getLines();

	default String getLine(int index) throws IndexOutOfBoundsException {
		return this.getLines()[index];
	}

	void setLine(int index, String value) throws IndexOutOfBoundsException;

}