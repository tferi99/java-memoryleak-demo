package org.ftoth.javamemoryleakdemo.model;

import org.ftoth.javamemoryleakdemo.util.SystemUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Test data which contains 1K Strings in a list.
 */
public class TestData
{
	private static Integer OSBits;

	private String title;
	private int originalItemCount;
	private TestDataUnit unit;
	private List<String> items;			// 1K String items

	public String getTitle()
	{
		return title;
	}

	public TestData(String title, List<String> items)
	{
		this.title = title;
		this.items = items;
		originalItemCount = items.size();
	}

	protected List<String> getItems() {
		return items;
	}

	public int getItemCount()
	{
		return items.size();
	}

	public int getOriginalItemCount() {
		return originalItemCount;
	}

	/**
	 *
	 * @param num
	 * @return if object is empty, there is no more items
	 */
	public boolean freeItems(int num) {
		if (num <= 0) {
			return false;
		}

		synchronized (items) {
			if (num >= items.size()) {
				// delete all
				items.clear();
				return true;
			}

			// not all deleted
			int origSize = items.size();
			for (int n=0; n<num; n++) {
				items.remove(0);
			}
			return false;
		}
	}

	public long getSizeInBytes()
	{
		int size = 0;
		synchronized (items) {
			for (String item: items) {
				size += getStringSize(item);
			}
		}
		return size;
	}

	//------------------ helpers ------------------
	public void allocateUnits(TestDataUnit unit, int amount) {

	}

	private static long getStringSize(String s)
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

	private static int getOSBits()
	{
		if (OSBits == null) {
			OSBits = SystemUtil.getOSBits();
		}
		return OSBits;
	}

	public static int getStringCharLenBytes(int bytes) {
		if (getOSBits() == 64) {
			return (bytes - 36) / 2;
		}
		// 32
		return (bytes - 32) / 2;
	}
}
