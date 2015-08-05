package com.robert.common.thread.pool;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

import org.apache.log4j.Logger;

import com.robert.common.thread.DefaultThreadFactory;

public class ABSPoolExecutor<T extends Runnable> implements IThreadPoolExecutor<T>
{

	private Logger logger = Logger.getLogger(ABSPoolExecutor.class);

	public BlockingQueue<T> taskQueue;
	ExecutorService executors;

	@Override
	public void initThreadPool()
	{
		this.taskQueue = new LinkedBlockingQueue<T>();
		this.executors = Executors.newCachedThreadPool(new DefaultThreadFactory());
	}

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

	protected T getTask()
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
	}

}
