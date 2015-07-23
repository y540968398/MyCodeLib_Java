package com.robert.socket.listener.impl;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.SocketChannel;

import org.apache.commons.lang.ArrayUtils;
import org.apache.log4j.Logger;

import com.robert.common.thread.ThreadUtil;
import com.robert.socket.listener.IServerListener;

public class PerMsgPerThreadListener extends ABSServerListener implements IServerListener
{

	@Override
	public void addTask(SelectionKey key, byte[] msg)
	{
		new Thread(new Talk(key, msg)).start();
	}

}

class Talk implements Runnable
{

	private Logger logger = Logger.getLogger(Talk.class);

	private ByteBuffer readBuffer = ByteBuffer.allocate(512);
	private String name;
	private SocketChannel channel;

	public Talk(SelectionKey key, byte[] nameByte)
	{
		this.channel = (SocketChannel) key.channel();
		this.name = new String(nameByte);
	}

	@Override
	public void run()
	{
		// 一读一写(每2秒一次)
		while (true)
		{
			logger.info("Client msg : " + new String(readMsg()));
			try
			{
				channel.write(ByteBuffer.wrap(new String("Hello ! this is " + name + " speaking ! ").getBytes()));
			}
			catch (IOException e)
			{
				e.printStackTrace();
			}
			ThreadUtil.sleep(2000);
		}
	}

	public byte[] readMsg()
	{
		readBuffer.clear();
		try
		{
			int len = channel.read(readBuffer);
			if (len >= 0)
			{
				readBuffer.flip();
				logger.info("Recevie msg from client :" + channel.getRemoteAddress());
				return ArrayUtils.subarray(readBuffer.array(), 0, len);
			}
			else
			{
				channel.close();
			}
		}
		catch (Exception e)
		{
			logger.error("Read msg from channel failed ! ", e);
			return null;
		}
		return null;
	}

}