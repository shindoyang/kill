package com.shindo.kill.server.utils;

import org.joda.time.DateTime;

import java.text.SimpleDateFormat;
import java.util.concurrent.ThreadLocalRandom;

/**
 * @Description: 随机数生成util
 * @Author: 杨耿
 * @Date: Create in 2021/1/18
 */
public class RandomUtil {
	private static final SimpleDateFormat dateFormatOne = new SimpleDateFormat("yyyyMMddHHmmssSS");

	private static final ThreadLocalRandom random = ThreadLocalRandom.current();

	/**
	 * 生成订单编号-方式一
	 *
	 * @return
	 */
	public static String generateOrderCode() {
		//TODO：时间戳+N位随机数流水号
		return dateFormatOne.format(DateTime.now().toDate()) + generateNumber(4);
	}

	//N位随机数流水号
	public static String generateNumber(final int num) {
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < num; i++) {
			sb.append(random.nextInt(9));
		}
		return sb.toString();
	}

}
