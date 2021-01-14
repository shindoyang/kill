package com.shindo.kill.server.controller;

import com.google.common.base.Strings;
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
	public String welcome(String name, ModelMap modelMap) {
		/**
		 * 如果只是页面跳转，只需要返回指定页面的 名字就可以
		 * 但是如果想要把业务数据带到页面中，就需要在接口入参上加上ModelMap
		 */
		log.info("request in welcome interface");
		if (Strings.isNullOrEmpty(name)) {
			name = "这是welcome！";
		}
		modelMap.put("name", name);
		return "welcome";
	}
	

}
