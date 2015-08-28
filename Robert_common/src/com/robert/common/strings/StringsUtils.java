package com.robert.common.strings;

public class StringsUtils
{

	private static final int INDEX_NOT_FOUND = -1;

	/**
	 * 查找制定个数的字符所在位置(如，查找字符 a 的第三次出现的坐标)
	 * 
	 * @param src
	 *            源字符串
	 * @param searchFor
	 *            目标字符串
	 * @param level
	 *            第几次出现
	 * @return int 恰好第几次出现的位置 或 -1
	 */
	public static int indexOfLevel(String src, String searchFor, int level)
	{
		int count = 0;
		int idx = 0;
		while ((idx = src.indexOf(searchFor, idx)) != INDEX_NOT_FOUND)
		{
			count++;
			if (count == level)
			{
				return idx;
			}
			idx += searchFor.length();
		}
		return idx;
	}

	/**
	 * 字符串包含指定的多个字符
	 * 
	 * @param string
	 *            字符串
	 * @param searchFor
	 *            指定的多个字符串
	 * @return true:都包含
	 */
	public static boolean containsAll(String string, String[] searchFor)
	{
		boolean isContainsAll = true;
		for (String search : searchFor)
		{
			if (!string.contains(search))
			{
				isContainsAll = false;
				break;
			}
		}
		return isContainsAll;
	}

	/**
	 * 字符串包含指定的任一字符串
	 * 
	 * @param string
	 *            字符串
	 * @param searchFor
	 *            指定的多个字符串
	 * @return true:包含任一字符串即可
	 */
	public static boolean containsAny(String string, String[] searchFor)
	{

		boolean isContainsAny = false;
		for (String search : searchFor)
		{
			if (string.contains(search))
			{
				isContainsAny = true;
				break;
			}
		}
		return isContainsAny;
	}

	/**
	 * 判断字符串 是否包含 指定多个字符
	 * 
	 * @param string
	 *            字符串
	 * @param searchFor
	 *            指定的多个字符串
	 * @param isAll
	 *            true:全部包含 false:包含任一
	 * @return true:全匹配模式下，字符串必须包含所有指定的字符 <br/>
	 *         true:任一匹配模式下，字符串包含任一指定的字符串即可
	 */
	public static boolean constains(String string, String[] searchFor, boolean isAll)
	{
		boolean isContainsAny = isAll;
		for (String search : searchFor)
		{
			if (!isAll)
			{
				if (string.contains(search))
				{
					isContainsAny = !isAll;
					break;
				}
			}
			else
			{
				if (!string.contains(search))
				{
					isContainsAny = !isAll;
					break;
				}
			}
		}
		return isContainsAny;
	}

}
