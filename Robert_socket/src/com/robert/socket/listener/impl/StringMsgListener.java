package com.robert.socket.listener.impl;

import java.nio.channels.SelectionKey;

import org.apache.log4j.Logger;

import com.robert.socket.listener.IServerListener;

public class StringMsgListener extends ABSServerListener implements IServerListener
{

	private Logger logger = Logger.getLogger(StringMsgListener.class);

	int i = 1;

	@Override
	public void addTask(SelectionKey key, byte[] msg)
	{
		// 获取消息
		String msgString = new String(msg);
		logger.info("message : " + msgString);
		key.attach(new String("StringMsgListernner feedbakmsg " + (i++)).getBytes());
	}

}
