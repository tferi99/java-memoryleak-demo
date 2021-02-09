package org.ftoth.javamemoryleakdemo.controller;

import org.apache.log4j.Logger;
import org.ftoth.javamemoryleakdemo.thread.TestThread;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Map;

@Controller
public class ThreadController extends JspController
{
	private static Logger log = Logger.getLogger(ThreadController.class);

	@RequestMapping("/thread_init")
	public String memalloc_init(Map<String, Object> model) {
		model.put("threadCountTotal", TestThread.getThreadsTotalCount());
		model.put("threadAllocMbTotal", TestThread.getThreadsTotalAllocatedMBs());
		model.put("threadCount", 1);
		model.put("mb", Integer.toString(20));
		model.put("threadMaxAgeSecs", 10);
		model.put("waitMsecs", 0);
		model.put("leakSize", TestThread.getThreadsTotalAllocatedMBs());

		boolean locked = TestThread.isMemReleaseLock();
		model.put("memReleaseLock", locked ? CHECKBOX_CHECKED : "");

		return "thread";
	}

	@RequestMapping("/thread")
	public String startThread(@RequestParam(value = "mb") String mbS, @RequestParam(value = "threadCount") String threadCountS, @RequestParam(value = "threadMaxAgeSecs") String threadMaxAgeSecsS,
		@RequestParam(value = "txt") String txt, @RequestParam(value = "waitMsecs") String waitMsecsS, @RequestParam(value = "stayHere", required = false) String stayHereS,
		@RequestParam(value = "redirect") String redirect, Map<String, Object> model)
	{
		boolean stayHere = stayHereS != null;

		int mb, threadCount, threadMaxAgeSecs, waitMsecs;
		try {
			mb = getRequiredIntParam("MB", mbS);
			threadCount = getRequiredIntParam("Threads", threadCountS);
			threadMaxAgeSecs = getRequiredIntParam("Max age (secs)", threadMaxAgeSecsS);
			waitMsecs = getRequiredIntParam("With msecs...", waitMsecsS);
		}
		catch (Exception e) {
			model.put("error", e.getMessage());
			return "thread";
		}

		try {
			for (int n=0; n<threadCount; n++) {
				int id = TestThread.getCurrentThreadId();
				TestThread thr = new TestThread(id, mb, threadMaxAgeSecs, txt, waitMsecs);
				thr.start();

				TestThread.setCurrentThreadId(id + 1);
			}
		}
		catch (Exception e) {
			model.put("error", e.getMessage());
			return "thread";
		}

		model.put("threadCountTotal", TestThread.getThreadsTotalCount());
		model.put("threadAllocMbTotal", TestThread.getThreadsTotalAllocatedMBs());
		model.put("threadCount", threadCountS);
		model.put("mb", mbS);
		model.put("threadMaxAgeSecs", threadMaxAgeSecs);
		model.put("leakSize", TestThread.getThreadsTotalAllocatedMBs());
		model.put("stayHere", stayHere ? CHECKBOX_CHECKED : "");
		model.put("waitMsecs", waitMsecsS);
		model.put("txt", txt);

		if (stayHere) {
			return "/thread";
		}
		return "redirect:" + redirect;
	}

	@RequestMapping("/threadmemlock")
	public String startThread(Map<String, Object> model)
	{
		boolean locked = TestThread.isMemReleaseLock();
		model.put("memReleaseLock", locked ? CHECKBOX_CHECKED : "");
		return "threadmemlock";
	}

	@RequestMapping(value = "/threadmemlocked", method = RequestMethod.GET)
	@ResponseBody
	public void startThread(@RequestParam(value = "locked") String lockedS)
	{
		log.debug("threadmemlocked: " + lockedS);
		boolean locked = Boolean.parseBoolean(lockedS);
		TestThread.setMemReleaseLock(locked);

		if (TestThread.isMemReleaseLock()) {
			String lockObj = TestThread.getMemLockObj();
			synchronized (lockObj) {
				lockObj.notifyAll();
			}
		}
	}
}

