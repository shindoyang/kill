package com.shindo.kill.server.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @Description:
 * @Author: 杨耿
 * @Date: Create in 2021/1/14
 */
@Controller
@RequestMapping("base")
public class BaseController {
	private static final Logger log = LoggerFactory.getLogger(BaseController.class);

	@GetMapping("/welcome")
	public String welcome(ModelMap modelMap) {
		return "welcome";
	}

}
