package com.atguigu.gmall.ums.dao;

import com.atguigu.gmall.ums.entity.MemberEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 会员
 * 
 * @author nyf828
 * @email niuyangfan@outlook.com
 * @date 2020-03-25 13:07:14
 */
@Mapper
public interface MemberDao extends BaseMapper<MemberEntity> {
	
}
