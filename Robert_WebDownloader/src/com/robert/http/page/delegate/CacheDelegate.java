package com.robert.http.page.delegate;

import com.robert.http.enums.DownloadStatus;
import com.robert.http.page.bean.PageDealStatus;
import com.robert.http.page.cache.PageDownloadCache;

public enum CacheDelegate implements IWebDownloadDelegate
{

	INSTANCE;

	@Override
	public void startDownloadPage(PageDealStatus pageDealStatus)
	{
		PageDownloadCache.addTask(pageDealStatus);
	}

	@Override
	public void startDownloadImage(PageDealStatus pageDealStatus)
	{
		
	}

	@Override
	public void startDownloadCss(PageDealStatus pageDealStatus)
	{
	}

	@Override
	public void startDownloadJS(PageDealStatus pageDealStatus)
	{
	}

	@Override
	public void endDownloadImage(PageDealStatus pageDealStatus)
	{
	}

	@Override
	public void endDownloadCss(PageDealStatus pageDealStatus)
	{
	}

	@Override
	public void endDownloadJS(PageDealStatus pageDealStatus)
	{
	}

	@Override
	public void endDownloadPage(PageDealStatus pageDealStatus)
	{
		PageDownloadCache.changeTaskStatus(pageDealStatus.getCurUrl(), DownloadStatus.DOWNLOADED);
	}

}
