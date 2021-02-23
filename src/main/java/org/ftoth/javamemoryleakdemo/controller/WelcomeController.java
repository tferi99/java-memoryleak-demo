package org.ftoth.javamemoryleakdemo.controller;

import java.util.Map;

import org.ftoth.javamemoryleakdemo.thread.MyTestThread;
import org.ftoth.javamemoryleakdemo.util.MemoryLeakUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class WelcomeController
{
	@Autowired
	MemAllocController memAllocController;

	@Autowired
	OpenStreamController openStreamController;

	// inject via application.properties
	@Value("${welcome.message:test}")
	private String message = "Hello World";

	@RequestMapping("/")
	public String welcome(Map<String, Object> model)
	{
		model.put("message", this.message);
		model.put("memAllocLeakCount", memAllocController.getLeakCount());
		model.put("memAllocLeakSize", memAllocController.getLeakSize() / MemoryLeakUtil.MB);
		model.put("streamCount", openStreamController.getOpenStreamCount());
		model.put("streamLeakCount", openStreamController.getLeakCount());
		model.put("streamLeakSize", openStreamController.getLeakSize() / MemoryLeakUtil.MB);
		model.put("threadCount", MyTestThread.getThreadsTotalCount());
		model.put("threadLeakSize", MyTestThread.getThreadsTotalAllocatedMBs());
		return "welcome";
	}
}

