import java.util.concurrent.ConcurrentLinkedQueue;


/**
 * 消息队列测试
 * @author D-io
 * 用法：输入+添加一个消息，此消息会添加到消息队列中等待执行
 * 初始化10个消息
 */
public class TestQueue {
	
	public static ConcurrentLinkedQueue<String> queue;
	
	public TestQueue() {
	}
	
	public static void main(String[] args) {
		int size = 200;

		queue = new ConcurrentLinkedQueue<String>();
		for (int i = 0; i < size; i++) {
			queue.add("123");
		}
		
		for (int i = 0; i < size / 2; i++) {
			Thread thread = new Thread() {
				public void run() {
					synchronized (queue) {
						queue.remove();
						System.out.println(queue.size());
						this.interrupt();
					}
				}
			};
			Thread thread2 = new Thread() {
				public void run() {
					synchronized (queue) {
						queue.remove();
						System.out.println(queue.size());
						this.interrupt();
					}
				}
			};
			
			thread.start();
			thread2.start();
		}
	}
	
}
