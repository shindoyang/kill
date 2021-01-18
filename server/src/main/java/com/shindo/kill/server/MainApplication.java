package com.shindo.kill.server;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.support.SpringBootServletInitializer;
import org.springframework.context.annotation.ImportResource;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * @Description:
 * @Author: 杨耿
 * @Date: Create in 2021/1/13
 */
@SpringBootApplication
@ImportResource(value = {"classpath:spring/spring-jdbc.xml"})
@MapperScan(basePackages = "com.shindo.kill.model.mapper")
@EnableScheduling
public class MainApplication extends SpringBootServletInitializer {

	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
		return builder.sources(MainApplication.class);
	}

	public static void main(String[] args) {
		SpringApplication.run(MainApplication.class, args);
	}
}
