package com.atguigu.gmall.pms.vo;

import com.atguigu.gmall.pms.entity.SpuInfoEntity;
import lombok.Data;

import java.util.List;

/**
 * @author nyf
 * @since 2020/3/26
 */
@Data
public class SpuInfoVO extends SpuInfoEntity {
	//图片信息
	private List<String> spuImages;
	private List<ProductAttrValueVO> baseAttrs;
	private List<SkuInfoVO> skus;

}
