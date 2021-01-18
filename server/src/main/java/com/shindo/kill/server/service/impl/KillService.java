package com.shindo.kill.server.service.impl;

import com.shindo.kill.server.service.IKillService;
import org.springframework.stereotype.Service;

/**
 * @Description:
 * @Author: 杨耿
 * @Date: Create in 2021/1/18
 */
@Service
public class KillService implements IKillService {

	@Override
	public Boolean killItem(Integer killId, Integer userId) throws Exception {
		System.out.println("进入抢购逻辑！");
		return null;
	}
}
