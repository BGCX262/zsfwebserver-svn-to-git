package com.mina.pool.response;

import java.util.concurrent.ConcurrentLinkedQueue;

import org.apache.mina.util.SynchronizedQueue;

import com.mina.utils.Configuration;
import com.mina.utils.LogUtil;


/**
 * 响应线程池
 * @author zsf
 * 2011-4-14 下午03:11:47
 */
public class ResponseThreadPool {
	
	private static SynchronizedQueue<RunnableResponsePacket> responses = 
		new SynchronizedQueue<RunnableResponsePacket>(new ConcurrentLinkedQueue<RunnableResponsePacket>());
	
	private static ExecuteThread[] pool = new ExecuteThread[Configuration.getInt("response_threadPool_size", 5)];

	static {
		for (int i = 0; i < pool.length; i++) {
			pool[i] = new ExecuteThread();
			pool[i].start();
		}
	}
	
	public static void addResponse(RunnableResponsePacket packet) {
		synchronized (responses) {
			responses.add(packet);
			responses.notify();
		}
	}
	
	public static class ExecuteThread extends Thread {
		
		public void run() {
			Runnable r;
			while (true) {
				synchronized (responses) {
					while (responses.isEmpty()) {
						try {
							responses.wait();
						} catch (InterruptedException e) {}
					}
					r = (Runnable) responses.poll();
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
