package com.shindo.kill.server.controller;

import com.shindo.kill.model.entity.ItemKill;
import com.shindo.kill.server.service.IItemService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;


/**
 * @Description: 待秒杀商品Controller
 * @Author: 杨耿
 * @Date: Create in 2021/1/13
 */
@Controller
public class ItemController {
	private static final Logger log = LoggerFactory.getLogger(ItemController.class);

	private static final String prefix = "item";

	@Autowired
	private IItemService itemService;

	/**
	 * 获取商品列表
	 */
	@RequestMapping(value = {"/*", "index", prefix + "/list", prefix + "/index.html"}, method = RequestMethod.GET)
	public String list(ModelMap modelMap) {
		try {
			List<ItemKill> list = itemService.getKillItems();
			modelMap.put("list", list);

			log.info("获取待秒杀商品列表-数据：{}", list);
		} catch (Exception e) {
			log.error("获取待秒杀商品列表-发生异常：", e.fillInStackTrace());
			return "redirect:/base/error";
		}
		return "list";
	}

	/**
	 * 获取待秒杀商品的详情
	 *
	 * @param id
	 * @return
	 */
	@RequestMapping(value = prefix + "/detail/{id}", method = RequestMethod.GET)
	public String detail(@PathVariable Integer id, ModelMap modelMap) {
		if (null == id || id < 0) {
			return "redirect:/base/error";
		}
		try {
			ItemKill detail = itemService.getKillDetail(id);
			modelMap.put("detail", detail);
		} catch (Exception e) {
			log.error("获取待秒杀商品详情-发生异常： id = {}", id, e.fillInStackTrace());
			return "redirect:/base/error";
		}

		return "info";
	}
}
