package org.ftoth.javamemoryleakdemo.controller;

import org.apache.log4j.Logger;
import org.ftoth.javamemoryleakdemo.model.TestData;
import org.ftoth.javamemoryleakdemo.util.MemoryLeakUtil;
import org.ftoth.javamemoryleakdemo.util.SystemUtil;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

@Controller
public class MemAllocController extends JspController {
	private static Logger log = Logger.getLogger(MemAllocController.class);
	AtomicInteger dataSerial = new AtomicInteger(0);

	private static final int INITIAL_MEGA_BYTES = 1;
	private static List<TestData> leaks = new ArrayList<TestData>();

	public static int getLeakCount() {
		return leaks.size();
	}

	public static long getLeakSize() {
		long size = 0;
		for (TestData d : leaks) {
			size += d.getSizeInBytes();
		}
		return size;
	}

	public static String getAllocatingStatus()
	{
		return "ALLOCATIONS: Count: " + getLeakCount() + ", Allocated memory: " + getLeakSize() / SystemUtil.MB + " MB";
	}

	@RequestMapping("/memalloc_init")
	public String memalloc_init(Map<String, Object> model) {
		model.put("mb", Integer.toString(INITIAL_MEGA_BYTES));

		return "memalloc";
	}

	@RequestMapping("/memalloc")
	public String memalloc(@RequestParam(value = "mb", required = false) String mb, @RequestParam(value = "action", required = false) String action,
		@RequestParam(value = "txt", required = false) String txt, @RequestParam(value = "redirect", required = false) String redirect, Map<String, Object> model)
	{
		int mbCount = INITIAL_MEGA_BYTES;
		if (mb != null) {
			mbCount = Integer.parseInt(mb);
		}

		if (action == null) {
			throw new RuntimeException("'action' not found");
		}
		if (action.equals("alloc")) {
			alloc(mbCount, txt);
		} else if (action.equals("free")) {
			free(mbCount);
		}

		model.put("mb", Integer.toString(mbCount));
		model.put("txt", txt);

		if (redirect == null) {
			return "/memalloc";
		}

		return "redirect:" + redirect;
	}

	private void alloc(int mb, String txt) {
		if (log.isDebugEnabled()) {
			String cnt = txt == null ? "RANDOM DATA" : "'" + txt + "'";
			log.debug("Allocating " + mb + " MBytes with " + cnt);
		}

		String title = "Mem-" + dataSerial.getAndIncrement();
		TestData d = new TestData(title);
		for (int n=0; n<mb; n++) {
			List<String> data = MemoryLeakUtil.allocate1mbMemory(txt, 0);
			d.addData(data);
		}

		synchronized (leaks) {
			leaks.add(d);
		}
		if (log.isDebugEnabled()) {
			log.debug("Allocated memory -> memory leak");

			// print memory status
			log.debug(SystemUtil.getMemoryStatus());
		}

		if (log.isDebugEnabled()) {
			log.debug("    ----> Allocated " + mb + " MBytes ...");
		}
	}

	private void free(int mb) {
		while(mb > 0 && getLeakCount() > 0) {
			synchronized (leaks) {
				TestData leak = leaks.get(0);
				int leakMb = leak.getSize();
				if (mb >= leakMb) {
					leaks.remove(0);
				}
				else {
					leak.freeItems(mb);
				}
				mb -= leakMb;
			}
		}
	}


}

