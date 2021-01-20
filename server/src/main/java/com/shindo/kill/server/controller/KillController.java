package com.shindo.kill.server.controller;

import com.google.common.base.Strings;
import com.shindo.kill.api.enums.StatusCode;
import com.shindo.kill.api.response.BaseResponse;
import com.shindo.kill.model.dto.KillSuccessUserInfo;
import com.shindo.kill.model.mapper.ItemKillSuccessMapper;
import com.shindo.kill.server.dto.KillDto;
import com.shindo.kill.server.service.IKillService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;

/**
 * @Description: 秒杀controller
 * @Author: 杨耿
 * @Date: Create in 2021/1/18
 */
@Controller
public class KillController {
	private static final Logger log = LoggerFactory.getLogger(KillController.class);

	private static final String prefix = "kill";

	@Autowired
	private IKillService killService;

	@Autowired
	private ItemKillSuccessMapper itemKillSuccessMapper;

	@RequestMapping(value = prefix + "/execute", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
	@ResponseBody
	public BaseResponse excute(@RequestBody @Validated KillDto dto, BindingResult result, HttpSession session) {
		if (result.hasErrors() || dto.getKillId() <= 0) {
			return new BaseResponse(StatusCode.InvalidParams);
		}
		Integer userId = dto.getUserId();

		BaseResponse response = new BaseResponse(StatusCode.Success);
		try {
			Boolean res = killService.killItem(dto.getKillId(), userId);
			if (!res) {
				return new BaseResponse(StatusCode.Fail.getCode(), "哈哈~商品已抢购完毕或者不在抢购时间段哦！");
			}
		} catch (Exception e) {
			response = new BaseResponse(StatusCode.Fail.getCode(), e.getMessage());
		}
		return response;
	}

	@RequestMapping(value = prefix + "/execute/lock", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
	@ResponseBody
	public BaseResponse excuteLock(@RequestBody @Validated KillDto dto, BindingResult result, HttpSession session) {
		if (result.hasErrors() || dto.getKillId() <= 0) {
			return new BaseResponse(StatusCode.InvalidParams);
		}
		BaseResponse response = new BaseResponse(StatusCode.Success);
		try {
			//不加分布式锁的前提
			Boolean res = killService.killItemV2(dto.getKillId(), dto.getUserId());
			if (!res) {
				return new BaseResponse(StatusCode.Fail.getCode(), "不加分布式锁~哈哈~商品已抢购完毕或者不在抢购时间段哦！");
			}
		} catch (Exception e) {
			response = new BaseResponse(StatusCode.Fail.getCode(), e.getMessage());
		}
		return response;
	}

	//http://localhost:8092/kill/kill/record/detail/202101190823417181238

	/**
	 * 查看订单详情
	 */
	@RequestMapping(value = prefix + "/record/detail/{orderNo}", method = RequestMethod.GET)
	public String killRecordDetail(@PathVariable String orderNo, ModelMap modelMap) {
		if (Strings.isNullOrEmpty(orderNo)) {
			return "error";
		}
		KillSuccessUserInfo info = itemKillSuccessMapper.selectByCode(orderNo);
		if (null == info) {
			return "error";
		}
		modelMap.put("info", info);
		return "killRecord";
	}

	//抢购成功跳转页面
	@RequestMapping(value = prefix + "/execute/success", method = RequestMethod.GET)
	public String executeSuccess() {
		return "executeSuccess";
	}

	//抢购失败跳转页面
	@RequestMapping(value = prefix + "/execute/fail", method = RequestMethod.GET)
	public String executeFail() {
		return "executeFail";
	}


}
