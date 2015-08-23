package testThread;

public class MyThread extends Thread {

	public void run() {
		try {
			Thread.sleep(2000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		System.out.println("MyThread over............");
	}
	
}
