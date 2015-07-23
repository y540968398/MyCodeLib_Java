package com.robert.socket.listener.impl;

import java.io.IOException;
import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.SocketChannel;

import org.apache.commons.lang.ArrayUtils;
import org.apache.log4j.Logger;

import com.robert.socket.listener.IServerListener;


public abstract class ABSServerListener implements IServerListener
{

	private Logger logger = Logger.getLogger(ABSServerListener.class);

	ByteBuffer readBuffer = ByteBuffer.allocate(512);
	protected SocketAddress clientAddress;
	SelectionKey key;
	SocketChannel channel;

	@Override
	public void handel(SelectionKey key)
	{
		this.key = key;
		channel = (SocketChannel) key.channel();
		if (!channel.isOpen())
		{
			logger.info("Socket Channel is closed ! ");
			return;
		}

		// 获取地址
		parseAddress();

		// 读取客户端消息
		byte[] msg = readMsg();
		if (null == msg)
		{
			return;
		}
		
//		 添加消息处理任务(可启动线程处理任务)
		addTask(this.key, msg);
	}

	@Override
	public byte[] readMsg()
	{
		readBuffer.clear();
		try
		{
			int len = channel.read(readBuffer);
			if (len >= 0)
			{
				readBuffer.flip();
				logger.info("Recevie msg from client :" + clientAddress);
				return ArrayUtils.subarray(readBuffer.array(), 0, len);
			}
			else
			{
				close();
			}
		}
		catch (Exception e)
		{
			logger.error("Read msg from channel failed ! ", e);
			close();
			return null;
		}
		return null;
	}

	@Override
	public void close()
	{
		try
		{
			try
			{
				channel.close();
			}
			finally
			{

				key.cancel();
			}
		}
		catch (IOException e)
		{
			logger.info("Close connection from client error !" + clientAddress, e);
		}
		logger.info("Close connection from client :" + clientAddress);
	}

	@Override
	public void parseAddress()
	{
		try
		{
			clientAddress = channel.getRemoteAddress();
			logger.debug("Client address :" + clientAddress);
		}
		catch (IOException e)
		{
			logger.error("Get client socket address error ! ", e);
		}
	}

}
