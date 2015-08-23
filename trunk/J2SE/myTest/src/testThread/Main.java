package testThread;

public class Main {
	
	public static void main(String[] args) {
		Thread thr = new MyThread();
		
		thr.start();
		
		System.out.println("main thread over.......");
	}

}
