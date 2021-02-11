package org.ftoth.javamemoryleakdemo.util;

import org.apache.log4j.Logger;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class MemoryLeakUtil
{
	private static Logger log = Logger.getLogger(MemoryLeakUtil.class);

	public static final long MB = 1024 * 1024;
	private static final String BIG_FILE_URL = "http://norvig.com/big.txt";
	private static final int CHARS_IN_1KB = 512;
	private static final int RANDOM_HEAD_LEN = 10;

	public static synchronized List<String> allocateMemory(int mbytes, int waitPerMBytes)
	{
		return allocateMemory(mbytes, null, waitPerMBytes);
	}

	/**
	 * It allocates an ArrayList and it adds specified amount  
	 * 1 KB random string
	 *
	 *
	 * @param mbytes
	 * @param content if null/empty then random content will be generated
	 * @param waitPerMBytes
	 * @return
	 */
	public static synchronized List<String> allocateMemory(int mbytes, String content, int waitPerMBytes)
	{
		boolean randomContent = content == null || content.equals("");

		if (log.isDebugEnabled()) {
			String cnt = randomContent ? "random content" : "'" + content + "'";
			log.debug("Allocating " + mbytes + " MBytes with " + cnt);
		}

		List<String> list = new ArrayList<String>();

		// 1 KB content
		Random rand = new Random();
		String norandomContent = null;
		if (!randomContent) {							// create 1K content once
			StringBuffer b = new StringBuffer();
			b = new StringBuffer();
			content += "|";
			int norandomContentLen = CHARS_IN_1KB - RANDOM_HEAD_LEN;
			while (b.length() < norandomContentLen) {
				int len = norandomContentLen - b.length() > content.length() ? content.length() : norandomContentLen - b.length();
				b.append(content, 0, len);
//				log.debug("len: " + b.length());
			}
//			log.debug("### LEN: " + b.length());
			norandomContent = b.toString();
		}

		// N MB data into ArrayList
		for (int n=0; n<mbytes; n++) {
			String content1K;
			if (log.isDebugEnabled()) {
//				log.debug("Allocating 1MB #" + (n + 1));
			}

			// 1 MB data into ArrayList
			for(int m=0; m<1024; m++) {
				StringBuffer b = new StringBuffer();
				if (randomContent) {							// content is random
					// 1K random pattern as a String
					for (int i = 0; i < 512; i++) {            // 1 char is 2 bytes
						b.append(Integer.toString(rand.nextInt(9)));
					}
				}
				else {											// content = random_header + notrandom
					for (int i = 0; i < RANDOM_HEAD_LEN-1; i++) {            // 1 char is 2 bytes
						b.append(Integer.toString(rand.nextInt(9)));
					}
					b.append("|");
					b.append(norandomContent);
				}
				content1K = b.toString();

/*				if (log.isDebugEnabled()) {
					log.debug("Content[" + m + "][" + content1K.length() + "] : " + content1K);
				}*/
				list.add(content1K);
			}

			// wait after allocation
			if (waitPerMBytes > 0) {
				try {
					if (log.isDebugEnabled()) {
						log.debug("Waiting for " + waitPerMBytes + " msecs...");
					}
					Thread.sleep(waitPerMBytes);
				}
				catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
		if (log.isDebugEnabled()) {
			log.debug("    ----> Allocated " + mbytes + " MBytes ...");
		}
		return list;
	}

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

}
