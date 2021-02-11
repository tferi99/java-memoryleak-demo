package org.ftoth.javamemoryleakdemo.thread;

import org.apache.log4j.Logger;
import org.ftoth.javamemoryleakdemo.util.MemoryLeakUtil;

import java.util.List;

public class TestThread extends Thread
{
	private static Logger log = Logger.getLogger(TestThread.class);

	private static int currentThreadId = 0;
	private static int threadsTotalCount = 0;
	private static int threadsTotalAllocatedMBs = 0;
	private static boolean memReleaseLock = false;

	private int customId;
	private int aliveSeconds;
	private int mbToAlloc;
	private int waitMsecs;
	private String tag;

	private static String memLockObj = "this is lock";

	private List<String> allocatedData;

	// ------------------------------------- static ----------------------------------------
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

	public static boolean isMemReleaseLock()
	{
		return memReleaseLock;
	}

	public static void setMemReleaseLock(boolean memReleaseLock)
	{
		TestThread.memReleaseLock = memReleaseLock;
	}

	public static String getMemLockObj()
	{
		return memLockObj;
	}

	// ------------------------------------- instance ----------------------------------------

	public int getCustomId() {
		return customId;
	}

	public int getAliveSeconds() {
		return aliveSeconds;
	}

	public int getMbToAlloc() {
		return mbToAlloc;
	}

	public int getWaitMsecs() {
		return waitMsecs;
	}

	public List<String> getAllocatedData() {
		return allocatedData;
	}

	public String getTag() {
		return tag;
	}

	public TestThread(int customId, int mbToAlloc, int aliveSeconds, String content, int waitMsecs) {
		this(customId, mbToAlloc, aliveSeconds, content, waitMsecs, null);
	}

	public TestThread(int customId, int mbToAlloc, int aliveSeconds, String content, int waitMsecs, String tag)
	{
		this.customId = customId;
		this.mbToAlloc = mbToAlloc;
		this.aliveSeconds = aliveSeconds;
		this.waitMsecs = waitMsecs;
		this.tag = tag;
		this.setName("Developer TestThread[" + customId + "]");

		if (mbToAlloc > 0) {
			allocatedData = MemoryLeakUtil.allocateMemory(mbToAlloc, waitMsecs);
		}

		TestThread.threadsTotalCount++;
		TestThread.threadsTotalAllocatedMBs += mbToAlloc;

		if (log.isDebugEnabled()) {
			log.debug(toString() + " created");
		}
	}

	@Override
	protected void finalize() throws Throwable
	{
		if (TestThread.isMemReleaseLock()) {
			String lockObj = TestThread.getMemLockObj();
			synchronized (lockObj) {
//				log.debug(toString() + " BEFORE wait");
				lockObj.wait();
//				log.debug(toString() + " AFTER wait");
			}
		}

		TestThread.threadsTotalCount--;
		TestThread.threadsTotalAllocatedMBs -= mbToAlloc;

		if (log.isDebugEnabled()) {
			String released = "";
			if(mbToAlloc > 0) {
				released = " (released " + mbToAlloc + " MB)";
			}
			log.debug(toString() + " deleted" + released);
		}
	}

	@Override
	public void run()
	{
		for (int n=0; n<aliveSeconds; n++) {
			if (log.isDebugEnabled()) {
				log.debug(toString() + " still alive for " + (aliveSeconds - n) + " second(s)");
			}
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		if (log.isDebugEnabled()) {
			log.debug(toString() + " terminated");
		}
	}

	public static String getThreadStatus()
	{
		return "THREADS: Count: " + threadsTotalCount + ", Allocated memory in threads: " + threadsTotalAllocatedMBs + " MB";
	}

	@Override
	public String toString() {
		return this.getClass().getSimpleName() + "[" + getId() + "][" + getCustomId() + "][" + getTag() + "]";
	}
}
