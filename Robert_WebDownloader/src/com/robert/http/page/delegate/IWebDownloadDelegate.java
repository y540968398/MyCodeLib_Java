package com.robert.http.page.delegate;

import com.robert.http.page.bean.PageDealStatus;

/**
 * 
 * 功能扩展点：用于对下载页面关键的位置操作进行委派
 *
 */
public interface IWebDownloadDelegate
{

	void startDownloadPage(PageDealStatus pageDealStatus);

	void startDownloadImage(PageDealStatus pageDealStatus);

	void startDownloadCss(PageDealStatus pageDealStatus);

	void startDownloadJS(PageDealStatus pageDealStatus);

	void endDownloadImage(PageDealStatus pageDealStatus);

	void endDownloadCss(PageDealStatus pageDealStatus);

	void endDownloadJS(PageDealStatus pageDealStatus);

	void endDownloadPage(PageDealStatus pageDealStatus);

}
