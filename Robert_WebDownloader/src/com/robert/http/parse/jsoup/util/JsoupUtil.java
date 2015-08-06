package com.robert.http.parse.jsoup.util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.robert.common.cfglog.CfgConstants;
import com.robert.common.cfglog.CfgUtil;
import com.robert.common.file.FileUtil;
import com.robert.common.web.http.URLUtils;
import com.robert.http.constants.WebConstants;

public class JsoupUtil
{

	private static Logger logger = Logger.getLogger(JsoupUtil.class);

	public static Document getDocViaGet(String url)
	{
		try
		{
			return Jsoup.connect(url).get();
		}
		catch (IOException e)
		{
			logger.error("Get document error ! url : " + url, e);
		}
		return null;
	}

	public static Document getDocViaPost(String url)
	{
		return getDocViaPost(url, null, null);
	}

	public static Document getDocViaPost(String url, Map<String, String> paramsMap, Map<String, String> coocieMap)
	{
		Connection jsoupConnection = Jsoup.connect(url);
		// 设置post请求超时时间
		jsoupConnection.timeout((int) TimeUnit.MILLISECONDS.convert(
		        CfgUtil.getInt(CfgConstants.TIMEOUT_HTTP_POST_SECOND), TimeUnit.SECONDS));
		// 设置 post 请求编码
		jsoupConnection.postDataCharset(CfgUtil.get(CfgConstants.CHARSET_DEFAULT));
		// 设置 post 表单参数
		if (null != paramsMap)
		{
			jsoupConnection.data(paramsMap);
		}
		// 设置 post cookie
		if (null != coocieMap)
		{
			for (Map.Entry<String, String> entry : coocieMap.entrySet())
			{
				jsoupConnection.cookie(entry.getKey(), entry.getValue());
			}
		}

		// 请求
		try
		{
			return jsoupConnection.post();
		}
		catch (IOException e)
		{
			logger.error("Post connection error ! url : " + url, e);
		}
		return null;
	}

	public static Document getDocFromFile(String filePath)
	{
		try
		{
			return Jsoup.parse(new File(filePath), CfgUtil.get(CfgConstants.CHARSET_DEFAULT));
		}
		catch (IOException e)
		{
			logger.error("Parse document error from file : " + filePath);
		}
		return null;
	}

	/**
	 * 解析下载的元素，修改链接地址为本地地址，并保存原始链接。
	 * 
	 * @description 1.链接 仅记录 资源URL的 路径 (即链接除去 http://domain 部分)<br/>
	 *              2.生成 original_url ，保存原始的 完整链接，如果没有域名则会拼接域名。
	 * 
	 * @param elements
	 *            页面获取的原始元素
	 * @param isUnique
	 *            是否去重重复元素
	 * @param curPageUrl
	 *            元素的所在页面的 baseURL,用于还原相对连接为合法连接。
	 * @param rootRelPath
	 *            跳转到根目录的相对路径
	 * @param srcAttr
	 *            元素链接属性(SRC HREF 等属性)，将被修改为相对路径。
	 * @return List<Element> 下载的元素集合
	 */
	public static List<Element> getDownloadElement(Elements elements, boolean isUnique, String curPageUrl,
	        String rootRelPath, String srcAttr)
	{
		Set<String> uniqueEleSrcSet = new HashSet<String>();

		List<Element> ele4DownloadList = new ArrayList<Element>();
		for (Element element : elements)
		{
			// 元素原始链接地址
			String eleUrl = URLUtils.parseNoCacheUrl(element.attr(srcAttr));
			// 解析元素合法的链接
			eleUrl = URLUtils.getSubLinkURL(curPageUrl, eleUrl);
			// 保存元素原始链接地址
			element.attr(WebConstants.ATTR_ORIGINAL_URL, eleUrl);

			// 元素本地保存的相对路径
			String eleSaveRelPath = rootRelPath + URLUtils.getUrlPathNoneDomain(eleUrl) + URLUtils.getFileName(eleUrl);
			// 修改元素链接地址为本地链接
			element.attr(srcAttr, eleSaveRelPath);

			// 如果是下载模式，过滤重复的文件，但每个链接的路径要修改
			if (isUnique && uniqueEleSrcSet.contains(eleUrl))
			{
				continue;
			}
			uniqueEleSrcSet.add(eleUrl);
			ele4DownloadList.add(element);
			logger.debug(element.outerHtml());
		}
		return ele4DownloadList;
	}

	public static List<Element> getAllImages(Document document, String curPageUrl, String savePath)
	{
		return getDownloadElement(document.getElementsByTag(WebConstants.TAG_IMG), false, curPageUrl, savePath,
		        WebConstants.ATTR_SRC);
	}

	public static List<Element> getDownloadImages(Document document, String curPageUrl, String savePath)
	{
		return getDownloadElement(document.getElementsByTag(WebConstants.TAG_IMG), true, curPageUrl, savePath,
		        WebConstants.ATTR_SRC);
	}

	public static List<Element> getAllCss(Document document, String curPageUrl, String savePath)
	{
		return getDownloadElement(document.getElementsByAttributeValue("type", "text/css"), false, curPageUrl,
		        savePath, WebConstants.ATTR_HREF);
	}

	public static List<Element> getDownloadCss(Document document, String curPageUrl, String savePath)
	{
		return getDownloadElement(document.getElementsByAttributeValue("type", "text/css"), true, curPageUrl, savePath,
		        WebConstants.ATTR_HREF);
	}

	public static List<Element> getAllJS(Document document, String curPageUrl, String savePath)
	{
		return getJS(document, curPageUrl, savePath, false);
	}

	public static List<Element> getDownloadJS(Document document, String curPageUrl, String savePath)
	{
		return getJS(document, curPageUrl, savePath, false);
	}

	public static List<Element> getAllSubLinks(Document document, String curPageUrl)
	{
		return getSubLinks(document, curPageUrl, false);
	}

	public static List<Element> getDownloadSubLinks(Document document, String curPageUrl)
	{
		return getSubLinks(document, curPageUrl, true);
	}

	/**
	 * 保存加载到内存中的页面文件
	 * 
	 * @description 下载页面后，需要下载页面中链接的资源文件(CSS,IMG,JS)，并且将其中链接的地址修改为下载后的相对地址。<br/>
	 *              这里既是将修改了资源地址后的页面源码，覆盖保存至源文件的操作。
	 */
	public static void saveDocument(Document document, String filePath)
	{
		if (null == document || StringUtils.isEmpty(filePath))
		{
			return;
		}
		if (!FileUtil.createFileIfNotExists(filePath))
		{
			return;
		}
		try
		{
			FileOutputStream fileOut = new FileOutputStream(new File(filePath));
			try
			{
				fileOut.write(document.toString().getBytes());
			}
			finally
			{
				fileOut.flush();
				fileOut.close();
			}
		}
		catch (Exception e)
		{
			logger.error("Save document error !", e);
		}
	}

	private static List<Element> getJS(Document document, String curPageUrl, String rootUrl, boolean isUnique)
	{
		Elements downloadJS = new Elements();
		Elements allJSElements = document.getElementsByAttributeValue("type", "text/javascript");
		for (Element element : allJSElements)
		{
			// 有 src 属性的 js 是外部链接的js文件
			if (StringUtils.isNotEmpty(element.attr(WebConstants.ATTR_SRC)))
			{
				downloadJS.add(element);
			}
		}
		return getDownloadElement(downloadJS, isUnique, curPageUrl, rootUrl, WebConstants.ATTR_SRC);
	}

	/**
	 * 获取所有连接，并将地址修改为相对地址，生成 original_url
	 * 
	 * @description 1.解析链接，将连接地址修改为本地相对地址，父页面地址/当前页面名称.html<br/>
	 *              2.生成 original_url，保存页面原始链接(如果是相对连接则转换为觉得链接地址)。<br/>
	 *              3.过滤连接地址
	 * 
	 * @param document
	 *            父页面源码
	 * @param curPageUrl
	 *            父页面原始地址，用于解析 连接中的相对地址。<br/>
	 *            <U><B>由于子页面的所有相对地址都是从根目录开始的，所以加上域名即是资源的合法 URL</B></U>
	 * @param isUnique
	 *            是否去除重复连接
	 * @return
	 */
	private static List<Element> getSubLinks(Document document, String curPageUrl, boolean isUnique)
	{
		List<Element> elementList = new ArrayList<Element>();
		Elements links = document.getElementsByTag(WebConstants.TAG_A);

		for (Element element : links)
		{
			// 链接地址
			String linkUrl = element.attr(WebConstants.ATTR_HREF);
			// 解析链接为合法地址(非相对地址)
			String subLinkUrl = URLUtils.getSubLinkURL(curPageUrl, linkUrl);
			if (null == subLinkUrl)
			{
				continue;
			}
			// 生成 original_url 保存原始地址
			element.attr(WebConstants.ATTR_ORIGINAL_URL, subLinkUrl);

			// TODO 从缓存中根据 original_url 拿出相对路径，如果存在则是用缓存中的相对路径，并 continue
			// ，该连接不加入下载列表。

			// 解析链接名称 生成子连接相对路径(/子页面目录/子页面名称)
			String localABSPath = URLUtils.getPath2SubLink(curPageUrl, subLinkUrl);
			if (null == localABSPath)
			{
				continue;
			}
			// 修改并保存连接相对路径：由于页面在下载时，并不知道其后缀名是什么样的有的是静态链接，有的是动态链接，这里统一加.html
			element.attr(WebConstants.ATTR_HREF, localABSPath + WebConstants.SURFIX_HTML);
			// 去除重复连接
			if (isUnique && elementList.contains(element))
			{
				continue;
			}
			elementList.add(element);
		}

		return elementList;
	}

}
