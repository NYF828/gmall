package com.atguigu.gmall.sms.feign;

import com.atguigu.core.bean.Resp;
import com.atguigu.gmall.sms.vo.SkuSaleVO;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * @author nyf
 * @since 2020/3/27
 */
public interface SmsSaleApi {
	@PostMapping("/sms/skubounds/skusale/save")
	public Resp<Object> saveSkuSaleInfo(@RequestBody SkuSaleVO skuSaleVO);
}
