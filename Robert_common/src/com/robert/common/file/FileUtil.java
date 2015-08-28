package com.robert.common.file;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.filechooser.FileNameExtensionFilter;

import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;

import com.robert.common.strings.StringsUtils;

public class FileUtil
{

	private static Logger logger = Logger.getLogger(FileUtil.class);

	/**
	 * 获取文件字符串内容
	 * 
	 * @param filePath
	 *            文件路径
	 * @return String 文件字符串内容
	 */
	public static String getFileAsString(String filePath)
	{
		StringBuilder strBuilder = new StringBuilder();
		try
		{
			BufferedReader reader = new BufferedReader(new FileReader(new File(filePath)));
			try
			{
				String temp = "";
				while (null != (temp = reader.readLine()))
				{
					strBuilder.append(temp + "\r\n");
				}
			}
			finally
			{
				reader.close();
			}
		}
		catch (Exception e)
		{
			logger.error("Get file as string error ! ", e);
		}
		return strBuilder.toString();
	}

	public static boolean folderExists(String folderPath)
	{
		File folder = new File(folderPath);
		if (folder.exists() && folder.isDirectory())
		{
			return true;
		}
		return false;
	}

	public static boolean fileExists(String filePath)
	{
		File folder = new File(filePath);
		if (folder.exists() && folder.isFile())
		{
			return true;
		}
		return false;
	}

	private static boolean createIfNotExists(String filePath)
	{
		File file = new File(filePath);
		if (file.exists() && file.isFile())
		{
			return true;
		}
		try
		{
			return file.createNewFile();
		}
		catch (Exception e)
		{
			logger.error("Create new file[" + filePath + "] error !", e);
		}
		return false;
	}

	public static boolean createFileIfNotExists(String filePath)
	{
		String folder = new File(filePath).getParent();
		if (!createFolderIfNotExists(folder))
		{
			logger.error("Create folder[" + folder + "] error !");
		}
		return createIfNotExists(filePath);
	}

	public static boolean createFolderIfNotExists(String folderPath)
	{
		File folder = new File(folderPath);
		if (folder.exists() && folder.isDirectory())
		{
			return true;
		}
		return folder.mkdirs();
	}

	public static List<File> listFiles(String folderPath, String[] fileSurfix) throws IOException
	{
		File folder = new File(folderPath);
		if (!folder.exists() || !folder.isDirectory())
		{
			return null;
		}

		List<File> fileList = new ArrayList<File>();
		File[] files = folder.listFiles();
		for (File file : files)
		{
			if (file.isFile())
			{
				if (StringsUtils.containsAny(file.getName(), fileSurfix))
				{
					fileList.add(file);
				}
			}
			else if (file.isDirectory())
			{
				fileList.addAll(listFiles(file.getCanonicalPath(), fileSurfix));
			}
		}

		return fileList;
	}
}
