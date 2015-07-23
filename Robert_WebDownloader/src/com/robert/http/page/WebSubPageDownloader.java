package com.robert.http.page;

import java.util.List;

import org.apache.log4j.Logger;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.robert.common.cfglog.CfgConstants;
import com.robert.common.cfglog.CfgUtil;
import com.robert.common.thread.pool.IThreadPoolExecutor;
import com.robert.http.constants.WebConstants;
import com.robert.http.httpclient.IHttpDownloader;
import com.robert.http.httpclient.thread.HttpDownloadRunner;
import com.robert.http.parse.jsoup.JsoupUtil;

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

	public WebSubPageDownloader(String pageName, String pageUrl, IThreadPoolExecutor<HttpDownloadRunner> executor)
	{
		super(pageName, pageUrl, executor);
	}

	public WebSubPageDownloader(String pageName, String pageUrl, IThreadPoolExecutor<HttpDownloadRunner> executor,
	        IHttpDownloader pageDownloader)
	{
		super(pageName, pageUrl, executor, pageDownloader);
	}

	@Override
	public void downloadPage()
	{
		super.isSavePage = false;
		super.downloadPage();
		if (isSaveSub)
		{
			downloadSubPage();
		}
		super.savePage();
	}

	public void downloadSubPage()
	{
		// 解析链接，并修改连接地址到相对路径
		List<Element> subLinkList = JsoupUtil.getDownloadSubLinks(document, this.pageUrl, this.pageName, "");
		for (Element element : subLinkList)
		{
			// 下载连接，并指定绝对路径(配置路径 + 链接的相对路径)
			new WebSubPageDownloader(element.attr(WebConstants.ATTR_ORIGINAL_URL),
			        CfgUtil.get(CfgConstants.DIR_PAGE_DOWNLOAD) + element.attr(WebConstants.ATTR_HREF), executor);
		}

		logger.info("WebPage[" + this.pageUrl + "] download successed !");
	}

}
