package testFactory;

public class Main {

	public static void main(String[] args) throws Exception {
		//Date begin = new Date();

		/*
		 * for (int i = 0; i < 100000; i++) { Class<?> cls =
		 * Class.forName("testFactory.MyFactoryBean"); IFactoryBean newInstance
		 * = (IFactoryBean) cls.newInstance();
		 * 
		 * newInstance.print(); IFactoryBean bean = new MyFactoryBean();
		 * bean.print(); }
		 */
		/*final MyFactoryBean mfb = new MyFactoryBean();
		new Thread() {
			public void run() {
				mfb.print();
				mfb.print1();
			}
		}.start();

		Date end = new Date();
		System.out.println("Main.main() ---------------- " + (end.getTime() - begin.getTime()));*/
		
		/* [[10001,1],[10002,3],[10003,4]] */

		String cmd = "001%000000001%000000000001%0000000000000001";
		long begin = System.nanoTime();
		
		System.out.println(cmd.split("%")[1]);
		
		long middle = System.nanoTime();
		
		System.out.println(cmd.substring(4, 13));
		
		long end = System.nanoTime();
		
		System.out.println(middle - begin);
		System.out.println(end - middle);
		
	}

}

interface IFactoryBean {

	public void print();

}

class MyFactoryBean implements IFactoryBean {

	public void print() {
		try {
			Thread.sleep(2000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		System.out.println("MyFactoryBean.print()");
	}

	public void print1() {
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		System.out.println("MyFactoryBean.print1()");
	}

}