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

	/**
	 * It allocates an ArrayList and it adds specified amount  
	 * 1 KB random string
	 *
	 *
	 * @param mbytes
	 * @param waitPerMBytes
	 * @return
	 */
	public static synchronized List<String> allocateMemory(int mbytes, int waitPerMBytes)
	{
		if (log.isDebugEnabled()) {
			log.debug("Allocating " + mbytes + " MBytes ...");
		}

		List<String> list = new ArrayList<String>();
		Random rand = new Random();

		// N MB data into ArrayList
		for (int n=0; n<mbytes; n++) {
			if (log.isDebugEnabled()) {
				log.debug("Allocating 1MB #" + (n + 1));
			}

			// 1 MB data into ArrayList
			for(int m=0; m<1024; m++) {
				// 1K random pattern as a String
				StringBuffer b = new StringBuffer();
				for (int i=0; i<512; i++) {			// 1 char is 2 bytes
					b.append(Integer.toString(rand.nextInt(9)));
				}
				list.add(b.toString());
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
			log.debug("END OF ALLOCATION");
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
