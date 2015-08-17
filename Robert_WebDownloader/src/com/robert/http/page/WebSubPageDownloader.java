package com.robert.http.page;

import java.util.List;

import org.apache.log4j.Logger;
import org.jsoup.nodes.Element;

import com.robert.common.cfglog.CfgConstants;
import com.robert.common.cfglog.CfgUtil;
import com.robert.http.constants.WebConstants;
import com.robert.http.httpclient.IHttpDownloader;
import com.robert.http.httpclient.thread.WebPageDownloadRunner;
import com.robert.http.page.cache.PageDownloadCache;
import com.robert.http.page.launch.WebDownloaderLaunch;
import com.robert.http.parse.jsoup.util.JsoupUtil;

/**
 * 页面及子页面下载器
 * 
 * @see WebPageDownloader 页面及资源下载器
 * 
 * @description 1.下载指定的URL页面到指定文件，并将文件加载到内存当中。<br/>
 *              2.使用线程池下载页面链接的资源文件(CSS,JS,IMG)，并将资源链接地址修改为相对路径,
 *              <B><U>资源文件下载到一个与页面名称相同的文件夹中</U></B><br/>
 *              3.下载页面中的 链接 页面<br/>
 *              4.将内存中修改过相对路径的页面覆盖保存到原路径
 */
public class WebSubPageDownloader extends WebPageDownloader
{

	// 第三方辅助参数
	private Logger logger = Logger.getLogger(WebSubPageDownloader.class);

	// 功能控制参数
	/** 是否下载子页面 */
	private boolean isSaveSub = CfgUtil.getBoolean(CfgConstants.IS_DOWNLOAD_SUB);

	// 业务控制参数
	public WebSubPageDownloader(String rootUrl, String rootUrlName, String pageUrl, String pageUrlName)
	{
		super(rootUrl, rootUrlName, pageUrl, pageUrlName);

	}

	public WebSubPageDownloader(String rootUrl, String rootUrlName, String pageUrl, String pageUrlName,
	        IHttpDownloader pageDownloader)
	{
		super(rootUrl, rootUrlName, pageUrl, pageUrlName, pageDownloader);
	}

	@Override
	public void downloadPage()
	{
		super.isSavePage = false;
		super.downloadPage();
		if (isSaveSub && null != document)
		{
			downloadSubPage();
			super.savePage();
		}
	}

	private void downloadSubPage()
	{
		// 解析链接，并修改连接地址到相对路径
		List<Element> subLinkList = JsoupUtil.getDownloadSubLinks(document, this.pageUrl);
		for (Element element : subLinkList)
		{
			// 页面原始地址
			String subPageUrl = element.attr(WebConstants.ATTR_ORIGINAL_URL);

			if (subPageUrl
			        .startsWith("http://www.drbachinese.org/online_reading_simplified/sutra_explanation/SixthPat/tanjing"))
			{
				logger.error("link error !");
			}

			// 判断页面是否已经下载过
			if (PageDownloadCache.contains(subPageUrl))
			{
				if (subPageUrl.equals(this.rootUrl))
				{
					element.attr(WebConstants.ATTR_HREF, WebConstants.SURFIX_HTML);
				}
				else if (!element.attr(WebConstants.ATTR_HREF).equals(PageDownloadCache.getUrlCachedPath(subPageUrl)))
				{
					element.attr(WebConstants.ATTR_HREF, PageDownloadCache.getUrlCachedPath(subPageUrl));
					logger.debug("走到这里说明 路径 拼接有问题，同一个地址的相对路径应该是一致的！");
				}
				continue;
			}

			// 将相对路径修改为 与 根目录相对的路径：即 相对路径 加上根路径
			String relativePath = this.rootDirName +"/"+ element.attr(WebConstants.ATTR_HREF);
//			element.attr(WebConstants.ATTR_HREF, relativePath);

			// 下载连接，并指定绝对路径(配置路径 + 链接的相对路径)
			WebDownloaderLaunch.pagePoolExecutor.addTask(new WebPageDownloadRunner(new WebSubPageDownloader(
			        this.rootUrl, this.rootDirName, subPageUrl, relativePath)));
		}

		logger.info("WebPage[" + this.pageUrl + "] download successed !");
	}
}
