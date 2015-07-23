package com.robert.common.cfglog;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

public class CfgUtil
{
	private static Logger logger = Logger.getLogger(CfgUtil.class);

	/** 系统配置 */
	static Properties properties = new Properties();
	/** 扩展参数配置 */
	static Properties properties2Map = new Properties();

	/**
	 * 初始化相同配置信息
	 */
	public static void initConfig(String folderPath)
	{
		logger.info("Loading config files !");
		// log4j 日志配置
		initLogConf(folderPath);
		// 系统参数配置
		initSysConf(folderPath);
		// 其它参数配置，转换为Map(可选)
		initMapConf(folderPath);
		logger.info("Load config files success !");
	}

	/**
	 * 加载 log4j 日志配置参数
	 * 
	 * @description 日志路径：/conf/log4j.properties
	 */
	private static void initLogConf(String folderPath)
	{
		FileInputStream inputStream = null;
		try
		{
			try
			{
				String logConfPath = System.getProperty("user.dir") + folderPath + "log4j.properties";
				System.out.println("log4jPath : " + logConfPath);
				inputStream = new FileInputStream(new File(logConfPath));
				PropertyConfigurator.configure(inputStream);
			}
			finally
			{
				inputStream.close();
			}
		}
		catch (Exception e)
		{
			logger.error("Load log4j.properties failed !", e);
		}
		logger.debug("Load log4j.properties file success !");
	}

	/**
	 * 加载 系统 系统 配置参数
	 * 
	 * @description 日志路径：/conf/sysConfig.properties
	 */
	private static void initSysConf(String folderPath)
	{
		InputStream confInputStream = null;
		try
		{
			try
			{
				String sysConfPath = System.getProperty("user.dir") + folderPath + "sysConfig.properties";
				logger.debug("sysConfigPath : " + sysConfPath);
				confInputStream = new FileInputStream(new File(sysConfPath));
				properties.load(confInputStream);
			}
			finally
			{
				confInputStream.close();
			}
		}
		catch (Exception e)
		{
			logger.error("Load sysConfig failed !", e);
		}
		logger.debug("Load sysConfig file success !");
	}

	/**
	 * 加载 RabbitMQ消息客户端 其它配置 参数
	 * 
	 * @description 日志路径：/conf/msgConfig.properties
	 */
	private static void initMapConf(String folderPath)
	{
		File mapConfFile = new File(System.getProperty("user.dir") + folderPath + "mapConfig.properties");
		if (!mapConfFile.exists())
		{
			logger.debug("Msg queue conf not exists !");
			return;
		}

		InputStream msgConfInputStream = null;
		try
		{
			try
			{
				logger.debug("sysConfigPath : " + mapConfFile.getPath());
				msgConfInputStream = new FileInputStream(mapConfFile);
				properties2Map.load(msgConfInputStream);
			}
			finally
			{
				msgConfInputStream.close();
			}
		}
		catch (Exception e)
		{
			logger.error("Load msgConfig failed !", e);
		}
		logger.debug("Load msgConfig file success !");
	}

	/**
	 * 获取配置信息
	 * 
	 * @param key
	 *            配置信息键
	 * @return String 配置信息的值
	 */
	public static String get(String key)
	{
		return properties.getProperty(key);
	}

	/**
	 * 获取配置信息的 整形数字
	 * 
	 * @param key
	 *            配置信息键
	 * @return int 配置信息的值
	 */
	public static int getInt(String key)
	{
		return Integer.parseInt(properties.getProperty(key));
	}

	/**
	 * 获取配置信息的 布尔值
	 * 
	 * @param key
	 *            配置信息键
	 * @return boolean 配置信息的值
	 */
	public static boolean getBoolean(String key)
	{
		return Boolean.parseBoolean(properties.getProperty(key));
	}

	/**
	 * 扩展参数转为 Map 格式
	 * 
	 * @return Map<String, Object> 扩展参数Map
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static Map<String, Object> getMapConfig()
	{
		if (properties2Map.isEmpty())
		{
			return null;
		}
		return new HashMap<String, Object>((Map) properties2Map);
	}

	// public static void main(String[] args)
	// {
	// ConfigUtil.initConfig();
	// logger.debug(ConfigUtil.getProperty("certifyPath"));
	// logger.debug(ConfigUtil.getProperty("certifyPass"));
	// }

}
