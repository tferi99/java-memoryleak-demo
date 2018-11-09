package org.ftoth.javamemoryleakdemo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

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

