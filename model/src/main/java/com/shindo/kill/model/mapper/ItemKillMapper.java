package com.shindo.kill.model.mapper;

import com.shindo.kill.model.entity.ItemKill;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @Description:
 * @Author: 杨耿
 * @Date: Create in 2021/1/13
 */
public interface ItemKillMapper {
	List<ItemKill> selectAll();

	ItemKill selectById(@Param("id") Integer id);

	int updateKillItem(@Param("killId") Integer killId);
}
