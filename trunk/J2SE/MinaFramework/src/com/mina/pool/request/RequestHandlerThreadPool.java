package com.mina.pool.request;

import java.util.concurrent.ConcurrentLinkedQueue;

import org.apache.mina.util.SynchronizedQueue;

import com.mina.utils.Configuration;
import com.mina.utils.LogUtil;

/**
 * 用户请求线程池
 * @author zsf
 * 2011-4-14 下午02:13:50
 */
public class RequestHandlerThreadPool {
	
	private static SynchronizedQueue<RunnableRequestPacket> requests = 
		new SynchronizedQueue<RunnableRequestPacket>(new ConcurrentLinkedQueue<RunnableRequestPacket>());
	
	private static ExecuteThread[] threadPool = new ExecuteThread[Configuration.getInt("request_threadPool_size", 5)];
	
	static {
		for (int i = 0; i < threadPool.length; i++) {
			threadPool[i] = new ExecuteThread();
			threadPool[i].start();
		}
	}
	
	public static void putRequest(RunnableRequestPacket packet) {
		synchronized (requests) {
			requests.offer(packet);
			requests.notify();
		}
	}

	public static class ExecuteThread extends Thread {
		
		public void run() {
			Runnable r;
			while (true) {
				synchronized (requests) {
					while (requests.isEmpty()) {
						try {
							requests.wait();
						} catch (InterruptedException e) {}
					}
					r = (Runnable) requests.poll();
				}
				try {
					r.run();
				} catch (Exception e) {
					LogUtil.error("run error!!", e);
				}
			}
		}
		
	}

}
