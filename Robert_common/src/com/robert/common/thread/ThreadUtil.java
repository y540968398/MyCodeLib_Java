package com.robert.common.thread;

import org.apache.log4j.Logger;

public class ThreadUtil
{

	private static Logger logger = Logger.getLogger(ThreadUtil.class);

	public static void sleep(long millionSecond)
	{
		try
		{
			logger.debug("Cur thread sleep : " + millionSecond + " ms");
			Thread.currentThread();
			Thread.sleep(millionSecond);
			
		}
		catch (InterruptedException e)
		{
			logger.error("Sleep failed !", e);
		}
	}

}
