package com.atguigu.gmall.sms;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@MapperScan(basePackages = "com.atguigu.gmall.sms.dao")
@EnableSwagger2
@SpringBootApplication
@RefreshScope
@EnableDiscoveryClient
public class GmallSmsApplication {

	public static void main(String[] args) {
		SpringApplication.run(GmallSmsApplication.class, args);
	}

}
