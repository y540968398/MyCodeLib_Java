package com.robert.http.page.bean;

import com.robert.http.enums.DownloadStatus;

public class PageDealStatus
{

	private String curUrl;
	private String relativePath;

	private String rootUrl;
	private String rootPath;

	private DownloadStatus status;

	public PageDealStatus(String rootUrl, String rootPath, String curUrl, String relativePath)
	{
		this.curUrl = curUrl;
		this.relativePath = relativePath;
		this.rootUrl = rootUrl;
		this.rootPath = rootPath;
		this.status = DownloadStatus.DOWNLOADING;
	}

	public String getCurUrl()
	{
		return curUrl;
	}

	public void setCurUrl(String curUrl)
	{
		this.curUrl = curUrl;
	}

	public String getRelativePath()
	{
		return relativePath;
	}

	public void setRelativePath(String relativePath)
	{
		this.relativePath = relativePath;
	}

	public String getRootUrl()
	{
		return rootUrl;
	}

	public void setRootUrl(String rootUrl)
	{
		this.rootUrl = rootUrl;
	}

	public String getRootPath()
	{
		return rootPath;
	}

	public void setRootPath(String rootPath)
	{
		this.rootPath = rootPath;
	}

	public DownloadStatus getStatus()
	{
		return status;
	}

	public void setStatus(DownloadStatus status)
	{
		this.status = status;
	}

	@Override
	public String toString()
	{
		return "PageDealStatus [curUrl=" + curUrl + ", relativePath=" + relativePath + ", rootUrl=" + rootUrl
		        + ", rootPath=" + rootPath + ", status=" + status + "]";
	}

}
