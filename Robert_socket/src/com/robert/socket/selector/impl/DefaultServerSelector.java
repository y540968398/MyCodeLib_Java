package com.robert.socket.selector.impl;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.apache.log4j.Logger;

import com.robert.socket.listener.IServerListener;
import com.robert.socket.selector.IServerSelector;

public class DefaultServerSelector implements IServerSelector
{

	Logger logger = Logger.getLogger(DefaultServerSelector.class);

	// 监听器集合
	List<IServerListener> listernerList = new ArrayList<IServerListener>();
	// 轮询器
	Selector selector;

	/**
	 * 初始化服务端轮询器
	 * 
	 * @param port
	 *            服务端监听的端口
	 * @throws IOException
	 */
	public void initServer(int port) throws IOException
	{
		// 对端口指定一个Socket管道
		ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
		serverSocketChannel.configureBlocking(false);
		serverSocketChannel.socket().bind(new InetSocketAddress(port));
		logger.info("Binding a new server socket on port :" + port);

		// 将当前管道注册到一个 轮询器 上
		selector = Selector.open();
		serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);
		logger.info("Init server selector success !");
	}

	/**
	 * 轮询当前端口中的链接
	 * 
	 * @throws IOException
	 */
	public void monitoringNow() throws IOException
	{
		logger.info("Start monitoring option_accept !");
		while (true)
		{
			// 开始监听
			selector.select();
			// 返回已连接到服务端的 key
			Set<SelectionKey> selectedKeys = selector.selectedKeys();
			Iterator<SelectionKey> iterator = selectedKeys.iterator();
			while (iterator.hasNext())
			{
				SelectionKey selectionKey = iterator.next();
				// 从轮询器中移除
				iterator.remove();
				handleKey(selectionKey);
			}
		}
	}

	/**
	 * 处理已经连接到服务器的链接，并根据连接状态进行相应的 读写操作
	 * 
	 * @param selectionKey
	 * @throws IOException
	 */
	private void handleKey(SelectionKey selectionKey)
	{
		SocketChannel clientSocketChannel = null;
		if (selectionKey.isAcceptable())
		{
			try
			{
				// 通过轮询器获得该服务端 Chanel，并获取连接到该服务端的链接
				ServerSocketChannel serverSocketChannel = (ServerSocketChannel) selectionKey.channel();
				clientSocketChannel = serverSocketChannel.accept();
				clientSocketChannel.configureBlocking(false);
				// 将该客户端的连接注册到轮询器中，并监控 可读 可写 的状态
				clientSocketChannel.register(selector, SelectionKey.OP_READ | SelectionKey.OP_WRITE);
				// clientSocketChannel.register(selector, SelectionKey.OP_READ);
				logger.info("New connection from client, listerner for option read | write !"
				        + clientSocketChannel.getRemoteAddress());
			}
			catch (Exception e)
			{
				logger.error("Selectionkey.isAcceptable", e);
				selectionKey.cancel();
			}
		}
		else if (selectionKey.isReadable())
		{
			logger.debug("Channel can be read !");
			// 将消息key传递给监听器
			notifyListeners(selectionKey);
		}
		else if (selectionKey.isWritable())
		{
			// 判断是否有返回消息
			if (null == selectionKey.attachment())
			{
				return;
			}

			// 返回消息给客户端
			clientSocketChannel = (SocketChannel) selectionKey.channel();
			try
			{
				byte[] feedbackMsg = (byte[]) selectionKey.attachment();
				logger.debug("Feed back to client ! msg.len : " + feedbackMsg.length);
				clientSocketChannel.write(ByteBuffer.wrap(feedbackMsg));
			}
			catch (Exception e)
			{
				logger.error("Write to client error !", e);
			}
			logger.info("Feedback to client !");
			selectionKey.attach(null);
		}
	}

	/**
	 * 通知所有的监听器
	 * 
	 * @param channel
	 *            新的客户端连接
	 */
	private void notifyListeners(SelectionKey key)
	{
		logger.debug("Notify listeners !");
		int num = 1;
		for (IServerListener serverListener : listernerList)
		{
			logger.debug("Nofity " + (num++) + " listeners to handel readable channel !");
			// 监听器控制多个channel进行通信(线程池)
			serverListener.handel(key);
		}
	}

	public void addListener(IServerListener serverListener)
	{
		listernerList.add(serverListener);
	}

}
