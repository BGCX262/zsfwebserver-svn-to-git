package testStruts;

public class TestMain {
	
	public static void main(String[] args) {
		try {
			/* 获得需要执行方法的类 */
			Class<?> cls = Class.forName("testStruts.MyImplement");
			/* 强制转换成必须实现的接口 */
			MyInterface obj = (MyInterface) cls.newInstance();
			/* 执行接口中的方法 */
			obj.print("hello struts...");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}

interface MyInterface {
	/**
	 * 封装print方法
	 * @param str	需要打印的字符串
	 */
	public void print(String str);
}

class MyImplement implements MyInterface {
	/**
	 * 实现接口方法
	 */
	public void print(String str) {
		System.out.println("println:" + str);
	}
}