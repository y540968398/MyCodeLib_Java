package com.robert.common.thread.pool;

import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;

import org.apache.log4j.Logger;

import com.robert.common.thread.DefaultThreadFactory;

public class CachedPoolExecutor<T extends Runnable> extends ABSPoolExecutor<T> implements IThreadPoolExecutor<T>
{

	private Logger logger = Logger.getLogger(CachedPoolExecutor.class);

	@Override
	public void initThreadPool()
	{
		super.taskQueue = new LinkedBlockingQueue<T>();
		super.executors = Executors.newCachedThreadPool(new DefaultThreadFactory());
	}

	@Override
	public void startDealTask()
	{
		T t = null;
		while ((t = super.getTask()) != null)
		{
			executors.execute(t);
		}
		logger.info("CachedPoolExecutor shutdown !");
		executors.shutdown();
	}

}
