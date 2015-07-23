package com.robert.socket.selector;

import java.io.IOException;

import com.robert.socket.listener.IServerListener;

public interface IServerSelector
{
	
	/**
	 * 初始化serverSocket
	 * 
	 * @param port
	 */
	void initServer(int port) throws IOException;

	/**
	 * 服务器启动监听
	 * 
	 * @throws IOException
	 */
	void monitoringNow() throws IOException;

	/**
	 * 服务端监听器注册
	 * 
	 * @param t
	 */
	void addListener(IServerListener serverListener);
	
}
