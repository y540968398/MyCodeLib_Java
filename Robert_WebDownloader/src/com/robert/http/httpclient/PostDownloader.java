package com.robert.http.httpclient;

import org.apache.http.HttpEntity;
import org.apache.http.client.methods.HttpPost;

public class PostDownloader extends ABSDownloader implements IHttpDownloader
{
	public PostDownloader()
	{
		request = new HttpPost();
	}

	public void setEntity(HttpEntity entity)
	{
		HttpPost post = (HttpPost) request;
		post.setEntity(entity);
	}

}
