package com.atguigu.gmall.pms.service.impl;

import com.atguigu.gmall.pms.dao.AttrAttrgroupRelationDao;
import com.atguigu.gmall.pms.dao.AttrDao;
import com.atguigu.gmall.pms.entity.AttrAttrgroupRelationEntity;
import com.atguigu.gmall.pms.entity.AttrEntity;
import com.atguigu.gmall.pms.vo.AttrGroupVO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.atguigu.core.bean.PageVo;
import com.atguigu.core.bean.Query;
import com.atguigu.core.bean.QueryCondition;

import com.atguigu.gmall.pms.dao.AttrGroupDao;
import com.atguigu.gmall.pms.entity.AttrGroupEntity;
import com.atguigu.gmall.pms.service.AttrGroupService;
import org.springframework.util.CollectionUtils;


@Service("attrGroupService")
public class AttrGroupServiceImpl extends ServiceImpl<AttrGroupDao, AttrGroupEntity> implements AttrGroupService {
	@Autowired
	private AttrGroupDao attrGroupDao;

	@Autowired
	private AttrAttrgroupRelationDao relationDao;

	@Autowired
	private AttrDao attrDao;

	@Override
	public PageVo queryPage(QueryCondition params) {
		IPage<AttrGroupEntity> page = this.page(
				new Query<AttrGroupEntity>().getPage(params),
				new QueryWrapper<AttrGroupEntity>()
		);

		return new PageVo(page);
	}

	@Override
	public PageVo queryByCidPage(Long cid, QueryCondition condition) {
		IPage<AttrGroupEntity> groupEntityIPage = this.page(
				new Query<AttrGroupEntity>().getPage(condition),
				new QueryWrapper<AttrGroupEntity>().eq("catelog_id", cid));
		return new PageVo(groupEntityIPage);
	}


	@Override
	public AttrGroupVO queryById(Long gid) {

		// 查询分组
		AttrGroupVO attrGroupVO = new AttrGroupVO();
		AttrGroupEntity attrGroupEntity = this.attrGroupDao.selectById(gid);
		BeanUtils.copyProperties(attrGroupEntity, attrGroupVO);

		// 查询分组下的关联关系
		List<AttrAttrgroupRelationEntity> relations = this.relationDao.selectList(new QueryWrapper<AttrAttrgroupRelationEntity>().eq("attr_group_id", gid));
		// 判断关联关系是否为空，如果为空，直接返回
		if (CollectionUtils.isEmpty(relations)) {
			return attrGroupVO;
		}
		attrGroupVO.setRelations(relations);

		// 收集分组下的所有规格id
		List<Long> attrIds = relations.stream().map(relation -> relation.getAttrId()).collect(Collectors.toList());
		// 查询分组下的所有规格参数
		List<AttrEntity> attrEntities = this.attrDao.selectBatchIds(attrIds);
		attrGroupVO.setAttrEntities(attrEntities);

		return attrGroupVO;
	}

	@Override
	public List<AttrGroupVO> queryByCid(Long cid) {

		// 查询所有的分组
		List<AttrGroupEntity> attrGroupEntities = this.list(new QueryWrapper<AttrGroupEntity>().eq("catelog_id", cid));

		// 查询出每组下的规格参数
		List<AttrGroupVO> attrGroupVOs = attrGroupEntities.stream().map(attrGroupEntity -> {
			return this.queryById(attrGroupEntity.getAttrGroupId());
		}).collect(Collectors.toList());

		return attrGroupVOs;
	}
}
