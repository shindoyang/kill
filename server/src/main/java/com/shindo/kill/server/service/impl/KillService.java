package com.shindo.kill.server.service.impl;

import com.shindo.kill.model.entity.ItemKill;
import com.shindo.kill.model.entity.ItemKillSuccess;
import com.shindo.kill.model.mapper.ItemKillMapper;
import com.shindo.kill.model.mapper.ItemKillSuccessMapper;
import com.shindo.kill.server.enums.SysConstant;
import com.shindo.kill.server.service.IKillService;
import com.shindo.kill.server.service.RabbitSenderService;
import com.shindo.kill.server.utils.SnowFlake;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @Description:
 * @Author: 杨耿
 * @Date: Create in 2021/1/18
 */
@Service
public class KillService implements IKillService {
	private static final Logger log = LoggerFactory.getLogger(KillService.class);

	private SnowFlake snowFlake = new SnowFlake(2, 3);

	@Autowired
	private ItemKillSuccessMapper itemKillSuccessMapper;

	@Autowired
	private ItemKillMapper itemKillMapper;

	@Autowired
	private RabbitSenderService rabbitSenderService;

	/**
	 * 商品秒杀核心业务逻辑的处理
	 */
	@Override
	public Boolean killItem(Integer killId, Integer userId) throws Exception {
		Boolean result = false;
		//TODO:判断当前用户是否已经抢购过当前商品
		if (itemKillSuccessMapper.countByKillUserId(killId, userId) <= 0) {
			//TODO:查询待秒杀商品详情
			ItemKill itemKill = itemKillMapper.selectById(killId);

			//TODO:判断是否可以被秒杀canKill=1?
			if (itemKill != null && 1 == itemKill.getCanKill()) {
				//TODO:扣减库存- 减一
				int res = itemKillMapper.updateKillItem(killId);

				//TODO:扣减是否成功？是-生成秒杀成功的订单，同时通知用户秒杀成功的消息
				if (res > 0) {
					commonRecordKillSuccessInfo(itemKill, userId);

					result = true;
				}
			}
		} else {
			throw new Exception("您已经抢购过该商品了!");
		}
		return result;
	}

	/**
	 * 通用的方法-记录用户秒杀成功后生成的订单-并进行异步邮件消息的通知
	 *
	 * @param kill
	 * @param userId
	 * @throws Exception
	 */
	private void commonRecordKillSuccessInfo(ItemKill kill, Integer userId) throws Exception {
		//TODO:记录抢购成功后生成的秒杀订单记录

		ItemKillSuccess entity = new ItemKillSuccess();
		String orderNo = String.valueOf(snowFlake.nextId());

//		entity.setCode(RandomUtil.generateOrderCode());//传统时间戳+N位随机数
		entity.setCode(orderNo);//雪花算法
		entity.setItemId(kill.getItemId());
		entity.setKillId(kill.getId());
		entity.setUserId(userId.toString());
		entity.setStatus(SysConstant.OrderStatus.SuccessNotPayed.getCode().byteValue());
		entity.setCreateTime(DateTime.now().toDate());
		//TODO:举一反三，参考单例模式的双重检查锁
		//TODO:再次判断当前用户是否已经抢购过当前商品
		if (itemKillSuccessMapper.countByKillUserId(kill.getId(), userId) <= 0) {
			int res = itemKillSuccessMapper.insertSelective(entity);

			if (res > 0) {
				//TODO：进行异步邮件消息的通知=rabbitmq+email
				rabbitSenderService.sendKillSuccessEmailMsg(orderNo);

				//TODO:入死信队列，用于"失效"超过指定的TTL时间仍然未支付的订单(TTL:time to live 存活时间)
				rabbitSenderService.sendKillSuccessOrderExpireMsg(orderNo);
			}
		}
	}

	/**
	 * 商品秒杀核心业务逻辑的处理
	 */
	@Override
	public Boolean killItemV2(Integer killId, Integer userId) throws Exception {
		Boolean result = false;
		//TODO:判断当前用户是否已经抢购过当前商品
		if (itemKillSuccessMapper.countByKillUserId(killId, userId) <= 0) {
			//TODO:A.查询待秒杀商品详情
			ItemKill itemKill = itemKillMapper.selectByIdV2(killId);

			//TODO:判断是否可以被秒杀canKill=1?
			if (itemKill != null && 1 == itemKill.getCanKill() && itemKill.getTotal() > 0) {
				//TODO:B.扣减库存- 减一
				int res = itemKillMapper.updateKillItemV2(killId);

				//TODO:扣减是否成功？是-生成秒杀成功的订单，同时通知用户秒杀成功的消息
				if (res > 0) {
					commonRecordKillSuccessInfo(itemKill, userId);

					result = true;
				}
			}
		} else {
			throw new Exception("您已经抢购过该商品了!");
		}
		return result;
	}
}
