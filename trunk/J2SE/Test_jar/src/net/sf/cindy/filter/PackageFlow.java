package net.sf.cindy.filter;

import net.sf.cindy.util.Configuration;

public class PackageFlow {

	private static final int maxExceptionCount = Configuration.getMaxExceptionCount();
	private static final long alternationTime = Configuration.geAlternationTime();
	private static final int packageLength = Configuration.getPackageLength();
	private static final boolean enable = Configuration.isPackageFlow();
	private static final long interval = Configuration.getInterval();
	private int exceptionCount = 0;
	private long previousTime;
	private long startTime;

	public PackageFlow() {
		previousTime = System.currentTimeMillis();
		startTime = previousTime;
	}

	public boolean isReliance(int length) {
		if (enable) {
			if (length > packageLength) {
				return false;
			}
			long concurrentTime = System.currentTimeMillis();
			boolean blink = concurrentTime - previousTime < interval ? true : false;
			if (blink) {
				exceptionCount++;
				boolean attack = exceptionCount >= maxExceptionCount ? true : false;
				if (attack) {
					boolean invalid = startTime - concurrentTime < alternationTime ? true : false;
					if (invalid) {
						return false;
					}
				}
				previousTime = concurrentTime;
			} else {
				resetTimeRestrict();
			}
		}
		return true;
	}

	private void resetTimeRestrict() {
		long concurrentTime = System.currentTimeMillis();
		exceptionCount = 0;
		startTime = concurrentTime;
		previousTime = concurrentTime;
	}
}
