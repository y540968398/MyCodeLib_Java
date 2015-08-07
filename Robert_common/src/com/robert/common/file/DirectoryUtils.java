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
	 * 获取相对链接中的真实路径
	 * 
	 * @description 从跳转到父级目录的相对路径字符串表达式中 提取 包含的实际路径。<br/>
	 *              1.如 ../../../content/a.txt 得到 content/a.txt
	 * 
	 * @param parentRelativePath
	 * @return String 实际路径
	 */
	public static String getPathWithoutJump(String parentRelativePath)
	{
		if (StringUtils.isEmpty(parentRelativePath))
		{
			return null;
		}
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
	 * 获取当前页面 跳转到 父级 的链接的合法表示形式
	 * 
	 * @description 当前路径中 跳转到父级别 路径的合法表示形式<br/>
	 *              如：当前路径 D:/news/province/xian/lianhu/contents.txt<br/>
	 *              相对路径： ../../shanghai/jingansi/content.txt<br/>
	 *              解析后的路径:D:/news/province/shanghai/jingansi/content.txt<br/>
	 * 
	 * @param curPagePath
	 *            当前页面链接的 路径 (连接中包含的 协议://域名/path)
	 * @param relativeLink
	 *            跳转到父级的链接 ../../path
	 * @return String 合法的链接
	 */
	public static String getLink4RelParentPath(String curPagePath, String relativeLink)
	{
		// 跳转到上级别的相对链接
		if (StringUtils.isEmpty(curPagePath) || StringUtils.isEmpty(relativeLink) || !relativeLink.startsWith("../"))
		{
			return null;
		}
		// 当前级别的路径
		if (!curPagePath.endsWith("/"))
		{
			curPagePath = curPagePath + "/";
		}
		// 路径反转
		String reverseRootPath = new StringBuilder(curPagePath).reverse().toString();

		// 获取跳转的级数
		int level = getParentLevel(relativeLink);
		// 获取跳转的级别分隔符所在的位置
		int idx = StringsUtils.indexOfLevel(reverseRootPath, "/", level + 1);
		if (-1 == idx)
		{
			return null;
		}
		// 获取跳转后所在父级别的路径
		String path = new StringBuilder(reverseRootPath.substring(idx)).reverse().toString();
		// 父级别路径 + 相对路径
		return path + getPathWithoutJump(relativeLink);
	}

	/**
	 * 获取 当前路径 跳转到 指定路径的跳转链接
	 * 
	 * @param destPath
	 *            目标路径，要跳转到的路径
	 * @param curPath
	 *            当前路径
	 * @return String 跳转到指定路径的相对路径
	 */
	public static String getRelPath(String destPath, String curPath)
	{
		// 路径相同 或 为空字符串
		if (StringUtils.isEmpty(curPath) || StringUtils.isEmpty(destPath) || destPath.equals(curPath)
		        || getDir(curPath).equals(getDir(destPath)))
		{
			return null;
		}

		// 如果两个路径为 子父 关系，才能找到其中的相对链接关系。所以两个路径中有相同的部分（这个相同的部分作为要跳转的路径）
		int diffIdx = StringUtils.indexOfDifference(curPath, destPath);
		if (diffIdx < 0)
		{
			// 两个路径中没有共同的部分，不是合法的 子 父 路径
			return null;
		}

		// 获取当前路径 到 父路径的 部分 （以 "/" 开头结尾）
		String curPath2Parent = curPath.substring(diffIdx - 1);
		String curDirPath = curPath2Parent.substring(0, curPath2Parent.lastIndexOf("/") + 1);
		if (curDirPath.charAt(0) != '/' || curDirPath.charAt(0) != curDirPath.charAt(curDirPath.length() - 1))
		{
			return null;
		}

		// 子路径到父路径跳转的级别
		int toParentLevel = StringUtils.countMatches(curDirPath, "/") - 1;
		StringBuilder stb = new StringBuilder();
		for (int i = 0; i < toParentLevel; i++)
		{
			stb.append("../");
		}

		if (StringUtils.isEmpty(stb.toString()))
		{
			stb.append("/");
		}
		stb.append(destPath.substring(diffIdx));

		return stb.toString();
	}

	/**
	 * 获取地址中的路径部分
	 * 
	 * @param path
	 * @return String 文件路径
	 */
	public static String getDir(String path)
	{
		if (StringUtils.isEmpty(path))
		{
			return null;
		}
		return path.substring(0, path.lastIndexOf("/") + 1);
	}

	/**
	 * 获取地址中的文件名称
	 * 
	 * @param path
	 *            文件具体地址
	 * @return String 文件名称
	 */
	public static String getFileName(String path)
	{
		if (StringUtils.isEmpty(path))
		{
			return null;
		}
		return path.substring(path.lastIndexOf("/") + 1);
	}

}
