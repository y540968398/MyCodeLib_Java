package com.robert.common.web.http;

import org.junit.Assert;
import org.junit.Test;

public class URLUtilsTest
{

	@Test
	public void getSubRelativeLink_sameLevel()
	{
		String sub1 = URLUtils.getSubRelativeLink("http://domain.com/sixthpat/contents.html",
		        "http://domain.com/sixthpat/test.html");
		Assert.assertEquals("/test.html", sub1);
	}

	@Test
	public void getSubRelativeLink_subLevel()
	{
		String sub2 = URLUtils.getSubRelativeLink("http://domain.com/sixthpat/contents.html",
		        "http://domain.com/sixthpat/subpath/test.html");
		Assert.assertEquals("/subpath/test.html", sub2);
	}

	@Test
	public void getSubRelativeLink_subLevel1()
	{
		String sub2 = URLUtils.getSubRelativeLink("http://domain.com/sixthpat/contents.html",
		        "http://domain.com/sixthpat/subpath/sub2/test.html");
		Assert.assertEquals("/subpath/sub2/test.html", sub2);
	}

	@Test
	public void getSubRelativeLink_subLevel3()
	{
		String sub2 = URLUtils.getSubRelativeLink("http://domain.com/sixthpat/contents.html",
		        "http://domain.com/sixthpat/subpath/sub2/test.html");
		Assert.assertEquals("/subpath/sub2/test.html", sub2);
	}

	
	@Test
	public void getSubLinkURL_parentUrl()
	{
		String sub2 = URLUtils.getSubLinkURL("http://domain.com/sixthpat/subpath/sub2/test.html",
				"../../haha.html");
		Assert.assertEquals("http://domain.com/sixthpat/haha.html", sub2);
	}
}
