package com.robert.common.web.http;

import org.junit.Assert;
import org.junit.Test;

public class URLUtilsTest
{

	@Test
	public void getSubRelativeLink_01()
	{
		String sub1 = URLUtils.getPath2SubLink("http://domain.com/sixthpat/contents.html",
		        "http://domain.com/sixthpat/test.html");
		Assert.assertEquals("/test.html", sub1);
	}

	@Test
	public void getSubRelativeLink_02()
	{
		String sub2 = URLUtils.getPath2SubLink("http://domain.com/sixthpat/contents.html",
		        "http://domain.com/sixthpat/subpath/test.html");
		Assert.assertEquals("/subpath/test.html", sub2);
	}

	@Test
	public void getSubRelativeLink_03()
	{
		String sub2 = URLUtils.getPath2SubLink("http://domain.com/sixthpat/contents.html",
		        "http://domain.com/sixthpat/subpath/sub2/test.html");
		Assert.assertEquals("/subpath/sub2/test.html", sub2);
	}

	@Test
	public void getSubRelativeLink_04()
	{
		String sub2 = URLUtils.getPath2SubLink("http://domain.com/sixthpat/contents.html",
		        "http://domain.com/sixthpat/subpath/sub2/test.html");
		Assert.assertEquals("/subpath/sub2/test.html", sub2);
	}

	@Test
	public void getSubLinkURL_01()
	{
		String sub2 = URLUtils.getSubLinkURL("http://domain.com/sixthpat/sub1/sub2/test.html", "../../haha.html");
		Assert.assertEquals("http://domain.com/sixthpat/haha.html", sub2);
	}

	@Test
	public void getSubLinkURL_02()
	{
		String sub2 = URLUtils.getSubLinkURL("http://domain.com/sixthpat/sub1/sub2/test.html", "../haha.html");
		Assert.assertEquals("http://domain.com/sixthpat/sub1/haha.html", sub2);
	}

	@Test
	public void getSubLinkURL_03()
	{
		String sub2 = URLUtils.getSubLinkURL("http://domain.com/sixthpat/sub1/sub2/test.html", "../");
		Assert.assertEquals("http://domain.com/sixthpat/sub1/", sub2);
	}

	@Test
	public void getSubLinkURL_04()
	{
		String sub2 = URLUtils.getSubLinkURL("http://domain.com/sixthpat/sub1/sub2/test.html", "../../../../../");
		Assert.assertEquals("http:/", sub2);
	}

	@Test
	public void getSubLinkURL_05()
	{
		String sub2 = URLUtils.getSubLinkURL("http://domain.com/sixthpat/sub1/sub2/test.html", "../../../../../../");
		Assert.assertEquals(null, sub2);
	}

	@Test
	public void getSubLinkURL_06()
	{
		String sub2 = URLUtils.getSubLinkURL(
		        "http://www.drbachinese.org/online_reading_simplified/sutra_explanation/SixthPat/sixthpatSutra.htm",
		        "#top");
		Assert.assertEquals(
		        "http://www.drbachinese.org/online_reading_simplified/sutra_explanation/SixthPat/sixthpatSutra.htm",
		        sub2);
	}

	@Test
	public void getUrlPathNoneDomain_01()
	{
		String url = "http://www.sina.com.cn/path1/path2/path3/contents.html";
		String path = URLUtils.getUrlPathNoneDomain(url);

		Assert.assertEquals("/path1/path2/path3/", path);
	}

	@Test
	public void getUrlPathNoneDomain_02()
	{
		String url = "www.sina.com.cn/path1/path2/path3/contents.html";
		String path = URLUtils.getUrlPathNoneDomain(url);

		Assert.assertEquals("/path1/path2/path3/", path);
	}

	@Test
	public void getUrlPathNoneDomain_03()
	{
		String url = "/path1/path2/path3/contents.html";
		String path = URLUtils.getUrlPathNoneDomain(url);

		Assert.assertEquals("/path1/path2/path3/", path);
	}

	@Test
	public void getUrlPathNoneDomain_04()
	{
		String url = "path1/path2/path3/contents.html";
		String path = URLUtils.getUrlPathNoneDomain(url);

		Assert.assertEquals("path1/path2/path3/", path);
	}

	@Test
	public void getUrlPathNoneDomain_05()
	{
		String url = "../path1/path2/path3/contents.html";
		String path = URLUtils.getUrlPathNoneDomain(url);

		Assert.assertEquals("../path1/path2/path3/", path);
	}

}
