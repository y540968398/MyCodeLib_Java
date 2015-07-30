package com.robert.http.page.delegate;

import com.robert.http.enums.DownloadStatus;
import com.robert.http.page.bean.PageDealStatus;
import com.robert.http.page.cache.DownloadCache;

public class CacheDelegate implements IWebDownloadDelegate
{

	@Override
	public void startDownloadPage(PageDealStatus pageDealStatus)
	{
		DownloadCache.addTask(pageDealStatus);
	}

	@Override
	public void startDownloadImage(PageDealStatus pageDealStatus)
	{
		DownloadCache.addTask(pageDealStatus);
	}

	@Override
	public void startDownloadCss(PageDealStatus pageDealStatus)
	{
		DownloadCache.addTask(pageDealStatus);
	}

	@Override
	public void startDownloadJS(PageDealStatus pageDealStatus)
	{
		DownloadCache.addTask(pageDealStatus);
	}

	@Override
	public void endDownloadImage(PageDealStatus pageDealStatus)
	{
		DownloadCache.changeTaskStatus(pageDealStatus.getCurUrl(), DownloadStatus.DOWNLOADED);
	}

	@Override
	public void endDownloadCss(PageDealStatus pageDealStatus)
	{
		DownloadCache.changeTaskStatus(pageDealStatus.getCurUrl(), DownloadStatus.DOWNLOADED);
	}

	@Override
	public void endDownloadJS(PageDealStatus pageDealStatus)
	{
		DownloadCache.changeTaskStatus(pageDealStatus.getCurUrl(), DownloadStatus.DOWNLOADED);
	}

	@Override
	public void endDownloadPage(PageDealStatus pageDealStatus)
	{
		DownloadCache.changeTaskStatus(pageDealStatus.getCurUrl(), DownloadStatus.DOWNLOADED);
	}

}
