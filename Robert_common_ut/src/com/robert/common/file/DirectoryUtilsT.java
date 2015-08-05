package com.robert.common.file;

import org.junit.Assert;
import org.junit.Test;

public class DirectoryUtilsT
{

	@Test
	public void getParentLevel_test01()
	{
		int level = DirectoryUtils.getParentLevel("../../../../sub1/subhtml.html");
		Assert.assertEquals(4, level);
	}

	@Test
	public void getParentLevel_test02()
	{
		int level = DirectoryUtils.getParentLevel("../sub1/subhtml.html");
		Assert.assertEquals(1, level);
	}

	@Test
	public void getParentLevel_test03()
	{
		int level = DirectoryUtils.getParentLevel("/sub1/subhtml.html");
		Assert.assertEquals(0, level);
	}

	@Test
	public void getParentLevel_test04()
	{
		int level = DirectoryUtils.getParentLevel("sub1/subhtml.html");
		Assert.assertEquals(0, level);
	}

	@Test
	public void getPathWithNoParent_test01()
	{
		String rs = DirectoryUtils.getPathWithNoParent("../../../../sub1/subhtml.html");
		Assert.assertEquals("sub1/subhtml.html", rs);
	}

	@Test
	public void getPathWithNoParent_test02()
	{
		String rs = DirectoryUtils.getPathWithNoParent("../sub1/subhtml.html");
		Assert.assertEquals("sub1/subhtml.html", rs);
	}

	@Test
	public void getPathWithNoParent_test03()
	{
		String rs = DirectoryUtils.getPathWithNoParent("/sub1/subhtml.html");
		Assert.assertEquals("sub1/subhtml.html", rs);
	}

	@Test
	public void getPathWithNoParent_test04()
	{
		String rs = DirectoryUtils.getPathWithNoParent("sub1/subhtml.html");
		Assert.assertEquals("sub1/subhtml.html", rs);
	}

	@Test
	public void getParentPath_test01()
	{
		String rs = DirectoryUtils.getParentPath(
		        "http://www.sina.com.cn/news/province/shanxi/xian/lianhu/lianhu1.html", "/test.html");
		Assert.assertEquals(null, rs);
	}

	@Test
	public void getParentPath_test02()
	{
		String rs = DirectoryUtils.getParentPath("http://www.sina.com.cn/news/province/shanxi/xian/lianhu/",
		        "../test.html");
		Assert.assertEquals("http://www.sina.com.cn/news/province/shanxi/xian/test.html", rs);
	}

	@Test
	public void getParentPath_test03()
	{
		String rs = DirectoryUtils.getParentPath("http://www.sina.com.cn/news/province/shanxi/xian/lianhu",
		        "../../test.html");
		Assert.assertEquals("http://www.sina.com.cn/news/province/shanxi/test.html", rs);
	}

}
