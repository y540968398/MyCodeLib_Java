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
		String rs = DirectoryUtils.getPathWithoutJump("../../../../sub1/subhtml.html");
		Assert.assertEquals("sub1/subhtml.html", rs);
	}

	@Test
	public void getPathWithNoParent_test02()
	{
		String rs = DirectoryUtils.getPathWithoutJump("../sub1/subhtml.html");
		Assert.assertEquals("sub1/subhtml.html", rs);
	}

	@Test
	public void getPathWithNoParent_test03()
	{
		String rs = DirectoryUtils.getPathWithoutJump("/sub1/subhtml.html");
		Assert.assertEquals("sub1/subhtml.html", rs);
	}

	@Test
	public void getPathWithNoParent_test04()
	{
		String rs = DirectoryUtils.getPathWithoutJump("sub1/subhtml.html");
		Assert.assertEquals("sub1/subhtml.html", rs);
	}

	@Test
	public void getParentPath_test01()
	{
		String rs = DirectoryUtils.getLink4RelParentPath(
		        "http://www.sina.com.cn/news/province/shanxi/xian/lianhu/lianhu1.html", "/test.html");
		Assert.assertEquals(null, rs);
	}

	@Test
	public void getParentPath_test02()
	{
		String rs = DirectoryUtils.getLink4RelParentPath("http://www.sina.com.cn/news/province/shanxi/xian/lianhu/",
		        "../test.html");
		Assert.assertEquals("http://www.sina.com.cn/news/province/shanxi/xian/test.html", rs);
	}

	@Test
	public void getParentPath_test03()
	{
		String rs = DirectoryUtils.getLink4RelParentPath("http://www.sina.com.cn/news/province/shanxi/xian/lianhu",
		        "../../test.html");
		Assert.assertEquals("http://www.sina.com.cn/news/province/shanxi/test.html", rs);
	}

	@Test
	public void getRelPath_01()
	{
		String destPath = "http://www.sina.com.cn/news/";
		String curPath = "http://www.sina.com.cn/news/province/shanxi/xian/";
		String relPath = DirectoryUtils.getRelPath(destPath, curPath);
		
		Assert.assertEquals("../../../", relPath);
	}

	@Test
	public void getRelPath_02()
	{
		String destPath = "http://www.sina.com.cn/news/test.contens";
		String curPath = "http://www.sina.com.cn/news/province/shanxi/xian/aaaa.contents";
		String relPath = DirectoryUtils.getRelPath(destPath, curPath);
		
		Assert.assertEquals("../../../test.contens", relPath);
	}
	
	@Test
	public void getRelPath_03()
	{
		String destPath = "http://www.sina.com.cn/news/test.contens";
		String curPath = "http://www.sina.com.cn/news/province/shanxi/xian/aaaa.contents";
		String relPath = DirectoryUtils.getRelPath(curPath, destPath);
		
		Assert.assertEquals("/province/shanxi/xian/aaaa.contents", relPath);
	}

	@Test
	public void getRelPath_04()
	{
		String destPath = "http://www.sina.com.cn/news/test.contens";
		String curPath = "http://www.sina.com.cn/news/province/shanxi/xian/";
		String relPath = DirectoryUtils.getRelPath(curPath, destPath);
		
		Assert.assertEquals("/province/shanxi/xian/", relPath);
	}

	@Test
	public void getRelPath_05()
	{
		String destPath = "http://www.sina.com.cn/news/";
		String curPath = "http://www.sina.com.cn/news/province/shanxi/xian/";
		String relPath = DirectoryUtils.getRelPath(curPath, destPath);
		
		Assert.assertEquals("/province/shanxi/xian/", relPath);
	}

	@Test
	public void getRelPath_06()
	{
		String destPath = "http://www.sina.com.cn/news/province/shanxi/xian/";
		String curPath = "http://www.sina.com.cn/news/province/shanxi/xian/";
		String relPath = DirectoryUtils.getRelPath(curPath, destPath);
		
		Assert.assertEquals(null, relPath);
	}

	@Test
	public void getRelPath_07()
	{
		String destPath = "http://www.sina.com.cn/news/province/shanxi/xian/aaaa";
		String curPath = "http://www.sina.com.cn/news/province/shanxi/xian/bbbb";
		String relPath = DirectoryUtils.getRelPath(curPath, destPath);
		
		Assert.assertEquals(null, relPath);
	}

	@Test
	public void getRelPath_08()
	{
		String destPath = "http://www.sina.com.cn/news/province/shanxi/xian/aaaa";
		String curPath = "http://www.sina.com.cn/news/province/shanxi/xian/";
		String relPath = DirectoryUtils.getRelPath(curPath, destPath);
		
		Assert.assertEquals(null, relPath);
	}

	@Test
	public void getRelPath_09()
	{
		String destPath = "http://www.sina.com.cn/news/province/shanxi/xian/";
		String curPath = "http://www.sina.com.cn/news/province/shanxi/xian/bbbb";
		String relPath = DirectoryUtils.getRelPath(curPath, destPath);
		
		Assert.assertEquals(null, relPath);
	}

}
