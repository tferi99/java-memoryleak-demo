package org.ftoth.javaprofilerdemo.model;

import org.ftoth.javaprofilerdemo.util.SystemUtil;

import java.util.ArrayList;
import java.util.List;

public class TestData
{
	private static Integer OSBits;

	private String title;
	private List<String> data = new ArrayList<String>();

	public String getTitle()
	{
		return title;
	}

	public void setTitle(String title)
	{
		this.title = title;
	}

	public List<String> getData()
	{
		return data;
	}

	public void setData(List<String> data)
	{
		this.data = data;
	}

	public TestData(String title, List<String> data)
	{
		this.title = title;
		this.data = data;
	}

	//------------------ utility ------------------ 
	public long getSize()
	{
		int size = 0;
		for (String d: data) {
			size += getStringSize(d);
		}
		return size;
	}

	//------------------ helpers ------------------ 
	private long getStringSize(String s)
	{
		if (s == null) {
			return getOSBits() / 8;		// just a reference
		}
		if (getOSBits() == 64) {
			return 36 + s.length() * 2;
		}
		// 32
		return  32 + s.length() * 2;
	}

	private int getOSBits()
	{
		if (OSBits == null) {
			OSBits = SystemUtil.getOSBits();
		}
		return OSBits;
	}
}
