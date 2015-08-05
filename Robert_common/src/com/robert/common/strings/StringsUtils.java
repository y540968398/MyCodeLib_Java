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

}
