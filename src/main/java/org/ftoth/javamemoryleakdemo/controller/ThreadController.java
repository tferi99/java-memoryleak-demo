package org.ftoth.javamemoryleakdemo.controller;

import org.apache.log4j.Logger;
import org.ftoth.javamemoryleakdemo.model.TestData;
import org.ftoth.javamemoryleakdemo.thread.TestThread;
import org.ftoth.javamemoryleakdemo.util.MemoryLeakUtil;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Controller
public class ThreadController extends JspController
{
	private static Logger log = Logger.getLogger(ThreadController.class);

	@RequestMapping("/thread_init")
	public String memalloc_init(Map<String, Object> model) {
		model.put("mb", Integer.toString(0));
		model.put("threadCount", 1);
		model.put("threadMaxAgeSecs", 0);
		model.put("leakSize", TestThread.getThreadsTotalAllocatedMBs());
		model.put("gcChecked", CHECKBOX_CHECKED);

		return "thread";
	}

	@RequestMapping("/thread")
	public String startThread(@RequestParam(value = "mb") String mbS, @RequestParam(value = "threadCount") String threadCountS, @RequestParam(value = "threadMaxAgeSecs") String threadMaxAgeSecsS, @RequestParam(value = "gc") String gc, Map<String, Object> model)
	{
		boolean isGc = gc != null;

		int mb, threadCount, threadMaxAgeSecs;
		try {
			mb = getRequiredIntParam("MB", mbS);
			threadCount = getRequiredIntParam("Threads", threadCountS);
			threadMaxAgeSecs = getRequiredIntParam("Max age (secs)", threadMaxAgeSecsS);
		}
		catch (Exception e) {
			model.put("error", e.getMessage());
			return "thread";
		}

		try {
			for (int n=0; n<threadCount; n++) {
				int id = TestThread.getCurrentThreadId();
				TestThread thr = new TestThread(id, mb, threadMaxAgeSecs);
				thr.start();

				TestThread.setCurrentThreadId(id + 1);
			}
		}
		catch (Exception e) {
			model.put("error", e.getMessage());
			return "thread";
		}

		model.put("mb", mbS);
		model.put("threadCount", threadCountS);
		model.put("threadMaxAgeSecs", threadMaxAgeSecs);
		model.put("leakSize", TestThread.getThreadsTotalAllocatedMBs());
		model.put("gcChecked", isGc ? CHECKBOX_CHECKED : "");

		return "thread";
	}
}
