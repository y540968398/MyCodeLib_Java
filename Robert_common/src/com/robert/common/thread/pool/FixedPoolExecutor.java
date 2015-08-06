package com.robert.common.thread.pool;

import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;

import com.robert.common.thread.DefaultThreadFactory;
import com.robert.common.thread.ThreadUtil;

public class FixedPoolExecutor<T extends Runnable> extends ABSPoolExecutor<T> implements IThreadPoolExecutor<T>
{

	@Override
	public void initThreadPool()
	{
		super.taskQueue = new LinkedBlockingQueue<>();
		super.executors = Executors.newFixedThreadPool(ThreadUtil.getThreadNum(), new DefaultThreadFactory());
	}

	@Override
	public void startDealTask()
	{
		// 启动固定的线程，循环处理任务队列中的任务
		for (int i = 0; i < ThreadUtil.getThreadNum(); i++)
		{
			executors.execute(new Runnable()
			{
				@Override
				public void run()
				{
					T t = null;
					while ((t = getTask()) != null)
					{
						t.run();
					}
				}
			});
		}
		executors.shutdown();
	}

}
