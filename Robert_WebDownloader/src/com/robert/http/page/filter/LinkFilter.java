package com.robert.http.page.filter;

import java.util.List;

import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.robert.common.cfglog.CfgUtil;
import com.robert.common.strings.StringsUtils;
import com.robert.http.constants.SystemConstants;

public class LinkFilter
{

	public static Elements filterByTextContains(List<Element> links)
	{
		// 过滤链接条件:根据text进行
		String[] contains_text_and = StringUtils.split(CfgUtil.get(SystemConstants.SUBLINK_FILTER_CONTAINS_TEXT_AND),
		        ";");
		String[] contains_text_or = StringUtils.split(
		        CfgUtil.get(CfgUtil.get(SystemConstants.SUBLINK_FILTER_CONTAINS_TEXT_OR)), ";");

		if (ArrayUtils.isNotEmpty(contains_text_and))
		{
			return filterByText(links, contains_text_and);
		}
		else if (ArrayUtils.isNotEmpty(contains_text_or))
		{
			return filterByAnyText(links, contains_text_or);
		}
		return null;
	}

	private static Elements filterByAnyText(List<Element> links, String[] contains_text_or)
	{
		Elements rsElements = new Elements();
		for (Element element : links)
		{
			String text = element.text();
			if (StringsUtils.containsAny(text, contains_text_or))
			{
				rsElements.add(element);
			}
		}
		return rsElements;
	}

	private static Elements filterByText(List<Element> links, String[] contains_text_and)
	{
		Elements rsElements = new Elements();
		for (Element element : links)
		{
			String text = element.text();
			if (StringsUtils.containsAll(text, contains_text_and))
			{
				rsElements.add(element);
			}
		}
		return rsElements;
	}

}
