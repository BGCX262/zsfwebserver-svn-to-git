package testTimer;

import java.util.TimerTask;

public class MyTask extends TimerTask {

	public void run() {
		System.out.println("running.....");
		
		try {
			Thread.sleep(3000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		System.out.println("run over....");
	}

}
