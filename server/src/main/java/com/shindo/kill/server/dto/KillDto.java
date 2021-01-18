package com.shindo.kill.server.dto;

import lombok.Data;
import lombok.ToString;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * @Description:
 * @Author: 杨耿
 * @Date: Create in 2021/1/18
 */
@Data
@ToString
public class KillDto implements Serializable {
	@NotNull
	private Integer killId;

	private Integer userId;
}
