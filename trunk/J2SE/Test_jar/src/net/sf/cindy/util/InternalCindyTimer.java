package net.sf.cindy.util;

import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import net.sf.cindy.util.Configuration;

import net.sf.cindy.util.ThreadPoolExecutorTimer.TimerThreadFactory;

public class InternalCindyTimer {
	private static final ConcurrentLinkedQueue<InternalCindyTimer> instances = new ConcurrentLinkedQueue<InternalCindyTimer>();
	private int corePoolSize = Math.max(1, Configuration.getTimerCorePoolSize());
	private ScheduledExecutorService scheduledThreadPool;

	private InternalCindyTimer() {
		scheduledThreadPool = new ScheduledThreadPoolExecutor(getCorePoolSize(), new TimerThreadFactory("SmileCindy internal Timer - "));
	}

	public ScheduledFuture<?> schedule(Runnable command, long delay, TimeUnit unit) {
		return scheduledThreadPool.schedule(command, delay, unit);
	}

	public ScheduledFuture<?> schedule(Runnable command, long initialDelay, long period, TimeUnit unit) {
		return scheduledThreadPool.scheduleAtFixedRate(command, initialDelay, period, unit);
	}

	public ScheduledExecutorService getScheduledExecutorService() {
		return scheduledThreadPool;
	}

	public static void cancelScheduledFuture(ScheduledFuture<?> scheduledFuture) {
		if (scheduledFuture != null) {
			if (!scheduledFuture.isDone() && !scheduledFuture.isCancelled()) {
				scheduledFuture.cancel(true);
			}
		}
	}

	public static InternalCindyTimer getNewInstance() {
		InternalCindyTimer newInstance = new InternalCindyTimer();
		instances.offer(newInstance);
		return newInstance;
	}

	public static InternalCindyTimer getInstance() {
		InternalCindyTimer instance = instances.peek();
		if (instance != null) {
			return instance;
		} else {
			return getNewInstance();
		}
	}

	public void setCorePoolSize(int corePoolSize) {
		this.corePoolSize = corePoolSize;
	}

	public int getCorePoolSize() {
		return corePoolSize;
	}
}
