package org.ftoth.javamemoryleakdemo.controller;

import java.util.Map;

import org.ftoth.javamemoryleakdemo.model.MyTestThread;
import org.ftoth.javamemoryleakdemo.model.TestDataUtil;
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
		model.put("memAllocLeakCount", TestDataUtil.getLeakCount());
		model.put("memAllocLeakSize", TestDataUtil.getLeakSize() / TestDataUtil.MB);
		model.put("streamCount", openStreamController.getOpenStreamCount());
		model.put("streamLeakCount", openStreamController.getLeakCount());
		model.put("streamLeakSize", openStreamController.getLeakSize() / TestDataUtil.MB);
		model.put("threadCount", MyTestThread.getThreadsTotalCount());
		model.put("threadLeakSize", MyTestThread.getThreadsTotalAllocatedMBs());
		return "welcome";
	}
}

