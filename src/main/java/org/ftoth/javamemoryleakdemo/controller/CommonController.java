package org.ftoth.javamemoryleakdemo.controller;

import org.apache.log4j.Logger;
import org.ftoth.javamemoryleakdemo.model.MyTestThread;
import org.ftoth.javamemoryleakdemo.util.SystemUtil;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Map;

@Controller
public class CommonController
{
	private static Logger log = Logger.getLogger(CommonController.class);

	@RequestMapping("/gc")
	public String dc(@RequestParam(value = "redirect") String redirect, Map<String, Object> model)
	{
		SystemUtil.gc("CommonController");
		return "redirect:" + redirect;
	}

	@RequestMapping("/stat")
	public String memstat(@RequestParam(value = "redirect") String redirect) {
		String allocStat = MemAllocController.getAllocatingStatus();
		String threadStat = MyTestThread.getThreadStatus();
		String memStat = SystemUtil.getMemoryStatus();
		if (log.isDebugEnabled()) {
			log.debug("-------------------------------------------------------------------");
			log.debug(allocStat);
			log.debug(threadStat);
			log.debug(memStat);
		}
		return "redirect:" + redirect;
	}
}
