package com.robert.http.page.launch;

import java.util.Map;

import org.apache.log4j.Logger;

import com.robert.common.cfglog.CfgUtil;
import com.robert.common.thread.pool.CachedPoolExecutor;
import com.robert.common.thread.pool.FixedPoolExecutor;
import com.robert.common.thread.pool.IThreadPoolExecutor;
import com.robert.http.httpclient.thread.HttpDownloadRunner;
import com.robert.http.httpclient.thread.WebPageDownloadRunner;
import com.robert.http.page.WebSubPageDownloader;

public class WebDownloaderLaunch
{

	private static Logger logger = Logger.getLogger(WebDownloaderLaunch.class);

	/** 线程池：仅用于资源下载 */
	public static IThreadPoolExecutor<HttpDownloadRunner> resourcePoolExecutor;
	/** 线程池：仅用于页面下载 */
	public static IThreadPoolExecutor<WebPageDownloadRunner> pagePoolExecutor;

	static
	{
		resourcePoolExecutor = new CachedPoolExecutor<HttpDownloadRunner>();
		resourcePoolExecutor.initThreadPool();
		pagePoolExecutor = new FixedPoolExecutor<WebPageDownloadRunner>();
		pagePoolExecutor.initThreadPool();
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
			String url = entry.getValue().toString();
			String urlName = entry.getKey();
			WebSubPageDownloader subPageDownloader = new WebSubPageDownloader(url, urlName, url, urlName);
			// 指定当前链接的保存名称
			subPageDownloader.downloadPage();
			pagePoolExecutor.startDealTask();
		}

		resourcePoolExecutor.startDealTask();
		logger.debug("Main thread done !");
	}

}
