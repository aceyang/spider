package stock.common;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class Tool {
	
	public static String priceToString(int price) {
		String strPrice = "";
		int intNum = price / 100;
		int decNum = price % 100;
		String strDecNum = String.valueOf(decNum);
		while (strDecNum.length() < 2) {
			strDecNum = "0" + strDecNum;
		}

		strPrice = intNum + "." + strDecNum;

		return strPrice;
	}

	public static boolean isLast3Min(String time) {
		return time.compareTo("14:57:") > 0;
	}

	public static long getTimeInSecond(String date, String time) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Calendar cal = Calendar.getInstance();
		long second = 0L;
		try {
			cal.setTime(sdf.parse(date.trim() + " " + time));
			second = cal.getTimeInMillis() / 1000L;
		} catch (Exception e) {
			second = 0L;
		}

		return second;
	}
}