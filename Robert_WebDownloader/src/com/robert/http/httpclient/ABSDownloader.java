package com.robert.http.httpclient;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.CoreConnectionPNames;
import org.apache.log4j.Logger;

import com.robert.http.httputil.HttpUtils;

public abstract class ABSDownloader implements IHttpDownloader
{
	private Logger logger = Logger.getLogger(GetDownloader.class);

	// 用户定义参数
	protected String filePath;
	protected String urlString;

	// 请求参数部分
	/** http 请求执行者 (可复用) */
	protected HttpClient httpClient;
	/** http 请求实体 (每次新建或传入) */
	protected HttpUriRequest request;
	/** http 请求报文头部 */
	protected Header[] headers;
	/** http 请求编码格式 */
	protected String charSet;

	@Override
	public void setHttpClient(HttpClient httpClient)
	{
		this.httpClient = httpClient;
	}

	@Override
	public HttpClient getHttpClient()
	{
		return this.httpClient;
	}

	// 反馈信息部分
	/** http 请求响应状态码 */
	protected int responseStatus;

	@SuppressWarnings("deprecation")
	public void downPage(String url, String filePath)
	{
		// 初始化
		init(url, filePath);

		// 发送请求至服务器
		/** http 响应实体 */
		HttpResponse response = null;
		int reTryCnt = 0;
		do
		{
			try
			{
				httpClient.getParams().setParameter(CoreConnectionPNames.CONNECTION_TIMEOUT, 60000);
				response = httpClient.execute(request);

				logger.debug("Visit URL : " + request.getURI());
			}
			catch (Exception e)
			{
				logger.error("Get request failed ! ", e);
			}
			logger.debug("Send get request to :" + urlString);
			reTryCnt++;
		}
		while (response == null && reTryCnt < 3);

		if (null == response)
		{
			logger.error("Get response null for page : " + url);
			return;
		}
		// 接收服务器响应信息
		responseStatus = response.getStatusLine().getStatusCode();
		if (responseStatus != HttpStatus.SC_OK)
		{
			this.filePath = this.filePath + ".html";
		}

		// 保存服务器响应文件
		HttpEntity entity = response.getEntity();
		headers = response.getAllHeaders();
		try
		{
			HttpUtils.readResponse(entity.getContent(), this.filePath);
		}
		catch (Exception e)
		{
			logger.error("Get response stream failed ! ", e);
		}
		logger.debug("Successed save page[" + url + "] to " + this.filePath);
		logger.debug("Response status : " + responseStatus);

		request = null;
	}

	/**
	 * 初始化参数及请求
	 * 
	 * @param url
	 *            访问的地址
	 * @param filePath
	 *            文件保存地址
	 */
	private void init(String url, String filePath)
	{
		// 参数初始化
		this.urlString = url;
		this.filePath = filePath;

		if (null == httpClient)
		{
			httpClient = new DefaultHttpClient();
		}

		// 默认使用 get 请求
		if (null == request)
		{
			request = new HttpGet(urlString);
		}
	}

	public int getResponseStatus()
	{
		return responseStatus;
	}

	public Header[] getResponseHeaders()
	{
		return headers;
	}

	public void setReqeust(HttpUriRequest request)
	{
		this.request = request;
	}

	public void setHeaders(Header[] headers)
	{
		request.setHeaders(headers);
	}

}
