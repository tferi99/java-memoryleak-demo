package org.ftoth.javaprofilerdemo.controller;

import org.ftoth.javaprofilerdemo.model.TestData;
import org.ftoth.javaprofilerdemo.util.MemoryLeakUtil;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Controller
public class ErrorController
{
	@RequestMapping("/error2")
	public String error(Map<String, Object> model)
	{
		return "error";
	}
}

