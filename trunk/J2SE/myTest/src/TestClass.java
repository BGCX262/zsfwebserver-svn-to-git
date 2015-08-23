import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Date;


public class TestClass {
	
	@SuppressWarnings("deprecation")
	public static void main(String[] args) {
		try {
			/* 用字符串获得类，并实例化 */
			Class<?> test = Class.forName("TheClass");
			Object obj = test.newInstance();
			
			/* 获得无参print方法并执行 */
			Method method = test.getMethod("print", new Class[] {});
			method.invoke(obj, new Object[0]);
			
			/* 获得无参getStr方法输出返回值 */
			Method method2 = test.getMethod("getStr", new Class[] {});
			System.out.println(method2.invoke(obj, new Object[] {}));
			
			/* 获得带参数print方法输出method3 */
			Method method3 = test.getMethod("print", new Class[] {String.class});
			method3.invoke(obj, new Object[] {new String("method3")});
			
			/* 获得无参方法getDate返回对象 */
			Method method4 = test.getMethod("getDate", new Class[] {});
			System.out.println(((Date) method4.invoke(obj, new Object[] {})).getTime());
			
			/* 访问私有成员变量并直接赋值（对公共有也有效） */
			Field field = test.getDeclaredField("date");
			field.setAccessible(true);
			System.out.println(((Date) field.get(obj)));
			/* 给私有变量赋值 */
			field.set(obj, new Date(100, 1, 1, 1, 1, 1));
			System.out.println(((Date) field.get(obj)));
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}

class TheClass {
	private String str = "str";
	
	private Date date = new Date();
	
	public void print() {
		System.out.println("123");
	}
	
	public void print(String str) {
		System.out.println(str);
	}
	
	public String getStr() {
		return str;
	}
	
	public Date getDate() {
		return date;
	}
}