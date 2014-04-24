package net.netcoding.niftybukkit.util;

import java.util.HashMap;
import java.util.Map;

public class TimeUtil {

	private static final Map<String, Integer> times = new HashMap<>();

	static {
		times.put("dawn", 22000);
		times.put("sunrise", 23000);
		times.put("morning", 24000);
		times.put("day", 24000);
		times.put("midday", 28000);
		times.put("noon", 28000);
		times.put("afternoon", 30000);
		times.put("evening", 32000);
		times.put("sunset", 37000);
		times.put("dusk", 37500);
		times.put("night", 38000);
		times.put("midnight", 16000);
	}

	public static long getClockTime(String time) {
		Integer clock = times.get(time);

		if (clock != null)
			return clock;
		else {
			clock = NumberUtil.isInt(time) ? Integer.valueOf(time) : -1;
			if (clock == -1) throw new NumberFormatException(StringUtil.format("The provided time value {0} is neither a clock time or number!", clock));
			return Math.abs(clock);
		}
	}

	public static long getDateTime(String time) {
		long duration = 0;
		long component = 0;

		for (int i = 0; i < time.length(); i++) {
			char chr = time.charAt(i);

			if (Character.isDigit(chr)) {
				component *= 10;
				component += chr - '0';
			} else {
				switch (Character.toLowerCase(chr)) {
				case 'y':
					component *= 52;
				case 'w':
					component *= 7;
				case 'd':
					component *= 24;
				case 'h':
					component *= 60;
				case 'm':
					component *= 60;
				case 's':
					component *= 1000;
				}

				duration += component;
				component = 0;
			}
		}

		return duration;
	}

}