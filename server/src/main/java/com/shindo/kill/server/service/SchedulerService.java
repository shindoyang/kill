package com.shindo.kill.server.service;

import com.shindo.kill.model.entity.ItemKillSuccess;
import com.shindo.kill.model.mapper.ItemKillSuccessMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.List;


/**
 * @Description: 定时任务服务
 * @Author: 杨耿
 * @Date: Create in 2021/1/19
 */
@Service
public class SchedulerService {
	private static final Logger log = LoggerFactory.getLogger(SchedulerService.class);

	@Autowired
	private ItemKillSuccessMapper itemKillSuccessMapper;

	@Autowired
	private Environment env;

	/**
	 * 定时获取status=0的订单并判断是否超过TTL,然后进行失效
	 */
	@Scheduled(cron = "0/10 * * * * ?")
	public void schedulerExpireOrders() {
		log.info("v1的定时任务");

		try {
			List<ItemKillSuccess> list = itemKillSuccessMapper.selectExpireOrders();
			if (null != list && !list.isEmpty()) {
				list.stream().forEach(i -> {
					if (i != null && i.getDiffTime() > env.getProperty("scheduler.expire.orders.time", Integer.class)) {
						itemKillSuccessMapper.expireOrder(i.getCode());
					}
				});
			}

			//for(ItemKillSuccess entity : list){}//非java8写法
		} catch (Exception e) {
			log.error("定时获取status=0的订单并判断是否超过TTL,然后进行失效-发生异常:", e.fillInStackTrace());
		}
	}

}
