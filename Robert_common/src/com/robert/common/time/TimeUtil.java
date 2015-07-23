package com.robert.common.time;

import java.util.concurrent.TimeUnit;

public class TimeUtil
{

	public static long getMS4Second(int seconds)
	{
		return TimeUnit.MILLISECONDS.convert(seconds, TimeUnit.SECONDS);
	}
	
	public static long getMS4Hour(int hours){
		return TimeUnit.MILLISECONDS.convert(hours, TimeUnit.HOURS);
	}
	
	public static long getMS4Day(int days){
		return TimeUnit.MILLISECONDS.convert(days, TimeUnit.DAYS);
	}
	
}
