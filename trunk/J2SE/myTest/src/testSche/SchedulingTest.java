package testSche;

import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class SchedulingTest {
	private ScheduledThreadPoolExecutor executor;

	private static class SumTask implements Runnable {
		private int a, b;

		public SumTask(int a, int b) {
			this.a = a;
			this.b = b;
		}

		public void run() {
			System.out.println(String.format("Summing %s, %s = %s", a, b, (a + b)));
		}
	}

	private static class AccumulatorTask implements Runnable {
		private int increment, threshold;
		private int accumulator;
		private int loopCount;

		public AccumulatorTask(int increment, int threshold) {
			this.increment = increment;
			this.threshold = threshold;
			this.loopCount = 0;

			reset();
		}

		private void reset() {
			accumulator = 0;
		}

		public synchronized void run() {
			accumulator += increment;

			if (accumulator >= threshold) {
				System.out.println("Threshold reached. Going back to zero.");
				loopCount++;

				/*
				 * If we want the whole process to stop after a certain amount
				 * of loops we can stop it by simply firing an exception
				 */
				if (loopCount == 3)
					throw new RuntimeException();

				reset();
			}

			System.out.println("Accumulator task runnning: " + accumulator);
		}
	}

	public SchedulingTest() {
		executor = new ScheduledThreadPoolExecutor(3);
		exampleOfUsage();
	}

	/*
	 * Show how to schedule a single task for later execution or how to loop a
	 * task for multiple interval-based executions
	 */
	void exampleOfUsage() {
		executor.schedule(new SumTask(30, 20), 3, TimeUnit.SECONDS);
		executor.schedule(new SumTask(10, 90), 200, TimeUnit.MICROSECONDS);
		executor.schedule(new SumTask(2000, 3000), 500, TimeUnit.MILLISECONDS);

		executor.scheduleAtFixedRate(new AccumulatorTask(25, 100), 0, 1, TimeUnit.SECONDS);

	}

	public static void main(String[] args) {
		new SchedulingTest();
	}
}