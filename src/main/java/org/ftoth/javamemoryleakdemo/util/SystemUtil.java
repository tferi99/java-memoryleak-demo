package org.ftoth.javamemoryleakdemo.util;

import org.apache.log4j.Logger;
import org.ftoth.javamemoryleakdemo.controller.MemAllocController;

import java.lang.management.ManagementFactory;

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

	public static String getJvmVersion() {
		return System.getProperty("java.version");
	}

	public static String getMemoryStatus() {
		long total = Runtime.getRuntime().totalMemory();
		long free = Runtime.getRuntime().freeMemory();
		long used = total - free;
		long totalMB = total / MB;
		long freeMB = free / MB;
		long usedMB = used / MB;
		String stat = "MEMORY: Total/Free/Used: " + totalMB + "MB / " + freeMB + "MB / " + usedMB + "MB        (" + total + " / " + free + " / " + used + ")";
		System.out.println(stat);

		if (log.isDebugEnabled()) {
			log.debug(stat);
		}
		return stat;
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

	public static String getProcessInfo() {
		return ManagementFactory.getRuntimeMXBean().getName();
	}
}
