package com.robert.common.thread.pool;


public interface IThreadPoolExecutor<T extends Runnable>
{

	/**
	 * 初始化线程池
	 * 
	 * @description 初始化参数:BlockingQueue ExecutorService
	 */
	public void initThreadPool();

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
