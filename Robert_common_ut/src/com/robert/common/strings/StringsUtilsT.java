package com.robert.common.strings;

import org.junit.Assert;
import org.junit.Test;

public class StringsUtilsT
{

	@Test
	public void indexOfLevel_test1()
	{
		int idx = StringsUtils.indexOfLevel("http://www.sina.com.cn/news/province/shanxi/xian/lianhu/lianhu1.html",
		        "//", 1);

		Assert.assertEquals(5, idx);
	}

	@Test
	public void indexOfLevel_test2()
	{
		int idx = StringsUtils.indexOfLevel("http://www.sina.com.cn/news/province/shanxi/xian/lianhu/lianhu1.html",
		        "/", 3);

		Assert.assertEquals(22, idx);
	}

	@Test
	public void indexOfLevel_test3()
	{
		int idx = StringsUtils.indexOfLevel("http://www.sina.com.cn/news/province/shanxi/xian/lianhu/lianhu1.html",
		        "/", 4);

		Assert.assertEquals(27, idx);
	}

	@Test
	public void indexOfLevel_test4()
	{
		int idx = StringsUtils.indexOfLevel("http://www.sina.com.cn/news/province/shanxi/xian/lianhu/lianhu1.html",
		        "/", 6);

		Assert.assertEquals(43, idx);
	}

	@Test
	public void indexOfLevel_test5()
	{
		int idx = StringsUtils.indexOfLevel("http://www.sina.com.cn/news/province/shanxi/xian/lianhu/lianhu1.html",
		        "/", 8);

		Assert.assertEquals(55, idx);
	}

	@Test
	public void indexOfLevel_test6()
	{
		int idx = StringsUtils.indexOfLevel("http://www.sina.com.cn/news/province/shanxi/xian/lianhu/lianhu1.html",
		        "/", 10);

		Assert.assertEquals(-1, idx);
	}

	@Test
	public void indexOfLevel_test7_lastIndex()
	{
		String url = "http://www.sina.com.cn/";
		int idx = StringsUtils.indexOfLevel(url, "/", 3);
		Assert.assertEquals(url.lastIndexOf("/"), idx);
	}

	@Test
	public void indexOfLevel_test8_lastIndex()
	{
		String url = "http://www.sina.com.cn/test";
		int idx = StringsUtils.indexOfLevel(url, "/", 3);
		Assert.assertEquals(url.lastIndexOf("/"), idx);
	}

	@Test
	public void indexOfLevel_test9_firstIndex()
	{
		String url = "http://www.sina.com.cn/";
		int idx = StringsUtils.indexOfLevel(url, "/", 1);
		Assert.assertEquals(url.indexOf("/"), idx);
	}

	@Test
	public void constains_test1_all()
	{
		String text = "全部包含即可;测试;理论;天;地;人;";
		String[] searchFor = new String[] { "包含", "测试", "理论" };
		boolean rs = StringsUtils.constains(text, searchFor, true);

		Assert.assertTrue(rs);
	}

	@Test
	public void constains_test2_all()
	{
		String text = "全部包含即可;测试;理论;天;地;人;";
		String[] searchFor = new String[] { "包含", "测试", "理论", "a" };
		boolean rs = StringsUtils.constains(text, searchFor, true);

		Assert.assertFalse(rs);
	}

	@Test
	public void constains_test3_all()
	{
		String text = "全部包含即可;测试;理论;天;地;人;a";
		String[] searchFor = new String[] { "包含", "测试", "理论", "a" };
		boolean rs = StringsUtils.constains(text, searchFor, true);

		Assert.assertTrue(rs);
	}

	@Test
	public void constains_test4_all()
	{
		String text = "全部包含即可;测试;理论;天;地;人;a";
		String[] searchFor = new String[] { "ee", "c试", "d论", "d" };
		boolean rs = StringsUtils.constains(text, searchFor, true);

		Assert.assertFalse(rs);
	}

	@Test
	public void constains_test1_or()
	{
		String text = "包含一个即可;测试;理论;天;地;人;a";
		String[] searchFor = new String[] { "包含", "ssss" };
		boolean rs = StringsUtils.constains(text, searchFor, false);

		Assert.assertTrue(rs);
	}

	@Test
	public void constains_test2_or()
	{
		String text = "包含一个即可;测试;理论;天;地;人;ssss";
		String[] searchFor = new String[] { "a含", "ssss" };
		boolean rs = StringsUtils.constains(text, searchFor, false);

		Assert.assertTrue(rs);
	}

	@Test
	public void constains_test3_or()
	{
		String text = "包含一个即可;测试;理论;天;地;人;ssss";
		String[] searchFor = new String[] { "eeee", "dddd" };
		boolean rs = StringsUtils.constains(text, searchFor, false);

		Assert.assertFalse(rs);
	}

}
