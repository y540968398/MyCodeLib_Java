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
	
}
