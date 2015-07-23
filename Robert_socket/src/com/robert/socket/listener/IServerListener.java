package com.robert.socket.listener;

import java.nio.channels.SelectionKey;

public interface IServerListener
{

	/**
	 * 处理接收到的消息
	 * 
	 * @param key
	 */
	void handel(SelectionKey key);

	/**
	 * 获取客户端的消息
	 * 
	 * @return byte[]
	 */
	byte[] readMsg();

	/**
	 * 解析客户端链接地址
	 */
	void parseAddress();

	/**
	 * 释放当前客户端的链接
	 * 
	 * @param key
	 * @param channel
	 */
	void close();

	/**
	 * 启动任务处理来自客户端的请求
	 * 
	 * @param key
	 * @param msg
	 */
	void addTask(SelectionKey key, byte[] msg);

}
