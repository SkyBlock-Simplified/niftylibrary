package net.netcoding.niftybukkit.utilities;

import java.util.Comparator;

public class LengthCompare implements Comparator<String> {

	public LengthCompare() {
		super();
	}

	@Override
	public int compare(String s1, String s2) {
		return s1.length() - s2.length();
	}

}