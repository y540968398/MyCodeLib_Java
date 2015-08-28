package com.robert.http.parse.jsoup.util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.List;

import org.apache.log4j.Logger;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.robert.common.file.DirectoryUtils;
import com.robert.common.file.FileUtil;

public class HtmlFileParse
{

	private static Logger logger = Logger.getLogger(HtmlFileParse.class);

	private String srcHtmlPath;

	private Document document;
	private String title = "";
	private String content = "";

	public HtmlFileParse(String filePath)
	{
		this.srcHtmlPath = filePath;
	}

	public void parseFileInfo()
	{
		document = JsoupUtil.getDocFromFile(srcHtmlPath);

		Elements elements = document.getElementsByTag("title");
		for (Element element : elements)
		{
			title = element.text().substring("星云法师：释迦牟尼佛传".length()+1);
		}

		Elements elements2 = document.getElementsByAttributeValue("class", "zwzw");
		for (Element element : elements2)
		{
			content = element.html();
		}
		
	}

	public void saveContentByTitle()
	{
		parseFileInfo();
		String filePath = DirectoryUtils.getDir(srcHtmlPath) + title + ".html";
		try
		{
			FileOutputStream fileOutputStream = new FileOutputStream(new File(filePath));
			try
			{
				fileOutputStream.write(content.getBytes("UTF-8"));
			}
			finally
			{
				fileOutputStream.close();
			}
		}
		catch (Exception e)
		{
			logger.error("Write content to file" + filePath + " failed !", e);
		}
	}

	public static void main(String[] args)
	{
		List<File> fileList = null;
		try
		{
			fileList = FileUtil.listFiles("F:/webDownloader/shijiamonizhuan", new String[] { ".htm", ".html" });
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}

		try
		{
			for (File file : fileList)
			{
				new HtmlFileParse(file.getCanonicalPath().replace("\\", "/")).saveContentByTitle();
			}
		}
		catch (Exception e)
		{
			logger.error("ErrorMsg!", e);
		}
	}
	

}
