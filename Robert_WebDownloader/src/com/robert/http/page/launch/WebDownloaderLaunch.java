package com.robert.http.page.launch;

import java.util.Map;

import org.apache.log4j.Logger;

import com.robert.common.cfglog.CfgUtil;
import com.robert.common.thread.pool.CachedPoolExecutor;
import com.robert.common.thread.pool.IThreadPoolExecutor;
import com.robert.http.httpclient.thread.HttpDownloadRunner;
import com.robert.http.page.WebSubPageDownloader;

public class WebDownloaderLaunch
{

	private static Logger logger = Logger.getLogger(WebDownloaderLaunch.class);

	private static IThreadPoolExecutor<HttpDownloadRunner> downloaderPool;

	static
	{
		downloaderPool = new CachedPoolExecutor<HttpDownloadRunner>();
	}

	public static void main(String[] args)
	{
		CfgUtil.initConfig("/cfg/");
		Map<String, Object> downloadPageMap = CfgUtil.getMapConfig();
		if (downloadPageMap.size() == 0)
		{
			logger.info("No task to run ! Please add task on file[mapConfig.properties] !");
			return;
		}

		for (Map.Entry<String, Object> entry : downloadPageMap.entrySet())
		{
			new WebSubPageDownloader(entry.getKey(), entry.getValue().toString(), downloaderPool).downloadPage();
		}

		downloaderPool.startDealTask();
		logger.info("All task was done, system exit !");
	}

}
