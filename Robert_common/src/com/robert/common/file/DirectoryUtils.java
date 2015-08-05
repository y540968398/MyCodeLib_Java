package com.robert.common.file;

import org.apache.commons.lang.StringUtils;

import com.robert.common.strings.StringsUtils;

public class DirectoryUtils
{

	/**
	 * 获取相对路径中跳转到上一级的次数
	 * 
	 * @description 检测跳转路径中 ../ 的个数
	 * 
	 * @param relativePath
	 *            跳转到父级目录的 相对路径 表示字符串
	 * @return Integer 跳转到上一级的次数
	 */
	public static int getParentLevel(String relativePath)
	{
		String[] rsArr = relativePath.split("/");
		int level = 0;
		for (String rs : rsArr)
		{
			if (rs.equals(".."))
			{
				level++;
			}
		}
		return level;
	}

	/**
	 * 获取跳转到父级目录中的实际路径
	 * 
	 * @description 从跳转到父级目录的相对路径字符串表达式中 提取 包含的实际路径。<br/>
	 *              1.如 ../../../content/a.txt 得到 content/a.txt
	 * 
	 * @param parentRelativePath
	 * @return
	 */
	public static String getPathWithNoParent(String parentRelativePath)
	{
		String[] rsArr = StringUtils.splitByWholeSeparator(parentRelativePath, "/");

		StringBuilder resultStrb = new StringBuilder();
		for (String rs : rsArr)
		{
			if (!rs.equals(".."))
			{
				resultStrb.append(rs + "/");
			}
		}
		return resultStrb.substring(0, resultStrb.length() - 1);
	}

	/**
	 * 获取跳转父级连接的合法形式
	 * 
	 * @param rootPath
	 *            根目录
	 * @param subRelativePath
	 *            跳转到父级的链接 ../../path
	 * @return String 合法的链接
	 */
	public static String getParentPath(String rootPath, String subRelativePath)
	{
		if (StringUtils.isEmpty(rootPath) || StringUtils.isEmpty(subRelativePath) || !subRelativePath.startsWith("../"))
		{
			return null;
		}
		if (!rootPath.endsWith("/"))
		{
			rootPath = rootPath + "/";
		}
		String reverseRootPath = new StringBuilder(rootPath).reverse().toString();
		int level = getParentLevel(subRelativePath);
		int idx = StringsUtils.indexOfLevel(reverseRootPath, "/", level + 1);
		if (-1 == idx)
		{
			return null;
		}

		String path = new StringBuilder(reverseRootPath.substring(idx)).reverse().toString();

		return path + getPathWithNoParent(subRelativePath);
	}

}
