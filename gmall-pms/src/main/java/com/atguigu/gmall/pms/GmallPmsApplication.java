package com.atguigu.gmall.pms;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@MapperScan(basePackages = "com.atguigu.gmall.pms.dao")
@EnableSwagger2
@SpringBootApplication
@RefreshScope
@EnableDiscoveryClient
public class GmallPmsApplication {



	public static void main(String[] args) {
		SpringApplication.run(GmallPmsApplication.class, args);
	}

}
