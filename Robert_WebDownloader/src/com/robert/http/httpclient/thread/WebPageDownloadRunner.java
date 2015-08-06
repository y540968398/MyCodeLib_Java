package com.robert.http.httpclient.thread;

import com.robert.http.page.WebSubPageDownloader;

public class WebPageDownloadRunner implements Runnable
{

	private WebSubPageDownloader downloader;

	public WebPageDownloadRunner(WebSubPageDownloader downloader)
	{
		this.downloader = downloader;
	}

	@Override
	public void run()
	{
		downloader.downloadPage();
	}

}
