import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;



public class TestDate {

	public static void main(String[] args) throws Exception {
		Calendar c = Calendar.getInstance();
		c.setTimeInMillis(1323383942064l);
		System.out.println(c.getTime().toLocaleString());
		
		System.out.println(c.getTimeInMillis());
		
		c.add(Calendar.MINUTE, 1);
		
		System.out.println(c.getTimeInMillis());
		
		/*SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String t1 = "1927-12-31 23:54:07";
		String t2 = "1927-12-31 23:54:08";
		
		Date d1 = sdf.parse(t1);
		Date d2 = sdf.parse(t2);
		
		long l1 = d1.getTime() / 1000;
		long l2 = d2.getTime() / 1000;
		
		System.out.println(l1 + "\r\n" + l2);
		System.out.println(l1 - l2);*/
		
	}

}
