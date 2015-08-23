package com.server.util;

import java.text.NumberFormat;
import java.util.Calendar;
import java.util.Random;

public class ToolUtil {
	private static final Random RANDOM = new Random();
	private static final Calendar CALENDAR = Calendar.getInstance();
	private static final NumberFormat FORMAT = NumberFormat.getInstance();
	private static final NumberFormat LOWFORMAT = NumberFormat.getInstance();
	
	static {
		FORMAT.setGroupingUsed(false);
		FORMAT.setMaximumFractionDigits(4);
		FORMAT.setMinimumFractionDigits(0);
		
		LOWFORMAT.setGroupingUsed(false);
		LOWFORMAT.setMinimumFractionDigits(0);
		LOWFORMAT.setMaximumFractionDigits(1);
	}
	
	
	public static Random getRandom(){
		return RANDOM;
	}
	
	public static Calendar getCalendar(){
		return CALENDAR;
	}
	
	public static NumberFormat getFormat() {
		return FORMAT;
	}
	
	public static NumberFormat getLowFormat() {
		return LOWFORMAT;
	}
	
}
