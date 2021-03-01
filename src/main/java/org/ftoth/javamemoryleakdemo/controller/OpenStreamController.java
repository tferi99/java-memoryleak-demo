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

@Controller
public class OpenStreamController extends JspController
{
	private static Logger log = Logger.getLogger(OpenStreamController.class);
	private static List<TestData> leak = new ArrayList<TestData>();
	private static int numberOfOpenStreams = 0;

	public int getLeakCount()
	{
		return leak.size();
	}

	public long getLeakSize()
	{
		long size = 0;
		for(TestData d: leak) {
			size += d.getSizeInBytes();
		}
		return size;
	}

	public int getOpenStreamCount()
	{
		return numberOfOpenStreams;
	}

	@RequestMapping("/openstream_init")
	public String memalloc_init(Map<String, Object> model)
	{
		model.put("openStreamCount", getOpenStreamCount());
		model.put("leakCount", getLeakCount());
		model.put("leakSize", getLeakSize() / MemoryLeakUtil.MB);
		model.put("storeChecked", "");
		model.put("gcChecked", CHECKBOX_CHECKED);
		return "openstream";
	}

	@RequestMapping("/openstream")
	public String openstream(@RequestParam(value = "store", required = false) String store, @RequestParam(value = "gc", required = false) String gc,
		 @RequestParam(value = "redirect") String redirect, Map<String, Object> model)
	{
		boolean isStored = store != null;
		boolean isGc = gc != null;

		int generated = leak.size();
		generated++;

		List<String> data = MemoryLeakUtil.readStreamButDontCloseIt();
		numberOfOpenStreams++;
		if (log.isDebugEnabled()) {
			log.debug("Generated a memory leak from a not-closed stream");
		}
		if (isStored) {
			String title = "OpenStream-" + generated;
			TestData d = new TestData(title, data);
			leak.add(d);
			if (log.isDebugEnabled()) {
				log.debug("Allocated memory -> extra memory leak");
			}
		}

		if (isGc) {
			SystemUtil.gc("OpenStreamController");
		}

		model.put("openStreamCount", getOpenStreamCount());
		model.put("leakCount", getLeakCount());
		model.put("leakSize", getLeakSize() / MemoryLeakUtil.MB);
		model.put("storeChecked", isStored ? CHECKBOX_CHECKED : "");
		model.put("gcChecked", isGc ? CHECKBOX_CHECKED : "");

		return "redirect:" + redirect;
	}
}

