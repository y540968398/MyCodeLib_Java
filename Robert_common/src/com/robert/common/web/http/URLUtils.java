package com.robert.common.web.http;

import org.apache.commons.lang.StringUtils;

public class URLUtils
{
	/**
	 * 获取域名
	 * 
	 * @param pageUrl
	 *            具体页面地址
	 * @return String 域名地址
	 */
	public static String getDomainName(String pageUrl)
	{
		if (StringUtils.isEmpty(pageUrl))
		{
			return pageUrl;
		}

		// 获取域名时去掉连接最后的 '/'，避免拼接成 // 的情况
		if (pageUrl.endsWith("/"))
		{
			pageUrl = pageUrl.substring(0, pageUrl.length() - 1);
		}

		// 以http开头的取前两部分
		if (pageUrl.startsWith("http"))
		{
			String[] urlArr = StringUtils.split(pageUrl, "/");
			if (urlArr.length >= 2)
			{
				return urlArr[0] + "//" + urlArr[1];
			}
		}

		// hao.360.cn www.baidu.com 这种域名的情况
		if (pageUrl.indexOf("/") == -1)
		{
			return pageUrl;
		}

		return pageUrl.substring(0, pageUrl.indexOf("/"));
	}

	/**
	 * 获取连接中的文件名
	 * 
	 * @description 获取连接中最后一个 / 后面的部分
	 * 
	 * @param src
	 *            链接地址
	 * @return String fileName
	 */
	public static String getFileName(String src)
	{
		if (src.indexOf("/") == -1 || src.endsWith("/"))
		{
			return src;
		}

		String[] strArr = StringUtils.split(src, "/");
		return strArr[strArr.length - 1];
	}

	/**
	 * 获取页面子链接的规范地址，即 http://domain/.../Xxx.Xx
	 * 
	 * @description 1.以 http 开头的连接地址不需要转换<br/>
	 *              2.以 / 开头的地址需要拼接域名，因为 以 / 开头及定位到网站的根目录<br/>
	 *              3.以 ../ 或 其它字符开头的 需要拼接当前页面路径
	 * @param curPageUrl
	 *            当前页面地址
	 * @param linkUrl
	 *            连接地址
	 * @return String 合法的URL http://domain/.../Xxx.Xx
	 */
	public static String getSubLinkURL(String curPageUrl, String linkUrl)
	{
		if (StringUtils.isEmpty(curPageUrl) || StringUtils.isEmpty(linkUrl))
		{
			return null;
		}

		// 以协议 或 www 开头的网址 应该是合法地址
		if (linkUrl.startsWith("http") || linkUrl.startsWith("www"))
		{
			return linkUrl;
		}

		// 直接以域名开头的地址 也是合法地址
		String firstPart = linkUrl.substring(0, linkUrl.indexOf("/") + 1);
		if (firstPart.contains("."))
		{
			return linkUrl;
		}

		if (linkUrl.startsWith("/"))
		{
			return getDomainName(curPageUrl) + linkUrl;
		}

		return getUrlPath(curPageUrl) + linkUrl;
	}

	/**
	 * 获取当前地址对应的路径
	 * 
	 * @description 1.页面地址如 :http://domain/.../Xxx.Xx <br/>
	 *              &nbsp;&nbsp;&nbsp;&nbsp;页面路径地址: http://domain/.../
	 *              2.如果页面地址中包含 ../
	 *              之类的地址，直接访问时由浏览器进行解析，最终跳转到一个真实的url，这样的url不用转换。
	 * @param curPageUrl
	 *            当前页面地址
	 * @return String 当前页面地址对应的网站目录地址
	 */
	public static String getUrlPath(String curPageUrl)
	{
		if (StringUtils.isEmpty(curPageUrl) || !StringUtils.contains(curPageUrl, "/"))
		{
			return null;
		}

		// 以 / 结尾的url
		if (curPageUrl.indexOf(curPageUrl.length() - 1) == '/')
		{
			return curPageUrl;
		}

		String rsString = "";
		// http://domain
		if (curPageUrl.startsWith("http") && curPageUrl.lastIndexOf("/") <= 7)
		{
			rsString = curPageUrl;
		}
		else
		{
			// http://domain/.../Xxx.xx domain/Xxx.Xx
			rsString = curPageUrl.substring(0, curPageUrl.lastIndexOf("/"));
		}

		if (rsString.indexOf(rsString.length() - 1) != '/')
		{
			rsString += "/";
		}
		return rsString;
	}

	/**
	 * 解析无缓存的链接地址
	 * 
	 * @param url
	 *            带有规避缓存的参数地址，通常用于图片地址的解析。
	 * @return String 无规避缓存参数的地址
	 */
	public static String parseNoCacheUrl(String url)
	{
		if (url.indexOf("?") != -1)
		{
			return url.substring(0, url.indexOf("?"));
		}
		return url;
	}

}
