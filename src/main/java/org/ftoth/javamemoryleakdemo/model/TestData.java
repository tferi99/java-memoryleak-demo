package org.ftoth.javamemoryleakdemo.model;

import org.ftoth.javamemoryleakdemo.util.SystemUtil;

import java.util.ArrayList;
import java.util.List;

public class TestData
{
	private static Integer OSBits;

	private String title;
	private List<List<String>> data = new ArrayList<List<String>>();

	public String getTitle()
	{
		return title;
	}

	public void setTitle(String title)
	{
		this.title = title;
	}

	public TestData(String title)
	{
		this.title = title;
	}

	public void removeFirst()
	{
		if (data.size() <= 0) {
			return;
		}

		synchronized (data) {
			data.remove(0);
		}
	}

	public void addData(List<String> d) {
		data.add(d);
	}

	//------------------ utility ------------------
	public int getSize()
	{
		return data.size();
	}

	public long getSizeInBytes()
	{
		int size = 0;
		synchronized (data) {
			for (List<String> list: data) {
				for (String d : list) {
					size += getStringSize(d);
				}
			}
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

	public void freeItems(int num) {
		if (num <= 0) {
			return;
		}
		synchronized (data) {
			for (int n=0; n<num; n++) {
				data.remove(0);
			}
		}
	}
}
