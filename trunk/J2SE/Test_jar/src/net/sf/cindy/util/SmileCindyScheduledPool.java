package net.sf.cindy.util;

import java.util.concurrent.Callable;
import java.util.concurrent.Delayed;
import java.util.concurrent.FutureTask;
import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.RunnableScheduledFuture;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class SmileCindyScheduledPool extends ScheduledThreadPoolExecutor {

	private static final Log log = LogFactory.getLog(SmileCindyScheduledPool.class);
	private static final AtomicLong sequencer = new AtomicLong(0);
	private static final long NANO_ORIGIN = System.nanoTime();

	public SmileCindyScheduledPool(int corePoolSize) {
		super(corePoolSize);
	}

	public SmileCindyScheduledPool(int corePoolSize, RejectedExecutionHandler handler) {
		super(corePoolSize, handler);
	}

	public SmileCindyScheduledPool(int corePoolSize, ThreadFactory threadFactory) {
		super(corePoolSize, threadFactory);
	}

	public SmileCindyScheduledPool(int corePoolSize, ThreadFactory threadFactory, RejectedExecutionHandler handler) {
		super(corePoolSize, threadFactory, handler);
	}

	protected <V> RunnableScheduledFuture<V> decorateTask(Callable<V> callable, RunnableScheduledFuture<V> task) {
		return super.decorateTask(callable, task);
	}

	protected <V> RunnableScheduledFuture<V> decorateTask(Runnable runnable, RunnableScheduledFuture<V> task) {
		return super.decorateTask(runnable, task);
	}

	private class ScheduledFutureTask<V> extends FutureTask<V> implements RunnableScheduledFuture<V> {
		/** Sequence number to break ties FIFO */
		private final long sequenceNumber;
		/** The time the task is enabled to execute in nanoTime units */
		private long time;
		/**
		 * Period in nanoseconds for repeating tasks. A positive value indicates
		 * fixed-rate execution. A negative value indicates fixed-delay
		 * execution. A value of 0 indicates a non-repeating task.
		 */
		private final long period;

		/**
		 * Creates a one-shot action with given nanoTime-based trigger time.
		 */
		ScheduledFutureTask(Runnable r, V result, long ns) {
			super(r, result);
			this.time = ns;
			this.period = 0;
			this.sequenceNumber = sequencer.getAndIncrement();
		}

		/**
		 * Creates a periodic action with given nano time and period.
		 */
		ScheduledFutureTask(Runnable r, V result, long ns, long period) {
			super(r, result);
			this.time = ns;
			this.period = period;
			this.sequenceNumber = sequencer.getAndIncrement();
		}

		/**
		 * Creates a one-shot action with given nanoTime-based trigger.
		 */
		ScheduledFutureTask(Callable<V> callable, long ns) {
			super(callable);
			this.time = ns;
			this.period = 0;
			this.sequenceNumber = sequencer.getAndIncrement();
		}

		public long getDelay(TimeUnit unit) {
			long d = unit.convert(time - now(), TimeUnit.NANOSECONDS);
			return d;
		}

		public int compareTo(Delayed other) {
			if (other == this) // compare zero ONLY if same object
				return 0;
			if (other instanceof ScheduledFutureTask) {
				ScheduledFutureTask<?> x = (ScheduledFutureTask<?>) other;
				long diff = time - x.time;
				if (diff < 0)
					return -1;
				else if (diff > 0)
					return 1;
				else if (sequenceNumber < x.sequenceNumber)
					return -1;
				else
					return 1;
			}
			long d = (getDelay(TimeUnit.NANOSECONDS) - other.getDelay(TimeUnit.NANOSECONDS));
			return (d == 0) ? 0 : ((d < 0) ? -1 : 1);
		}

		/**
		 * Returns true if this is a periodic (not a one-shot) action.
		 * 
		 * @return true if periodic
		 */
		public boolean isPeriodic() {
			return period != 0;
		}

		/**
		 * Runs a periodic task.
		 */
		private void runPeriodic() {
			boolean ok = ScheduledFutureTask.super.runAndReset();
			if (!ok) {
				log.warn("Scheduled task runtime exception.");
			}
			boolean down = isShutdown();

			// Reschedule if not cancelled and not shutdown or policy allows
			if ((!down || (getContinueExistingPeriodicTasksAfterShutdownPolicy() && !isTerminating()))) {
				long p = period;
				if (p > 0)
					time += p;
				else
					time = now() - p;
				SmileCindyScheduledPool.super.getQueue().add(this);
			}
			// This might have been the final executed delayed
			// task. Wake up threads to check.
			else if (down) {
				// interruptIdleWorkers();
				log.warn("It's do nothing when Shut down");
			}
		}

		final long now() {
			return System.nanoTime() - NANO_ORIGIN;
		}

		/**
		 * Overrides FutureTask version so as to reset/requeue if periodic.
		 */
		public void run() {
			if (isPeriodic())
				runPeriodic();
			else
				ScheduledFutureTask.super.run();
		}
	}

}
