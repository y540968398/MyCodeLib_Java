package com.robert.http.page.cache;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import com.robert.http.enums.DownloadStatus;
import com.robert.http.page.bean.PageDealStatus;

public class DownloadCache
{

	private static Map<String, PageDealStatus> downloadMap = new ConcurrentHashMap<String, PageDealStatus>();
	private static Lock lock = new ReentrantLock();

	public static void addTask(PageDealStatus pageDealStatus)
	{
		downloadMap.put(pageDealStatus.getCurUrl(), pageDealStatus);
	}

	public static PageDealStatus getUrlStatusBean(String url)
	{
		return downloadMap.get(url);
	}

	public static boolean contains(String url)
	{
		return downloadMap.containsKey(url);
	}

	public static String getUrlCachedPath(String url)
	{
		return downloadMap.get(url).getRelativePath();
	}

	public static DownloadStatus getUrlStatus(String url)
	{
		return downloadMap.get(url).getStatus();
	}

	public static void changeTaskStatus(String url, DownloadStatus downloadStatus)
	{
		lock.lock();
		PageDealStatus pageDealStatus = downloadMap.get(url);
		if (null != pageDealStatus)
		{
			pageDealStatus.setStatus(downloadStatus);
		}
		lock.unlock();
	}

}
