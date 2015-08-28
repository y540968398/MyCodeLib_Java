package com.robert.common.file;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;

public class FileUtilsT
{

	@Test
	public void listFiles_test01() throws IOException
	{
		String folderPath = "F:\\webDownloader\\shijiamonizhuan";
		boolean isSuccess = true;
		List<File> fileList = null;
		try
		{
			fileList = FileUtil.listFiles(folderPath, new String[] { ".htm", ".html" });
		}
		catch (IOException e)
		{
			e.printStackTrace();
			isSuccess = false;
		}
		System.out.println(fileList.size());
		for (File file : fileList)
		{
			System.out.println(file.getCanonicalPath());
		}

		Assert.assertTrue(isSuccess);
	}

}
