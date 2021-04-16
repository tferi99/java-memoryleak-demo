package org.ftoth.javamemoryleakdemo.model;

import org.apache.log4j.Logger;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

public class TestDataUtil
{
	private static Logger log = Logger.getLogger(TestDataUtil.class);

	public static final long MB = 1024 * 1024;
	private static final String BIG_FILE_URL = "http://norvig.com/big.txt";
	private static final int CHARS_IN_1KB = 512;

	//-------------------------------------------------------------- memleak --------------------------------------------------------------
	private static List<TestData> leaks = new ArrayList<TestData>();

	public static int getLeakCount() {
		return leaks.size();
	}

	public static synchronized TestData createTestData(String title, int count, TestDataUnit unit, int waitPerUnits)
	{
		List<String> items = new ArrayList<>();
		for (int n=0; n<count; n++) {
			if (unit == TestDataUnit.KB) {
				allocate1K(items);
			} else if (unit == TestDataUnit.MB) {
				allocate1M(items);
			}

			// wait after allocation
			if (waitPerUnits > 0) {
				try {
					if (log.isDebugEnabled()) {
						log.debug("Waiting for " + waitPerUnits + " msecs...");
					}
					Thread.sleep(waitPerUnits);
				}
				catch (InterruptedException e) {
					e.printStackTrace();
				}
			}

		}
		return new TestData(title, items);
	}
	
	public static synchronized void allocateTestDataLeak(String title, int count, TestDataUnit unit, int waitPerUnits) {
		TestData data = createTestData(title, count, unit, waitPerUnits);
		synchronized (leaks) {
			leaks.add(data);
		}
	}

	public static synchronized void freeTestDataLeak(int count, TestDataUnit unit)
	{
		int origCount = count;

		if (log.isDebugEnabled()) {
			log.debug("Deleting " + count + " " + unit.toString() + "...");
		}

		if (unit == TestDataUnit.MB) {
			count *= 1024;
		}

		synchronized (leaks) {
			Iterator<TestData> iter = leaks.iterator();
			while(iter.hasNext() && count > 0) {
				TestData item = iter.next();
				int origDataSize = item.getItemCount();
				boolean allDeletedFromTestData = item.freeItems(count);
				if (allDeletedFromTestData) {
					count -= origDataSize;
					if (log.isDebugEnabled()) {
						log.debug(item.getClass().getSimpleName() + "[" + item.getTitle() + "] deleting");
					}
					iter.remove();
				}
				else {
					// TestData still contains items but deleted all
					count = 0;
					if (log.isDebugEnabled()) {
						log.debug(item.getClass().getSimpleName() + "[" + item.getTitle() + "] not deleted, there is still " + item.getItemCount() + " KB data.");
					}
					break;
				}
			}
			if (log.isDebugEnabled()) {
				int delCount = count;
				if (unit == TestDataUnit.MB) {
					delCount /= 1024;
				}

				if (count > 0) {
					log.debug("Only " + (origCount - delCount) + " " + unit.toString() + " deleted - no more data.");
				} else {
					log.debug("Deleted " + (origCount - delCount) + " " + unit.toString());
				}
			}
		}
	}

	public static long getLeakSize() {
		long size = 0;
		for (TestData d : leaks) {
			size += d.getSizeInBytes();
		}
		return size;
	}

	//-------------------------------------------------------------- stream --------------------------------------------------------------
	public static synchronized List<String> readStreamButDontCloseIt()
	{
		if (log.isDebugEnabled()) {
			log.debug("Opening stream from: " + BIG_FILE_URL);
		}

		List<String> str = new ArrayList<String>();
		URLConnection conn = null;
		try {
			conn = new URL(BIG_FILE_URL).openConnection();
			BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream(), StandardCharsets.UTF_8));
			while (br.readLine() != null) {
				str.add(br.readLine());
			}
			if (log.isDebugEnabled()) {
				log.debug("Lines have bean read");
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return str;
	}

	public static synchronized List<String> openConnButDontCloseIt()
	{
		if (log.isDebugEnabled()) {
			log.debug("Opening connnection from: " + BIG_FILE_URL);
		}

		List<String> str = new ArrayList<String>();
		URLConnection conn = null;
		try {
			conn = new URL(BIG_FILE_URL).openConnection();
			BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream(), StandardCharsets.UTF_8));
			while (br.readLine() != null) {
				str.add(br.readLine());
			}
			if (log.isDebugEnabled()) {
				log.debug("Lines have bean read");
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return str;
	}

	//-------------------------------------------------------------- helpers --------------------------------------------------------------
	private static void allocate1M(List<String> list)
	{
		for (int n=0; n<1024; n++) {
			allocate1K(list);
		}
	}

	private static void allocate1K(List<String> list)
	{
		int chars = TestData.getStringCharLenBytes(1024);
		Random rand = new Random();
		StringBuffer b = new StringBuffer();
		for (int i = 0; i < chars; i++) {            // 1 char is 2 bytes
			b.append(Integer.toString(rand.nextInt(9)));
		}
		list.add(b.toString());
	}

}
