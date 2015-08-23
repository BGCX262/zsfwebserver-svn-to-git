import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;


public class TestFutureTask {

	public static void main(String[] args) {
		long begin = System.currentTimeMillis();
		FutureTask<String> task = new FutureTask<String>(new Runnable() {
			@Override
			public void run() {
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}, "abc");
		new Thread(task).start();

		try {
			System.out.println(task.get());
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (ExecutionException e) {
			e.printStackTrace();
		}
		long end = System.currentTimeMillis();
		System.out.println(end-begin);
	}

}
