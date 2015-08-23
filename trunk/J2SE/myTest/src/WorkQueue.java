import java.util.LinkedList;

public class WorkQueue {

	private final int nThreads;

	private final PoolWorker[] threads;

	private final LinkedList<Runnable> queue;
	
	public static volatile long count = 0;
	
	public static byte[] lock = new byte[0];
	
	public WorkQueue(int nThreads) {
		this.nThreads = nThreads;
		queue = new LinkedList<Runnable>();
		threads = new PoolWorker[this.nThreads];
		for (int i = 0; i < nThreads; i++) {
			threads[i] = new PoolWorker();
			threads[i].start();
		}
	}

	public void execute(Runnable r) {
		synchronized (queue) {
			queue.addLast(r);
			queue.notify();
		}
	}

	private class PoolWorker extends Thread {

		public void run() {
			Runnable r;
			while (true) {
				synchronized (queue) {
					while (queue.isEmpty()) {
						try {
							queue.wait();
						} catch (InterruptedException ignored) {}
					}
					r = (Runnable) queue.removeFirst();
				}
				try {
					r.run();
				} catch (RuntimeException e) {}
			}
		}
	}

	static long begin;
	static long end;
	public static void main(String[] args) throws InterruptedException {
		begin = System.currentTimeMillis();
		
		WorkQueue queue2 = new WorkQueue(5);
		/*for (int i = 1; i <= 1000000; i++) {
			final int num = i;
			queue2.execute(new Runnable() {
				public void run() {
					synchronized (lock) {
						count += num;
						throw new RuntimeException("throw");
					}
				}
			});
		}
		
		while (true) {
			Thread.sleep(0, 10);
			if (count == 500000500000l) {
				end = System.currentTimeMillis();
				
				System.out.println(end - begin);
				break;
			}
		}
		//System.exit(0);*/
	}
}
