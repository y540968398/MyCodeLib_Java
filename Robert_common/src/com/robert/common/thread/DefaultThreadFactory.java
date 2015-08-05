package com.robert.common.thread;

import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

public class DefaultThreadFactory implements ThreadFactory
{
	private static final AtomicInteger poolNumber = new AtomicInteger(1);
	private final AtomicInteger threadNumber = new AtomicInteger(1);
	private final ThreadGroup group;
	private final String namePrefix;

	public DefaultThreadFactory()
	{
		SecurityManager localSecurityManager = System.getSecurityManager();
		this.group = (localSecurityManager != null ? localSecurityManager.getThreadGroup() : Thread.currentThread()
		        .getThreadGroup());
		this.namePrefix = ("MyThrPool-" + poolNumber.getAndIncrement() + "-thr-");
	}

	public Thread newThread(Runnable paramRunnable)
	{
		Thread localThread = new Thread(this.group, paramRunnable, this.namePrefix
		        + this.threadNumber.getAndIncrement(), 0L);
		if (localThread.isDaemon())
			localThread.setDaemon(false);
		if (localThread.getPriority() != 5)
			localThread.setPriority(5);
		return localThread;
	}

}
