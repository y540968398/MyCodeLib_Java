package com.robert.http.httpclient;

import org.apache.http.Header;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpUriRequest;

public interface IHttpDownloader
{

	/**
	 * 下载页面
	 * 
	 * @param filePath
	 *            页面下载路径
	 */
	void downPage(String url, String filePath);

	/**
	 * 设置请求体 get/post
	 * 
	 * @param request
	 */
	void setReqeust(HttpUriRequest request);

	/**
	 * 设置 httpClient
	 * 
	 * @param httpClient
	 */
	void setHttpClient(HttpClient httpClient);

	/**
	 * 获取 httpClient 用于 session 共享与 cookie 重用
	 * 
	 * @param httpClient
	 */
	HttpClient getHttpClient();

	/**
	 * 设置http请求报文信息
	 * 
	 * @param headers
	 *            报文头部信息
	 */
	void setHeaders(Header[] headers);

	/**
	 * 获得访问的请求报文头部信息
	 * 
	 * @return Header[]
	 */
	Header[] getResponseHeaders();

	/**
	 * 获取请求结果状态
	 * 
	 * @return int
	 */
	int getResponseStatus();

}
