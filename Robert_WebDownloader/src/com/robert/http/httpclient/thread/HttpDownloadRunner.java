package com.robert.http.httpclient.thread;

import org.apache.log4j.Logger;

import com.robert.http.httpclient.GetDownloader;
import com.robert.http.httpclient.IHttpDownloader;

public class HttpDownloadRunner implements Runnable
{

	// 第三方参数部分
	private Logger logger = Logger.getLogger(HttpDownloadRunner.class);

	// 功能技术参数部分
	/** 页面下载器 */
	private IHttpDownloader pageDownloader;

	// 业务参数部分
	/** 下载的页面地址 */
	private String url;
	/** 页面保存路径 */
	private String savePath;

	public HttpDownloadRunner(String url, String filePath)
	{
		this.url = url;
		this.savePath = filePath;
		this.pageDownloader = new GetDownloader();
	}

	public HttpDownloadRunner(String url, String filePath, IHttpDownloader pageDownloader)
	{
		this.url = url;
		this.savePath = filePath;
		this.pageDownloader = pageDownloader;
	}

	@Override
	public void run()
	{
		logger.info("Start download page ! url[" + url + "], savePath[" + savePath + "]");
		pageDownloader.downPage(url, savePath);
		logger.info("Successed download page or resourse ! url[" + url + "], savePath[" + savePath + "]");
	}

}
