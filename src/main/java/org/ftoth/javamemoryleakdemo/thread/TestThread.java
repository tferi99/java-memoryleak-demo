package org.ftoth.javamemoryleakdemo.thread;

import org.apache.log4j.Logger;
import org.ftoth.javamemoryleakdemo.controller.MemAllocController;
import org.ftoth.javamemoryleakdemo.controller.ThreadController;
import org.ftoth.javamemoryleakdemo.util.MemoryLeakUtil;

import java.util.List;

public class TestThread extends Thread
{
	private static Logger log = Logger.getLogger(TestThread.class);

	private static int currentThreadId = 0;
	private static int threadsTotalCount = 0;
	private static int threadsTotalAllocatedMBs = 0;

	private int id;
	private int aliveSeconds;
	private int mbToAlloc;
	private List<String> allocatedData;

	public static int getThreadsTotalCount()
	{
		return threadsTotalCount;
	}

	public static void setThreadsTotalCount(int threadsTotalCount)
	{
		TestThread.threadsTotalCount = threadsTotalCount;
	}

	public static int getThreadsTotalAllocatedMBs()
	{
		return threadsTotalAllocatedMBs;
	}

	public static void setThreadsTotalAllocatedMBs(int threadsTotalAllocatedMBs)
	{
		TestThread.threadsTotalAllocatedMBs = threadsTotalAllocatedMBs;
	}

	public static int getCurrentThreadId()
	{
		return currentThreadId;
	}

	public static void setCurrentThreadId(int currentThreadId)
	{
		TestThread.currentThreadId = currentThreadId;
	}

	public TestThread(int id, int mbToAlloc, int aliveSeconds)
	{
		this.id = id;
		this.mbToAlloc = mbToAlloc;
		this.aliveSeconds = aliveSeconds;

		this.setName("Developer TestThread[" + id + "]");

		if (mbToAlloc > 0) {
			allocatedData = MemoryLeakUtil.allocateMemory(mbToAlloc, 0);
		}

		TestThread.threadsTotalCount++;
		TestThread.threadsTotalAllocatedMBs += mbToAlloc;

		if (log.isDebugEnabled()) {
			log.debug(this.getClass().getSimpleName() + "[" + id + "] created");
		}
	}

	@Override
	protected void finalize() throws Throwable
	{
		TestThread.threadsTotalCount--;
		TestThread.threadsTotalAllocatedMBs -= mbToAlloc;

		if (log.isDebugEnabled()) {
			log.debug(this.getClass().getSimpleName() + "[" + id + "] deleted");
		}
	}

	@Override
	public void run()
	{
		for (int n=0; n<aliveSeconds; n++) {
			if (log.isDebugEnabled()) {
				log.debug(this.getClass().getSimpleName() + "[" + id + "] created " + n + " seconds ago");
			}
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		if (log.isDebugEnabled()) {
			log.debug(this.getClass().getSimpleName() + "[" + id + "] terminated");
		}
	}

	public static String getThreadStatus()
	{
		return "Number of threads: " + threadsTotalCount + ", Allocated memory in threads: " + threadsTotalAllocatedMBs + " MB";
	}
}
