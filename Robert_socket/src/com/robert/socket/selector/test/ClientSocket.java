package com.robert.socket.selector.test;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.nio.ByteBuffer;

import org.apache.commons.lang.ArrayUtils;
import org.apache.log4j.Logger;

import com.robert.common.cfglog.CfgUtil;
import com.robert.common.thread.ThreadUtil;

public class ClientSocket
{
	private static Logger logger = Logger.getLogger(ClientSocket.class);

	Socket socket;
	ByteBuffer readBuffer = ByteBuffer.allocate(512);
	BufferedReader bufferedReader;
	BufferedWriter bufferedWriter;

	int readNum = 1;
	int writeNum = 1;

	public ClientSocket(int port)
	{
		try
		{
			socket = new Socket("127.0.0.1", port);
			logger.info("Create client socket on addr : 127.0.0.1:" + port);
			bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			bufferedWriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
		}
		catch (Exception e)
		{
			logger.error("Create client socket failed !", e);
		}
	}

	public void sendMsg()
	{
		try
		{
			bufferedWriter.write("hello server !" + (writeNum++));
			bufferedWriter.flush();
			logger.debug("Write msg to server !");
		}
		catch (IOException e)
		{
			logger.error("Client : send msg error ! ", e);
		}
	}

	public void readMsg()
	{
		try
		{
			char[] msgCharArr = new char[1024];
			int len = bufferedReader.read(msgCharArr);
			if (len > 0)
			{
				logger.info("Read msg from server : " + (readNum++) + "  "
				        + new String(ArrayUtils.subarray(msgCharArr, 0, len)));
			}
		}
		catch (Exception e)
		{
			logger.error("Client : send msg error ! ", e);
		}
	}

	public void close()
	{
		try
		{
			try
			{
				bufferedReader.close();
			}
			finally
			{
				try
				{
					bufferedWriter.close();
				}
				finally
				{
					socket.close();
				}
			}
		}
		catch (IOException e)
		{
			logger.error("Close client socket error !");
		}
		logger.info("Client socket closed !");
	}

	public static void main(String[] args)
	{
		CfgUtil.initConfig("/src/test/resources/conf/");

		ClientSocket clientSocket = new ClientSocket(2099);

		clientSocket.sendMsg();
		for (int i = 0; i < 60; i++)
		{
			ThreadUtil.sleep(2000);
			clientSocket.readMsg();
			clientSocket.sendMsg();
		}
		clientSocket.close();
	}

}
