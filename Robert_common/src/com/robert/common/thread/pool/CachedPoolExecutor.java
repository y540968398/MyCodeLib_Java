package com.robert.common.thread.pool;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

import org.apache.log4j.Logger;

import com.robert.common.thread.ThreadUtil;
import com.robert.common.time.TimeUtil;

public class CachedPoolExecutor<T extends Runnable> implements IThreadPoolExecutor<T>
{

	private Logger logger = Logger.getLogger(CachedPoolExecutor.class);

	public BlockingQueue<T> taskQueue = new LinkedBlockingQueue<T>();
	ExecutorService executors = Executors.newCachedThreadPool();

	@Override
	public void addTask(T t)
	{
		try
		{
			taskQueue.put(t);
			logger.debug("Add new task to queue !");
		}
		catch (Exception e)
		{
			logger.error("Add task to queue error, has occured InterruptedException ! ", e);
		}
	}

	private T getTask()
	{
		try
		{
			logger.debug("Take task from queue !");
			return taskQueue.poll(3, TimeUnit.SECONDS);
		}
		catch (InterruptedException e)
		{
			logger.error("Get task occured InterruptedException, and retry !", e);
			return getTask();
		}
	}

	@Override
	public void startDealTask()
	{
		// logger.info("Start deal task from queue ！");
		// new Thread(new Runnable()
		// {
		//
		// @Override
		// public void run()
		// {
		T t = null;
		while ((t = getTask()) != null)
		{
			executors.execute(t);
		}
		executors.shutdown();
		// }
		// }).start();
	}

}
