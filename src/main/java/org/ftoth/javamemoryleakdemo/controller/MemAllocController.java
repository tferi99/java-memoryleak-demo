package org.ftoth.javamemoryleakdemo.controller;

import org.apache.log4j.Logger;
import org.ftoth.javamemoryleakdemo.model.TestData;
import org.ftoth.javamemoryleakdemo.util.MemoryLeakUtil;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Controller
public class MemAllocController extends JspController
{
	private static Logger log = Logger.getLogger(MemAllocController.class);

	private static final int MEGA_BYTES = 1;
	private static List<TestData> leak = new ArrayList<TestData>();

	public int getLeakCount()
	{
		return leak.size();
	}

	public long getLeakSize()
	{
		long size = 0;
		for(TestData d: leak) {
			size += d.getSize();
		}
		return size;
	}

	@RequestMapping("/memalloc_init")
	public String memalloc_init(Map<String, Object> model)
	{
		model.put("mb", Integer.toString(MEGA_BYTES));
		model.put("leakCount", getLeakCount());
		model.put("leakSize", getLeakSize() / MemoryLeakUtil.MB);
		model.put("storeChecked", CHECKBOX_CHECKED);
		model.put("gcChecked", CHECKBOX_CHECKED);

		return "memalloc";
	}

	@RequestMapping("/memalloc")
	public String memalloc(@RequestParam(value = "mb", required = false) String mb, @RequestParam(value = "store", required = false) String store, @RequestParam(value = "gc", required = false) String gc, Map<String, Object> model)
	{
		boolean isStored = store != null;
		boolean isGc = gc != null;

		int mbToAlloc = MEGA_BYTES;
		if (mb != null) {
			mbToAlloc  = Integer.parseInt(mb);
		}

		// getting index
		int generated = leak.size();
		generated++;

		List<String> data = MemoryLeakUtil.allocateMemory(mbToAlloc, 0);
		if (isStored) {
			String title = "Mem-" + generated;
			TestData d = new TestData(title, data);
			leak.add(d);
			if (log.isDebugEnabled()) {
				log.debug("Allocated memory -> memory leak");
			}
		}

		if (isGc) {
			System.gc();
			if (log.isDebugEnabled()) {
				log.debug("GC called");
			}
		}

		model.put("mb", Integer.toString(mbToAlloc));
		model.put("leakCount", getLeakCount());
		model.put("leakSize", getLeakSize() / MemoryLeakUtil.MB);
		model.put("storeChecked", isStored ? CHECKBOX_CHECKED : "");
		model.put("gcChecked", isGc ? CHECKBOX_CHECKED : "");
		return "memalloc";
	}
}

