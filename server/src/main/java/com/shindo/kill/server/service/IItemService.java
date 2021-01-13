package com.shindo.kill.server.service;

import com.shindo.kill.model.entity.ItemKill;

import java.util.List;

/**
 * @Description:
 * @Author: 杨耿
 * @Date: Create in 2021/1/13
 */
public interface IItemService {
	List<ItemKill> getKillItems() throws Exception;
}
