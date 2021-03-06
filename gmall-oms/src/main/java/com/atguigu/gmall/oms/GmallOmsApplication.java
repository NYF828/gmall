package com.atguigu.gmall.oms;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * @author nyf
 * @since 2020/3/25
 */
@MapperScan(basePackages = "com.atguigu.gmall.oms.dao")
@EnableSwagger2
@SpringBootApplication
@RefreshScope
@EnableDiscoveryClient
public class GmallOmsApplication {

	public static void main(String[] args) {
		SpringApplication.run(GmallOmsApplication.class, args);
	}
}
