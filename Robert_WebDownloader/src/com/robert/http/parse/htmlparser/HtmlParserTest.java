package com.robert.http.parse.htmlparser;

import java.io.File;

import org.apache.log4j.Logger;
import org.htmlparser.Node;
import org.htmlparser.NodeFilter;
import org.htmlparser.Parser;
import org.htmlparser.filters.HasAttributeFilter;
import org.htmlparser.filters.LinkStringFilter;
import org.htmlparser.filters.TagNameFilter;
import org.htmlparser.tags.ImageTag;
import org.htmlparser.util.NodeList;

import com.robert.common.cfglog.CfgConstants;
import com.robert.common.cfglog.CfgUtil;
import com.robert.common.file.FileUtil;
import com.robert.http.constants.WebConstants;
import com.robert.http.httpclient.GetDownloader;

public class HtmlParserTest
{

	private static Logger logger = Logger.getLogger(HtmlParserTest.class);

	public void parsePath(String filePath)
	{
		Parser htmlParser = Parser.createParser(FileUtil.getFileAsString(filePath), "UTF-8");
		NodeFilter filter = new HasAttributeFilter("class", "bookcont");

		try
		{
			NodeList nodeList = htmlParser.extractAllNodesThatMatch(filter);
			for (Node node : nodeList.toNodeArray())
			{
				logger.info(node.getText());
				Parser htmlParser2 = Parser.createParser(node.toHtml(), "UTF-8");
				NodeFilter fileFilter = new LinkStringFilter("");

				NodeList nodeList2 = htmlParser2.extractAllNodesThatMatch(fileFilter);
				logger.info(nodeList2.toNodeArray().length);
				for (Node node2 : nodeList2.toNodeArray())
				{
					logger.info(node2.toPlainTextString() + ":\t\t" + node2.toHtml());
					return;
				}
			}
		}
		catch (Exception e)
		{
			logger.error("Parse file by filter error ! ", e);
		}
	}

	public void parseImg(String filePath)
	{
		Parser htmlParser = Parser.createParser(FileUtil.getFileAsString(filePath), "UTF-8");
		NodeFilter filter = new TagNameFilter(WebConstants.TAG_IMG);
		
		try
        {
	        NodeList imgList = htmlParser.extractAllNodesThatMatch(filter); 
	        for (Node img : imgList.toNodeArray())
            {
	        	ImageTag imgTag = (ImageTag) img;
	        	imgTag.setAttribute(WebConstants.ATTR_SRC, "new img src");
	            System.out.println(img.toHtml());
            }
        }
        catch (Exception e)
        {
	        logger.error("ParseImg error !", e);
        }
		System.out.println(htmlParser);
	}

	public static void main(String[] args)
	{
		CfgUtil.initConfig("/cfg/");
		String filePath = CfgUtil.get(CfgConstants.DIR_PAGE_DOWNLOAD) + "HuangDiNeiJing_Index.html";
		String url = "http://so.gushiwen.org/guwen/book_10.aspx";
		File page = new File(filePath);
		if (!page.exists() || !page.isFile())
		{
			new GetDownloader().downPage(url, filePath);
		}
//		new HtmlParserTest().parsePath(filePath);
		new HtmlParserTest().parseImg(filePath);
	}
}
