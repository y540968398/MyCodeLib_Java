package com.robert.http.page.cache;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ResourceCache
{
	private static Map<String, String> downloadMap = new ConcurrentHashMap<String, String>();

	public static boolean containsUrl(String url)
	{
		return downloadMap.containsKey(url);
	}

	public static void addResource(String url, String saveRelativePath)
	{
		downloadMap.put(url, saveRelativePath);
	}

}
