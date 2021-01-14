package com.shindo.kill.server.controller;

import com.google.common.base.Strings;
import com.shindo.kill.api.enums.StatusCode;
import com.shindo.kill.api.response.BaseResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

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

	/**
	 * 通过@ResponseBody直接响应数据，不做页面跳转
	 *
	 * @param name
	 * @return
	 */
	@RequestMapping(value = "/data", method = RequestMethod.GET)
	@ResponseBody
	public String data(String name) {
		if (Strings.isNullOrEmpty(name)) {
			name = "这是welcome！";
		}
		return name;
	}

	/**
	 * 组装正规的响应结构体，chrome浏览器可以安排FeHelper插件
	 *
	 * @param name
	 * @return
	 */
	@RequestMapping(value = "/response", method = RequestMethod.GET)
	@ResponseBody
	public BaseResponse response(String name) {
		BaseResponse baseResponse = new BaseResponse(StatusCode.Success);
		if (Strings.isNullOrEmpty(name)) {
			name = "这是welcome！";
		}
		baseResponse.setData(name);
		return baseResponse;
	}

}
