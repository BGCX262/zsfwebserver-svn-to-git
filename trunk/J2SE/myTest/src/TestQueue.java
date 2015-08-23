import java.util.concurrent.ConcurrentLinkedQueue;


/**
 * ��Ϣ���в���
 * @author D-io
 * �÷�������+���һ����Ϣ������Ϣ����ӵ���Ϣ�����еȴ�ִ��
 * ��ʼ��10����Ϣ
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
