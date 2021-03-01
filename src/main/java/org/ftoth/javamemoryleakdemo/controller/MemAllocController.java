package org.ftoth.javamemoryleakdemo.controller;

import org.apache.log4j.Logger;
import org.ftoth.javamemoryleakdemo.model.TestDataUnit;
import org.ftoth.javamemoryleakdemo.model.TestDataUtil;
import org.ftoth.javamemoryleakdemo.util.SystemUtil;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

@Controller
public class MemAllocController extends JspController {
	private static Logger log = Logger.getLogger(MemAllocController.class);
	AtomicInteger dataSerial = new AtomicInteger(0);

	private static final int INITIAL_MEGA_BYTES = 1;
	private static final TestDataUnit ALLOC_UNIT = TestDataUnit.MB;

	public static String getAllocatingStatus() {
		return "ALLOCATIONS: Count: " + TestDataUtil.getLeakCount() + ", Allocated memory: " + TestDataUtil.getLeakSize() / SystemUtil.MB + " MB";
	}

	@RequestMapping("/memalloc_init")
	public String memalloc_init(Map<String, Object> model) {
		model.put("mb", Integer.toString(INITIAL_MEGA_BYTES));

		return "memalloc";
	}

	@RequestMapping("/memalloc")
	public String memalloc(@RequestParam(value = "mb", required = false) String mb, @RequestParam(value = "action", required = false) String action,
						   @RequestParam(value = "redirect", required = false) String redirect, Map<String, Object> model) {
		int mbCount = INITIAL_MEGA_BYTES;
		if (mb != null) {
			mbCount = Integer.parseInt(mb);
		}

		if (action == null) {
			throw new RuntimeException("'action' not found");
		}
		if (action.equals("alloc")) {
			alloc(mbCount);
		} else if (action.equals("free")) {
			free(mbCount);
		}

		model.put("mb", Integer.toString(mbCount));

		if (redirect == null) {
			return "/memalloc";
		}

		return "redirect:" + redirect;
	}

	private void alloc(int mb) {
		if (log.isDebugEnabled()) {
			log.debug("Allocating " + mb + " MBytes");
		}

		String title = "Mem-" + dataSerial.getAndIncrement();
		TestDataUtil.allocateTestDataLeak(title, mb, ALLOC_UNIT, 0);

		if (log.isDebugEnabled()) {
			log.debug("    ----> Allocated " + mb + " MBytes ...");

			// print memory status
			log.debug(SystemUtil.getMemoryStatus());
		}
	}

	private void free(int mb) {
		TestDataUtil.freeTestDataLeak(mb, ALLOC_UNIT);
	}
}


