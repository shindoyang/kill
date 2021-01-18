package com.shindo.kill.server.service;

import com.google.common.base.Strings;
import com.shindo.kill.model.dto.KillSuccessUserInfo;
import com.shindo.kill.model.mapper.ItemKillSuccessMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.AmqpException;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageDeliveryMode;
import org.springframework.amqp.core.MessagePostProcessor;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.AbstractJavaTypeMapper;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

/**
 * @Description: RabbitMQ发送邮件服务
 * @Author: 杨耿
 * @Date: Create in 2021/1/18
 */
@Service
public class RabbitSenderService {
	private static final Logger log = LoggerFactory.getLogger(RabbitSenderService.class);

	@Autowired
	private RabbitTemplate rabbitTemplate;

	@Autowired
	private Environment env;

	@Autowired
	private ItemKillSuccessMapper itemKillSuccessMapper;

	/**
	 * 秒杀成功异步发送邮件通知消息
	 */
	public void sendKillSuccessEmailMsg(String orderNo) {
		log.info("秒杀成功异步发送邮件通知消息-准备发送消息:{}", orderNo);

		try {
			if (!Strings.isNullOrEmpty(orderNo)) {
				KillSuccessUserInfo info = itemKillSuccessMapper.selectByCode(orderNo);
				if (null != info) {
					//TODO:rabbitmq发送消息的逻辑
					rabbitTemplate.setMessageConverter(new Jackson2JsonMessageConverter());
					rabbitTemplate.setExchange(env.getProperty("mq.kill.item.success.email.exchange"));
					rabbitTemplate.setRoutingKey(env.getProperty("mq.kill.item.success.email.routing.key"));

					//TODO:将info充当消息发送至队列
					rabbitTemplate.convertAndSend(info, new MessagePostProcessor() {
						@Override
						public Message postProcessMessage(Message message) throws AmqpException {
							MessageProperties messageProperties = message.getMessageProperties();
							messageProperties.setDeliveryMode(MessageDeliveryMode.PERSISTENT);
							messageProperties.setHeader(AbstractJavaTypeMapper.DEFAULT_CONTENT_CLASSID_FIELD_NAME, KillSuccessUserInfo.class);
							return message;
						}
					});
				}
			}
		} catch (Exception e) {
			log.error("秒杀成功异步发送邮件通知消息-发生异常,消息为:{}", orderNo, e.fillInStackTrace());
		}


	}
}
