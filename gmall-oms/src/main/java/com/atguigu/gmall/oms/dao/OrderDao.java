package com.atguigu.gmall.oms.dao;

import com.atguigu.gmall.oms.entity.OrderEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 订单
 * 
 * @author nyf828
 * @email niuyangfan@outlook.com
 * @date 2020-03-25 13:03:12
 */
@Mapper
public interface OrderDao extends BaseMapper<OrderEntity> {
	
}
