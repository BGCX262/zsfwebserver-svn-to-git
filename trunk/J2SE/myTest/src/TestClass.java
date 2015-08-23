import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Date;


public class TestClass {
	
	@SuppressWarnings("deprecation")
	public static void main(String[] args) {
		try {
			/* ���ַ�������࣬��ʵ���� */
			Class<?> test = Class.forName("TheClass");
			Object obj = test.newInstance();
			
			/* ����޲�print������ִ�� */
			Method method = test.getMethod("print", new Class[] {});
			method.invoke(obj, new Object[0]);
			
			/* ����޲�getStr�����������ֵ */
			Method method2 = test.getMethod("getStr", new Class[] {});
			System.out.println(method2.invoke(obj, new Object[] {}));
			
			/* ��ô�����print�������method3 */
			Method method3 = test.getMethod("print", new Class[] {String.class});
			method3.invoke(obj, new Object[] {new String("method3")});
			
			/* ����޲η���getDate���ض��� */
			Method method4 = test.getMethod("getDate", new Class[] {});
			System.out.println(((Date) method4.invoke(obj, new Object[] {})).getTime());
			
			/* ����˽�г�Ա������ֱ�Ӹ�ֵ���Թ�����Ҳ��Ч�� */
			Field field = test.getDeclaredField("date");
			field.setAccessible(true);
			System.out.println(((Date) field.get(obj)));
			/* ��˽�б�����ֵ */
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