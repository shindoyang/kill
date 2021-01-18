package com.shindo.kill.server.controller;

import com.shindo.kill.api.enums.StatusCode;
import com.shindo.kill.api.response.BaseResponse;
import com.shindo.kill.model.mapper.ItemKillSuccessMapper;
import com.shindo.kill.server.dto.KillDto;
import com.shindo.kill.server.service.IKillService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

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

}
