import java.util.Calendar;
import java.util.TimeZone;



public class TestCardNo {
	
	public static void main(String[] args) {
		String str = "421281199012260053";
		if (str.length() >= 14) {
			int year = Integer.valueOf(str.substring(6, 10)).intValue();
			int month = Integer.valueOf(str.substring(10, 12)).intValue();
			int date = Integer.valueOf(str.substring(12, 14)).intValue();
			Calendar now = Calendar.getInstance(TimeZone.getTimeZone("gmt+8"));
			Calendar c = Calendar.getInstance();
			c.set(Calendar.YEAR, year + 18);
			c.set(Calendar.MONTH, month - 1);
			c.set(Calendar.DATE, date);
			c.set(Calendar.HOUR_OF_DAY, 0);
			c.set(Calendar.MINUTE, 0);
			c.set(Calendar.SECOND, 0);
			System.out.println(now.after(c));
		}
	}

}
