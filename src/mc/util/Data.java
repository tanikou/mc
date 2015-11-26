package mc.util;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Data {
	public static String toView(Date time) {
		return toView(time, "yyyy.MM.dd HH:mm:ss");
	}

	public static String toView(Date time, String format) {
		return new SimpleDateFormat(format).format(time);
	}
}
