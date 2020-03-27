package com.atguigu.gmall.pms.vo;

import com.atguigu.gmall.pms.entity.ProductAttrValueEntity;
import lombok.Data;
import org.apache.commons.lang.StringUtils;
import org.springframework.util.CollectionUtils;

import java.util.List;

/**
 * @author nyf
 * @since 2020/3/26
 */
@Data
public class ProductAttrValueVO extends ProductAttrValueEntity {

	public void setValueSelected(List<Object> valueSelected){
		if (CollectionUtils.isEmpty(valueSelected)){
			return;
		}
		this.setAttrValue(StringUtils.join(valueSelected,","));
	}
}
