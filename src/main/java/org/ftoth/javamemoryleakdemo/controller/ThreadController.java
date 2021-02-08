package org.ftoth.javamemoryleakdemo.controller;

import org.apache.log4j.Logger;
import org.ftoth.javamemoryleakdemo.thread.TestThread;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Map;

@Controller
public class ThreadController extends JspController
{
	private static Logger log = Logger.getLogger(ThreadController.class);

	@RequestMapping("/thread_init")
	public String memalloc_init(Map<String, Object> model) {
		model.put("threadCount", 1);
		model.put("mb", Integer.toString(20));
		model.put("threadMaxAgeSecs", 10);
		model.put("waitMsecs", 0);
		model.put("leakSize", TestThread.getThreadsTotalAllocatedMBs());

		return "thread";
	}

	@RequestMapping("/thread")
	public String startThread(@RequestParam(value = "mb") String mbS, @RequestParam(value = "threadCount") String threadCountS, @RequestParam(value = "threadMaxAgeSecs") String threadMaxAgeSecsS,
		@RequestParam(value = "waitMsecs") String waitMsecsS, @RequestParam(value = "redirect") String redirect, Map<String, Object> model)
	{
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
				TestThread thr = new TestThread(id, mb, threadMaxAgeSecs, waitMsecs);
				thr.start();

				TestThread.setCurrentThreadId(id + 1);
			}
		}
		catch (Exception e) {
			model.put("error", e.getMessage());
			return "thread";
		}

		model.put("threadCount", threadCountS);
		model.put("mb", mbS);
		model.put("threadMaxAgeSecs", threadMaxAgeSecs);
		model.put("leakSize", TestThread.getThreadsTotalAllocatedMBs());

		return "redirect:" + redirect;
	}
}
