package com.shindo.kill.model.dto;

import com.shindo.kill.model.entity.ItemKillSuccess;

import java.io.Serializable;

/**
 * @Description:
 * @Author: 杨耿
 * @Date: Create in 2021/1/14
 */
public class KillSuccessUserInfo extends ItemKillSuccess implements Serializable {
	private String userName;

	private String phone;

	private String email;

	private String itemName;

	@Override
	public String toString() {
		return super.toString() + "\nKillSuccessUserInfo{" +
				"userName='" + userName + '\'' +
				", phone='" + phone + '\'' +
				", email='" + email + '\'' +
				", itemName='" + itemName + '\'' +
				'}';
	}
}
