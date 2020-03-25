package com.atguigu.gmall.pms.dao;

import com.atguigu.gmall.pms.entity.CategoryEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 商品三级分类
 * 
 * @author nyf828
 * @email niuyangfan@outlook.com
 * @date 2020-03-24 20:54:37
 */
@Mapper
public interface CategoryDao extends BaseMapper<CategoryEntity> {
	
}
