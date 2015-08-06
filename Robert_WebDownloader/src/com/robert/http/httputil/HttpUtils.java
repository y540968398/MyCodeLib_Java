package com.robert.http.httputil;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.log4j.Logger;

import com.robert.common.file.FileUtil;

public class HttpUtils
{

	private static Logger logger = Logger.getLogger(HttpUtils.class);

	/**
	 * 读取服务器返回的资源，并保存至文件。
	 * 
	 * @param in
	 *            服务器返回的字节流
	 * @param filePath
	 *            保存的文件路径
	 */
	public static void readResponse(InputStream in, String filePath)
	{
		try
		{
			logger.debug("Response len : " + in.available());
			FileUtil.createFileIfNotExists(filePath);
			File outputFile = new File(filePath);
			BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(new FileOutputStream(outputFile));
			try
			{
				byte[] content = new byte[1024];
				int len = 0;
				while ((len = in.read(content, 0, 1024)) > 0)
				{
					bufferedOutputStream.write(content, 0, len);
				}
			}
			finally
			{
				bufferedOutputStream.flush();
				bufferedOutputStream.close();
			}

		}
		catch (Exception e)
		{
			logger.error("Download file failed !" + filePath, e);
		}
	}

	/**
	 * post json 数据格式
	 * 
	 * @param json
	 *            请求参数
	 * @param charset
	 *            请求编码
	 * @param contentType
	 *            内容格式
	 * @return
	 */
	public static StringEntity getJsonEntity(String json, String charset, String contentType)
	{
		StringEntity jsonEntity = new StringEntity(json, charset);
		jsonEntity.setContentEncoding(charset);
		jsonEntity.setContentType(contentType);

		return jsonEntity;
	}

	/**
	 * 二进制数据格式entity
	 * 
	 * @param stringParamMap
	 *            string参数键值对
	 * @param binParamMap
	 *            文件参数键值对
	 */
	public static HttpEntity getBinaryEntity(Map<String, String> stringParamMap, Map<String, String> binParamMap)
	{
		MultipartEntityBuilder binaryEntity = MultipartEntityBuilder.create();

		try
		{
			for (Map.Entry<String, String> entry : stringParamMap.entrySet())
			{
				binaryEntity.addPart(entry.getKey(), new StringBody(entry.getValue()));
			}
		}
		catch (UnsupportedEncodingException e)
		{
			logger.error("Get binary entity failed ! add StringBody error !", e);
		}

		for (Map.Entry<String, String> entry : binParamMap.entrySet())
		{
			binaryEntity.addBinaryBody(entry.getKey(), new File(entry.getValue()));
		}

		return binaryEntity.build();
	}

	/**
	 * 设置 post/get string类型的参数
	 * 
	 * @param paramsMap
	 *            参数键值对
	 */
	public static HttpEntity getStringEntity(Map<String, String> paramsMap)
	{
		List<NameValuePair> nameValuePairList = new ArrayList<NameValuePair>(paramsMap.size());

		for (Map.Entry<String, String> entry : paramsMap.entrySet())
		{
			NameValuePair nameValuePair = new BasicNameValuePair(entry.getKey(), String.valueOf(entry.getValue()));
			nameValuePairList.add(nameValuePair);
		}

		try
		{
			return new UrlEncodedFormEntity(nameValuePairList);
		}
		catch (UnsupportedEncodingException e)
		{
			logger.error("Get name value pair entity failed !", e);
		}

		return null;
	}

	public static void doPost(String url, Map<String, String> stringParamMap, Map<String, String> binParamMap)
	{
		HttpClient httpClient = new DefaultHttpClient();

		HttpPost post = new HttpPost(url);
		if (null == binParamMap)
		{
			post.setEntity(getStringEntity(stringParamMap));
		}
		else
		{
			post.setEntity(getBinaryEntity(stringParamMap, binParamMap));
		}

		try
		{
			httpClient.execute(post);
		}
		catch (Exception e)
		{
			logger.error("Sent post request to url[" + url + "] error!", e);
		}
		post.abort();
	}

}
