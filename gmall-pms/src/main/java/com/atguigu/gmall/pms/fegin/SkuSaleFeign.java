package com.atguigu.gmall.pms.fegin;

import com.atguigu.core.bean.Resp;
import com.atguigu.gmall.sms.feign.SmsSaleApi;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * @author nyf
 * @since 2020/3/26
 */
@FeignClient("sms-service")
public interface SkuSaleFeign extends SmsSaleApi {

}
