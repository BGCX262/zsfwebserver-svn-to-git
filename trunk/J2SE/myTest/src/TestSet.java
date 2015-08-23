import java.lang.reflect.Field;


public class TestSet {
	
	private int id = 0;
	
	public int getId() {
		return id;
	}
	
	public static void main(String[] args) {
		try {
			TestSet ts = new TestSet();
			
			Field field = TestSet.class.getDeclaredField("id");
			field.setAccessible(true);
			field.setInt(ts, 1);
			
			System.out.println(ts.getId());
			
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (NoSuchFieldException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
	}

}
