package com.robert.socket.selector.test;

import java.io.IOException;

import org.apache.log4j.Logger;

import com.robert.common.cfglog.CfgUtil;
import com.robert.socket.listener.impl.PerMsgPerThreadListener;
import com.robert.socket.selector.IServerSelector;
import com.robert.socket.selector.impl.DefaultServerSelector;

public class ServerSocketTest
{
	private static Logger logger = Logger.getLogger(ServerSocketTest.class);

	public static void main(String[] args)
	{
		IServerSelector selector;
		CfgUtil.initConfig("/cfg/");
		selector = new DefaultServerSelector();
//		selector.addListener(new StringMsgListener());
		selector.addListener(new PerMsgPerThreadListener());
		try
		{
			selector.initServer(2099);
			selector.monitoringNow();
		}
		catch (IOException e)
		{
			logger.error("Monitoring error !", e);
		}
	}

}
