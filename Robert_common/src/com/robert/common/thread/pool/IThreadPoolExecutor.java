package com.robert.common.thread.pool;

public interface IThreadPoolExecutor<T extends Runnable>
{
	
	/**
	 * 向队列中添加任务
	 * 
	 * @param t
	 */
	public void addTask(T t);
	
	/**
	 * 开始处理队列中的任务
	 */
	public void startDealTask();

}
