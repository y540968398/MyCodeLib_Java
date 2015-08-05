package com.robert.http.page;

import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.http.HttpStatus;
import org.apache.log4j.Logger;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import com.robert.common.cfglog.CfgConstants;
import com.robert.common.cfglog.CfgUtil;
import com.robert.common.thread.pool.IThreadPoolExecutor;
import com.robert.http.constants.WebConstants;
import com.robert.http.httpclient.GetDownloader;
import com.robert.http.httpclient.IHttpDownloader;
import com.robert.http.httpclient.thread.HttpDownloadRunner;
import com.robert.http.page.bean.PageDealStatus;
import com.robert.http.page.delegate.CacheDelegate;
import com.robert.http.page.delegate.IWebDownloadDelegate;
import com.robert.http.parse.jsoup.util.JsoupUtil;

/**
 * 页面及资源下载器
 * 
 * @see WebSubPageDownloader 页面及子页面下载器
 * 
 * @description 1.下载指定的URL页面到指定文件，并将文件加载到内存当中。<U><B>(并不下载页面的子链接)</B></U><br/>
 *              2.使用线程池下载页面链接的资源文件(CSS,JS,IMG)，并将资源链接地址修改为相对路径,
 *              <B><U>资源文件下载到一个与页面名称相同的文件夹中</U></B><br/>
 *              3.将内存中修改过相对路径的页面覆盖保存到原路径
 */
public class WebPageDownloader
{
	// 第三方参数
	private Logger logger = Logger.getLogger(WebPageDownloader.class);

	// 功能技术参数
	/** 是否直接下载页面，用于子页面下载时，控制该类的行为。 */
	boolean isSavePage = true;
	/** 是否直接下载图片 */
	boolean isSaveImage = CfgUtil.getBoolean(CfgConstants.IS_DOWNLOAD_IMG);
	/** 是否直接下载CSS */
	boolean isSaveCss = CfgUtil.getBoolean(CfgConstants.IS_DOWNLOAD_CSS);
	/** 是否直接下载JS */
	boolean isSaveJS = CfgUtil.getBoolean(CfgConstants.IS_DOWNLOAD_JS);
	/** 页面文档 */
	Document document;
	/** 页面下载器，get post 下载器 */
	IHttpDownloader pageDownloader;
	/** 线程池执行者:仅用于资源下载 */
	IThreadPoolExecutor<HttpDownloadRunner> resourceExecutor;
	/** 功能委派：默认为缓存委派 */
	IWebDownloadDelegate delegate = new CacheDelegate();
	/** 页面处理状态 */
	PageDealStatus pageDealStatus;

	// 业务参数
	/** 根目录页面地址:域名 或 指定的页面 */
	String rootUrl;
	/** 根目录页面保存名称 */
	String rootUrlName;
	/** 下载的页面地址 */
	String pageUrl;
	/**
	 * 下载的页面的相对路径+下载的页面名称！ <br/>
	 * 如果没有指定名称，取当前url与根url的相对路径 + 网页地址中的最后一部分作为当前页面名称。如果指定名称，则以下载的名称为准
	 */
	String pageUrlName;
	/**
	 * 页面保存地址
	 * 
	 * @description 1.根目录页面保存路径：<br/>
	 *              配置根目录/rootUrlName.html 2.子页面保存路径：
	 *              配置根目录/rootUrlName/pageUrlName[相对路径/子页面名称].html<br/>
	 *              相对路径：根据 rootUrl 与 pageUrl 比对，产生相对路径<br/>
	 *              子页面名称：如果未指定名称，则获取url中最后一部分作为文件名称
	 */
	String pageSavePath;

	/**
	 * 下载页面
	 * 
	 * @description 1.页面下载到指定目录中，并以页面名称进行命名。<br/>
	 *              页面下载路径：pro:{dir_page_download}/pageName.html<br/>
	 *              2.改方法必须在主线程中进行操作，因为页面下载完毕才能去下载资源。
	 */
	protected void downloadDocument()
	{
		if (StringUtils.isNotEmpty(pageUrlName) && !rootUrl.equals(pageUrl))
		{
			// 子页面
			pageSavePath = CfgUtil.get(CfgConstants.DIR_PAGE_DOWNLOAD) + this.rootUrlName + this.pageUrlName
			        + WebConstants.SURFIX_HTML;
		}
		else
		{
			// 根页面
			pageSavePath = CfgUtil.get(CfgConstants.DIR_PAGE_DOWNLOAD) + this.rootUrlName + WebConstants.SURFIX_HTML;
		}

		GetDownloader getDownloader = new GetDownloader();
		getDownloader.downPage(this.pageUrl, pageSavePath);
		if (getDownloader.getResponseStatus() != HttpStatus.SC_OK)
		{
			logger.error("Download page error !");
			return;
		}
		document = JsoupUtil.getDocFromFile(pageSavePath);
	}

	/**
	 * 下载图片
	 * 
	 * @description 1.下载当前页面关联的图片资源，下载到当前页面名称对应的文件夹中，并将当前页面中 img 的链接修改为相对路径。<br/>
	 *              2.页面图片下载路径:pro:{dir_page_download}/pageName/pro:{
	 *              dir_img_download}<br/>
	 *              3.使用线程池进行下载
	 */
	protected void downloadImages()
	{
		// 修改资源地址到相对路径
		List<Element> downLoadImgList = JsoupUtil.getDownloadImages(document, this.pageUrl,
		        this.rootUrl + CfgUtil.get(CfgConstants.DIR_IMG_DOWNLOAD));
		for (Element element : downLoadImgList)
		{
			// 下载资源时，指定绝对路径
			resourceExecutor.addTask(new HttpDownloadRunner(element.attr(WebConstants.ATTR_ORIGINAL_URL), CfgUtil
			        .get(CfgConstants.DIR_PAGE_DOWNLOAD) + element.attr(WebConstants.ATTR_SRC)));
		}
		logger.debug("Add image download task to queue!");
	}

	/**
	 * 下载CSS样式表
	 * 
	 * @description 1.下载当前页面关联的css样式资源，下载到当前页面名称对应的文件夹中，并将当前页面中 css样式
	 *              的链接修改为相对路径。<br/>
	 *              2.页面图片下载路径:pro:{dir_page_download}/pageName/pro:{
	 *              dir_css_download}<br/>
	 *              3.使用线程池进行下载
	 * 
	 */
	protected void downloadCss()
	{
		// 修改资源地址到相对路径
		List<Element> downloadCssList = JsoupUtil.getDownloadCss(document, this.pageUrl,
		        this.rootUrl + CfgUtil.get(CfgConstants.DIR_CSS_DOWNLOAD));
		for (Element element : downloadCssList)
		{
			// 下载资源时，指定绝对路径
			resourceExecutor.addTask(new HttpDownloadRunner(element.attr(WebConstants.ATTR_ORIGINAL_URL), CfgUtil
			        .get(CfgConstants.DIR_PAGE_DOWNLOAD) + element.attr(WebConstants.ATTR_HREF)));
		}
		logger.debug("Add CSS download task to queue!");
	}

	/**
	 * 下载JS资源
	 * 
	 * @description 1.下载当前页面关联的 JS 资源，下载到当前页面名称对应的文件夹中，并将当前页面中 JS资源 的链接修改为相对路径。<br/>
	 *              2.页面图片下载路径:pro:{dir_page_download}/pageName/pro:{
	 *              dir_js_download}<br/>
	 *              3.使用线程池进行下载
	 */
	protected void downloadJS()
	{
		// 修改资源地址到相对路径
		List<Element> downloadJsList = JsoupUtil.getDownloadJS(document, this.pageUrl,
		        this.rootUrl + CfgUtil.get(CfgConstants.DIR_JS_DOWNLOAD));
		for (Element element : downloadJsList)
		{
			// 下载资源时，指定绝对路径
			resourceExecutor.addTask(new HttpDownloadRunner(element.attr(WebConstants.ATTR_ORIGINAL_URL), CfgUtil
			        .get(CfgConstants.DIR_PAGE_DOWNLOAD) + element.attr(WebConstants.ATTR_SRC)));
		}
		logger.debug("Add JS download task to queue!");
	}

	public IHttpDownloader getPageDownloader()
	{
		return pageDownloader;
	}

	public void setPageDownloader(IHttpDownloader pageDownloader)
	{
		this.pageDownloader = pageDownloader;
	}

	public IWebDownloadDelegate getDelegate()
	{
		return delegate;
	}

	public void setDelegate(IWebDownloadDelegate delegate)
	{
		this.delegate = delegate;
	}

	/**
	 * 页面下载器
	 * 
	 * @param rootUrl
	 *            根url
	 * @param rootUrlName
	 *            根url页面 和 目录 名称
	 * @param pageUrl
	 *            当前下载url地址
	 * @param pageUrlName
	 *            当前下载url 相对路径 与 名称
	 * @param executor
	 *            资源下载线程池
	 */
	public WebPageDownloader(String rootUrl, String rootUrlName, String pageUrl, String pageUrlName,
	        IThreadPoolExecutor<HttpDownloadRunner> executor)
	{
		this.rootUrl = rootUrl;
		this.rootUrlName = rootUrlName;
		this.pageUrl = pageUrl;
		this.pageUrlName = pageUrlName;
		this.resourceExecutor = executor;
		this.pageDownloader = new GetDownloader();
		this.pageDealStatus = new PageDealStatus(rootUrl, rootUrlName, pageUrl, pageUrlName);
	}

	/**
	 * 页面下载器
	 * 
	 * @param rootUrl
	 *            根url
	 * @param rootUrlName
	 *            根url页面 和 目录 名称
	 * @param pageUrl
	 *            当前下载url地址
	 * @param pageUrlName
	 *            当前下载url 相对路径 与 名称
	 * @param executor
	 *            资源下载线程池
	 * @param pageDownloader
	 *            HTTP请求下载器(POST GET)
	 */
	public WebPageDownloader(String rootUrl, String rootUrlName, String pageUrl, String pageUrlName,
	        IThreadPoolExecutor<HttpDownloadRunner> executor, IHttpDownloader pageDownloader)
	{
		this.rootUrl = rootUrl;
		this.rootUrlName = rootUrlName;
		this.pageUrl = pageUrl;
		this.pageUrlName = pageUrlName;
		this.resourceExecutor = executor;
		this.pageDownloader = pageDownloader;
		this.pageDealStatus = new PageDealStatus(rootUrl, rootUrlName, pageUrl, pageUrlName);
	}

	public void downloadPage()
	{
		// 下载页面
		delegate.startDownloadPage(pageDealStatus);
		downloadDocument();
		// 下载 CSS 资源文件
		if (isSaveCss)
		{
			downloadCss();
		}
		// 下载 图片 资源文件
		if (isSaveImage)
		{
			downloadImages();
		}
		// 下载 JS 资源文件
		if (isSaveJS)
		{
			downloadJS();
		}
		// 是否保存修改后的 document
		if (isSavePage)
		{
			savePage();
		}
	}

	/**
	 * 保存加载到内存中的页面文件
	 * 
	 * @description 下载页面后，需要下载页面中链接的资源文件(CSS,IMG,JS)，并且将其中链接的地址修改为下载后的相对地址。<br/>
	 *              这里既是将修改了资源地址后的页面源码，覆盖保存至源文件的操作。
	 */
	public void savePage()
	{
		JsoupUtil.saveDocument(document, pageSavePath);
		delegate.endDownloadPage(pageDealStatus);
		logger.info("Success save document[] to " + pageSavePath);
	}

}
