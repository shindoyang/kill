package com.shindo.kill.server.service;

/**
 * @Description:
 * @Author: 杨耿
 * @Date: Create in 2021/1/18
 */
public interface IKillService {
	Boolean killItem(Integer killId, Integer userId) throws Exception;

	Boolean killItemV2(Integer killId, Integer userId) throws Exception;
}
