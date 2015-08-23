package testStruts;

public class TestMain {
	
	public static void main(String[] args) {
		try {
			/* �����Ҫִ�з������� */
			Class<?> cls = Class.forName("testStruts.MyImplement");
			/* ǿ��ת���ɱ���ʵ�ֵĽӿ� */
			MyInterface obj = (MyInterface) cls.newInstance();
			/* ִ�нӿ��еķ��� */
			obj.print("hello struts...");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}

interface MyInterface {
	/**
	 * ��װprint����
	 * @param str	��Ҫ��ӡ���ַ���
	 */
	public void print(String str);
}

class MyImplement implements MyInterface {
	/**
	 * ʵ�ֽӿڷ���
	 */
	public void print(String str) {
		System.out.println("println:" + str);
	}
}