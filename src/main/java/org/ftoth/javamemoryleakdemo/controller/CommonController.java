package org.ftoth.javamemoryleakdemo.controller;

import org.apache.log4j.Logger;
import org.ftoth.javamemoryleakdemo.util.SystemUtil;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

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

	@RequestMapping("/memstat")
	public String memstat(@RequestParam(value = "redirect") String redirect) {
		String stat = SystemUtil.getMemoryStatus();
		if (log.isDebugEnabled()) {
			log.debug(stat);
		}
		return "redirect:" + redirect;
	}
}
