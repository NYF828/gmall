package com.atguigu.gmall.pms.vo;

import com.atguigu.gmall.pms.entity.AttrAttrgroupRelationEntity;
import com.atguigu.gmall.pms.entity.AttrEntity;
import lombok.Data;

import java.util.List;

/**
 * @author nyf
 * @since 2020/3/26
 */
@Data
public class AttrGroupVO extends AttrEntity {
	private List<AttrEntity> attrEntities;
	private List<AttrAttrgroupRelationEntity> relations;
}
