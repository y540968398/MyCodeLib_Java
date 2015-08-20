package com.robert.common.web.http;

import org.apache.commons.lang.StringUtils;

import com.robert.common.file.DirectoryUtils;
import com.robert.common.strings.StringsUtils;

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
	 * 获取页面子链接的规范地址，内连接，即 http://domain/.../Xxx.Xx。
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

		// 以协议 或 www 开头的网址 应该是合法地址，也有可能是外链接(非本网站的链接)
		if (linkUrl.startsWith("http") || linkUrl.startsWith("www"))
		{
			return linkUrl;
		}

		// 连接到当前页面的 锚点 或 参数 不处理子连接
		if (linkUrl.startsWith("#") || linkUrl.startsWith("?"))
		{
			return null;
		}

		// 子连接如果是类似 url#top 锚点的,则去掉锚点
		if (linkUrl.contains("#"))
		{
			linkUrl = linkUrl.substring(0, linkUrl.indexOf("#"));
		}

		// 判断是否为特殊的域名(hao.360.cn)直接以域名开头的地址 也是合法地址
		String firstPart = linkUrl.substring(0, linkUrl.indexOf("/") + 1);
		if (!firstPart.startsWith("../") && firstPart.contains("."))
		{
			return linkUrl;
		}

		if (linkUrl.startsWith("/"))
		{
			return getDomainName(curPageUrl) + linkUrl;
		}

		// 判断是否为调到上级目录的链接 ../../test.html
		if (linkUrl.startsWith("../"))
		{
			// 解析跳转到上层的链接
			return DirectoryUtils.getLink4RelParentPath(getUrlPath(curPageUrl), linkUrl);
		}

		return getUrlPath(curPageUrl) + linkUrl;
	}

	/**
	 * 获取 跳转到 子链接 的相对路径，并过验证 URL 是否为子链接 ，或外网链接。
	 * 
	 * @description 根据 父链接 子连接 间的关系，得到 父链接页面 跳转到 子连接页面 的相对路径。
	 * @param rootUrl
	 *            父链接
	 * @param subUrl
	 *            子链接
	 * @return String 相对路径 （如果是外链接，或父链接，则返回  unValidLink:链接）
	 */
	public static String getPath2SubLink(String rootUrl, String subUrl)
	{
		if (StringUtils.isEmpty(rootUrl) || StringUtils.isEmpty(subUrl))
		{
			return null;
		}

		String rootUrlPath = getUrlPath(rootUrl);
		String subUrlPath = getUrlPath(subUrl);

		String absPath = "";
		// 如果两个连接是同一级的则直接返回页面名称
		if (rootUrlPath.equals(subUrlPath))
		{
			absPath = getFileName(subUrl);
		}
		// 如果要解析的路径不是指定路径的子路径则返回
		else if (subUrl.startsWith(rootUrlPath))
		{
			absPath = subUrl.substring(rootUrlPath.length());
		}
		// 外网链接
		else
		{
			return "unValidLink:" + subUrl;
		}

		if (StringUtils.isEmpty(absPath))
		{
			return null;
		}

		return absPath;
	}

	/**
	 * 获取当前连接中包含的路径信息
	 * 
	 * @description 1. 获取合法链接(http://domain/path/../conten.htm) <br/>
	 *              中包含的路径信息 (http://domain/path/../)<br/>
	 *              2. 获取相对连接中的路径信息 ../path/path/content.htm ----> ../path/path/
	 * 
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
		// http://domain 判断是否为：协议+域名 的格式
		if (curPageUrl.startsWith("http") && curPageUrl.lastIndexOf("/") <= 7)
		{
			// path最后要以 / 结尾
			if (curPageUrl.charAt(curPageUrl.length() - 1) != '/')
			{
				rsString += "/";
			}
			rsString = curPageUrl;
		}
		else
		{
			// 带路径的合法链接、相对链接(包含跳转到父路径的相对连接)
			rsString = curPageUrl.substring(0, curPageUrl.lastIndexOf("/") + 1);
		}

		return rsString;
	}

	/**
	 * 获取当前地址对应的路径，以 / 结尾且不包含域名。
	 * 
	 * @description 1.以 协议://域名/path/名称 形式的合法路径，获取其 path 部分 <br/>
	 *              该部分以 "第一级路径名" 开头 以"/"结尾 <br/>
	 *              2.如果是相对连接，则直接返回
	 * @param curPageUrl
	 *            当前页面地址
	 * @return String 当前页面地址对应的网站目录地址，不包含协议 与 域名部分
	 */
	public static String getUrlPathNoneDomain(String curPageUrl)
	{
		String urlPath = getUrlPath(curPageUrl);
		int firstSepIndex = urlPath.indexOf("/");
		int lastSepIndex = urlPath.lastIndexOf("/");

		// URL开头至第一个分隔符的部分
		String firstPart = urlPath.substring(0, urlPath.indexOf("/") + 1);

		// 以合法 协议://域名 开头的连接
		if (firstPart.startsWith("http"))
		{
			// 仅为 协议://域名/
			int thirdSepIdx = StringsUtils.indexOfLevel(urlPath, "/", 3);
			// 截取第三个 分隔符 后面的字符串
			if (thirdSepIdx != -1 && thirdSepIdx != urlPath.lastIndexOf("/"))
			{
				urlPath = urlPath.substring(thirdSepIdx + 1, urlPath.lastIndexOf("/") + 1);
			}
		}

		// 仅以域名 开头发连接
		if (firstPart.contains(".") && !firstPart.contains("../"))
		{
			urlPath = urlPath.substring(firstSepIndex + 1, lastSepIndex + 1);
		}

		return urlPath;
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

	/**
	 * 解析两个路径的相对关系
	 * 
	 * @param rootUrl
	 * @param pageUrl
	 * @return
	 */
	public static String getRelativePath(String rootUrl, String pageUrl)
	{
		return null;
	}

}
