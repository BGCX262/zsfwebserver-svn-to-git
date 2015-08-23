package net.sf.cindy.util;

import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import net.sf.cindy.util.Configuration;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class ThreadPoolExecutorTimer {
	private static final ConcurrentLinkedQueue<ScheduledExecutorService> executorInstances = new ConcurrentLinkedQueue<ScheduledExecutorService>();
	private static final ConcurrentLinkedQueue<ScheduledExecutorService> SmileCindyTimers = new ConcurrentLinkedQueue<ScheduledExecutorService>();
	private static final int CORE_POOL_SIZE = Math.max(1, Configuration.getTimerCorePoolSize());
	private static volatile ThreadPoolExecutorTimer instance = new ThreadPoolExecutorTimer();
	private static final Log log = LogFactory.getLog(ThreadPoolExecutorTimer.class);
	private static final Timer daemonTimer = new Timer(true);
	private BlockingQueue<Runnable> workQueue;
	private ExecutorService executor;

	private ThreadPoolExecutorTimer() {
		workQueue = new ArrayBlockingQueue<Runnable>(Math.max(100, Configuration.getDispatcherCapacity()));
		int keepAliveTime = Configuration.getDispatcherKeepAliveTime();
		executor = new ThreadPoolExecutor(CORE_POOL_SIZE, Integer.MAX_VALUE, keepAliveTime, TimeUnit.MILLISECONDS, workQueue, new TimerThreadFactory("CallerRunsPolicy timer - "),
				new ThreadPoolExecutor.CallerRunsPolicy());
	}

	@Deprecated
	public TimerTask schedule(Runnable task, Date time) {
		DefaultTimerTask defaultTimerTask = new DefaultTimerTask(task);
		daemonTimer.schedule(defaultTimerTask, time);
		return defaultTimerTask;
	}

	@Deprecated
	public TimerTask schedule(Runnable task, long delay, long period) {
		DefaultTimerTask defaultTimerTask = new DefaultTimerTask(task);
		daemonTimer.schedule(defaultTimerTask, delay, period);
		return defaultTimerTask;
	}

	public ScheduledExecutorService getPreciseTimer() {
		ScheduledExecutorService executorService = executorInstances.peek();
		if (executorService != null) {
			return executorService;
		} else {
			return getNewPreciseTimer();
		}
	}

	public ScheduledExecutorService getNewPreciseTimer() {
		ScheduledExecutorService executorService = new ScheduledThreadPoolExecutor(CORE_POOL_SIZE, new TimerThreadFactory("AbortPolicy Timer - "));
		executorInstances.offer(executorService);
		return executorService;
	}
	
	public ScheduledExecutorService getNewPreciseTimer(int corePoolSize) {//�߳���
		ScheduledExecutorService executorService = new ScheduledThreadPoolExecutor(corePoolSize, new TimerThreadFactory("AbortPolicy Timer - "));
		executorInstances.offer(executorService);
		return executorService;
	}

	public ScheduledExecutorService getCindyTimer() {
		ScheduledExecutorService executorService = SmileCindyTimers.peek();
		if (executorService != null) {
			return executorService;
		} else {
			return getNewCindyTimer();
		}
	}

	public ScheduledExecutorService getNewCindyTimer() {
		ScheduledExecutorService executorService = new SmileCindyScheduledPool(CORE_POOL_SIZE, new TimerThreadFactory("CindyPolicy Timer - "));
		SmileCindyTimers.offer(executorService);
		return executorService;
	}

	public static class TimerThreadFactory implements ThreadFactory {
		final AtomicInteger threadNumber = new AtomicInteger(1);
		String name = "Cindy timer thread - ";
		private AtomicInteger count;
		final ThreadGroup group;

		public TimerThreadFactory(String name) {
			if (name != null && !name.equals("")) {
				this.name = name;
			}
			SecurityManager s = System.getSecurityManager();
			group = (s != null) ? s.getThreadGroup() : Thread.currentThread().getThreadGroup();
			count = new AtomicInteger();
		}

		public Thread newThread(final Runnable r) {
			String threadName = name + count.incrementAndGet();
			Thread t = new Thread(group, r, threadName, 0);
			log.info(threadName);
			if (t.isDaemon())
				t.setDaemon(false);
			if (t.getPriority() != Thread.NORM_PRIORITY)
				t.setPriority(Thread.NORM_PRIORITY);
			return t;
		}

	}

	public class DefaultTimerTask extends TimerTask {
		private Runnable task;
		private Future<?> future;

		private DefaultTimerTask(Runnable task) {
			this.task = task;
		}

		public void run() {
			try {
				if (task != null) {
					setFuture(executor.submit(task));
				}
			} catch (Exception e) {
				log.error("Submit timer task run error," + e.toString());
			}
		}

		private void setFuture(Future<?> future) {
			this.future = future;
		}

		public Future<?> getFuture() {
			return future;
		}

	}

	public static ThreadPoolExecutorTimer getIntance() {
		if (instance != null) {
			return instance;
		} else {
			throw new IllegalStateException("Illegal initial variable");
		}
	}
}
