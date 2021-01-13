package com.shindo.kill.model.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;

/**
 * @Description:
 * @Author: 杨耿
 * @Date: Create in 2021/1/13
 */
@Data
public class ItemKill {
	private Integer id;

	private Integer itemId;

	private Integer total;

	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	private Data startTime;

	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	private Data endTime;

	private Byte isActive;

	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	private Date createTime;

	private String itemName;

	//采用服务器时间控制是否可以进行抢购
	private Integer canKill;
}
