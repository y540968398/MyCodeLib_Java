package com.robert.http.enums;

public enum DownloadStatus implements IEnums
{
	DOWNLOADING("downloading", "处理中！"), DOWNLOADED("downloaded", "已下载完毕！");

	private String enumName;
	private String enumDesc;

	private DownloadStatus(String name, String desc)
	{
		this.enumName = name;
		this.enumDesc = desc;
	}

	@Override
	public String enumName()
	{
		return this.enumName;
	}

	@Override
	public String enumDesc()
	{
		return this.enumDesc;
	}

	@Override
	public boolean same(String enumValue)
	{
		if (this.name().equals(enumValue) || this.enumName.equals(enumValue))
		{
			return true;
		}
		return false;
	}

	@Override
	public String toString()
	{
		return this.ordinal() + ":" + this.name() + "[" + this.enumName + ", " + this.enumDesc + "]";
	}

}
