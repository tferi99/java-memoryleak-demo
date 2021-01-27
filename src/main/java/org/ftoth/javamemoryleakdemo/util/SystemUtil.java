package org.ftoth.javamemoryleakdemo.util;

import org.apache.log4j.Logger;
import org.ftoth.javamemoryleakdemo.controller.MemAllocController;

public class SystemUtil
{
	private static Logger log = Logger.getLogger(SystemUtil.class);

	private static final int MB = 1024 * 1024;

	public static int getOSBits()
	{
		String arch = System.getProperty("os.arch");
		if (arch.equals("x86")) {
			return 32;
		}
		return 64;
	}

	public static String getMemoryStatus() {
		long total = Runtime.getRuntime().totalMemory() / MB;
		long free = Runtime.getRuntime().freeMemory() / MB;
		return "MEMORY: Total: " + total + "MB, Free: " + free + "MB, Used: " + (total - free) + "MB";
	}

	public static void gc(String context) {
		if (log.isDebugEnabled()) {
			log.debug("================== GC before[" + context + "] START ==================");
		}
		System.gc();
		if (log.isDebugEnabled()) {
			log.debug("------------------ GC after[" + context + "] END ------------------");
		}
	}
}
