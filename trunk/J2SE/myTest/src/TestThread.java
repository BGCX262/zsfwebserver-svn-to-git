

public class TestThread {
	
	public static String name = "123";
	
	public static void main(String[] args) {
		final long begin = System.currentTimeMillis();
		Thread thread = new Thread() {
			public void run() {
				synchronized (name) {
					try {
						name.wait(1003);
						Thread.sleep(1);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					System.out.println("thread1: " + name);
					long end = System.currentTimeMillis();
					System.out.println("program has run on: " + (end - begin) + "ms");
				}
			}
		};
		
		Thread thread2 = new Thread() {
			public void run() {
				synchronized (name) {
					try {
						Thread.sleep(1000);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					System.out.println("thread2: " + name);
				}
			}
		};
		
		thread.start();
		thread2.start();
		
	}

}
