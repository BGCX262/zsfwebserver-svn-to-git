package testTimer;

import java.util.Timer;
import java.util.TimerTask;

public class TestTimer {

	public static void main(String[] args) {
		Timer timer = new Timer();

		TimerTask task = new MyTask();

		timer.schedule(task, 2000);
		
		task.cancel();
		timer.purge();
		
		task = new MyTask();
		
		timer.schedule(task, 0);
	}

}
