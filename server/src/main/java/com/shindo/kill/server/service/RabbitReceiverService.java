package com.shindo.kill.server.service;

import com.shindo.kill.model.dto.KillSuccessUserInfo;
import com.shindo.kill.model.entity.ItemKillSuccess;
import com.shindo.kill.model.mapper.ItemKillSuccessMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

/**
 * @Description: RabbitMQ接收消息服务
 * @Author: 杨耿
 * @Date: Create in 2021/1/18
 */
@Service
public class RabbitReceiverService {
	public static final Logger log = LoggerFactory.getLogger(RabbitReceiverService.class);

	@Autowired
	private Environment env;

	@Autowired
	private ItemKillSuccessMapper itemKillSuccessMapper;


	/**
	 * 秒杀异步邮件通知-接收消息
	 *
	 * @param info
	 */
	@RabbitListener(queues = {"${mq.kill.item.success.email.queue}"}, containerFactory = "singleListenerContainer")
	public void consumeEmailMsg(KillSuccessUserInfo info) {
		try {
			log.info("秒杀异步邮件通知-接收消息:{}", info);

			final String content = String.format(env.getProperty("mail.kill.item.success.content"), info.getItemName(), info.getCode());
			System.out.println(content);

		} catch (Exception e) {
			log.error("秒杀异步邮件通知-接收消息-发生异常:", e.fillInStackTrace());
		}
	}

	/**
	 * 用户秒杀成功后超时未支付-监听者
	 *
	 * @param info
	 */
	@RabbitListener(queues = {"${mq.kill.item.success.kill.dead.real.queue}"}, containerFactory = "singleListenerContainer")
	public void consumeExpireOrder(KillSuccessUserInfo info) {
		try {
			log.info("用户秒杀成功后超时未支付-监听者-接收消息:{}", info);
			if (null != info) {
				ItemKillSuccess entity = itemKillSuccessMapper.selectByPrimaryKey(info.getCode());
				if (null != entity && entity.getStatus().intValue() == 0) {
					itemKillSuccessMapper.expireOrder(info.getCode());
				}
			}
		} catch (Exception e) {
			log.error("用户秒杀成功后超时未支付-监听者-发生异常:", e.fillInStackTrace());
		}
	}

}
