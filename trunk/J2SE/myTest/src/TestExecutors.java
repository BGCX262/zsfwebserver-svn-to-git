import java.util.Iterator;
import java.util.Vector;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;



public class TestExecutors {
	
	static ExecutorService es = Executors.newFixedThreadPool(5);
	
	public static void main(String[] args) {
		Vector<Object> vec = new Vector<Object>();
		/*for (int i = 0; i < 100000; i++) {
			vec.add(i);
		}*/
		
		Thread thread = new Processtor(vec);
		thread.start();
		
		for (int i = 0; i < vec.size(); i++	) {
			System.out.println(vec.get(i));
		}
	}
	
	public static class Processtor extends Thread {
		
		Vector<Object> vec;
		
		public Processtor(Vector<Object> vec) {
			this.vec = vec;
		}

		@Override
		public void run() {
			try {
				Thread.sleep(100);
				vec.remove(0);
				
			} catch (Exception e) {
				e.printStackTrace();
			}
			
		}
		
	}

}
