package net.netcoding.nifty.common._new_.minecraft.block;

public interface Sign extends BlockState {

	String[] getLines();

	default String getLine(int index) throws IndexOutOfBoundsException {
		return this.getLines()[index];
	}

	void setLine(int index, String value) throws IndexOutOfBoundsException;

}