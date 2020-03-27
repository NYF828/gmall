package com.atguigu.gmall.pms.service.impl;

import com.atguigu.gmall.pms.dao.AttrDao;
import com.atguigu.gmall.pms.dao.SkuInfoDao;
import com.atguigu.gmall.pms.dao.SpuInfoDescDao;
import com.atguigu.gmall.pms.entity.*;
import com.atguigu.gmall.pms.fegin.SkuSaleFeign;
import com.atguigu.gmall.pms.service.ProductAttrValueService;
import com.atguigu.gmall.pms.service.SkuImagesService;
import com.atguigu.gmall.pms.service.SkuSaleAttrValueService;
import com.atguigu.gmall.pms.vo.ProductAttrValueVO;
import com.atguigu.gmall.pms.vo.SkuInfoVO;
import com.atguigu.gmall.pms.vo.SpuInfoVO;
import com.atguigu.gmall.sms.vo.SkuSaleVO;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.atguigu.core.bean.PageVo;
import com.atguigu.core.bean.Query;
import com.atguigu.core.bean.QueryCondition;

import com.atguigu.gmall.pms.dao.SpuInfoDao;
import com.atguigu.gmall.pms.service.SpuInfoService;
import org.springframework.util.CollectionUtils;


@Service("spuInfoService")
public class SpuInfoServiceImpl extends ServiceImpl<SpuInfoDao, SpuInfoEntity> implements SpuInfoService {
	@Autowired
	private SpuInfoDescDao spuInfoDescDao;
	@Autowired
	private ProductAttrValueService productAttrValueService;
	@Autowired
	private SkuSaleFeign skuSaleFeign;
	@Autowired
	private SkuInfoDao skuInfoDao;
	@Autowired
	private AttrDao attrDao;
	@Autowired
	private SkuImagesService skuImagesService;
	@Autowired
	private SkuSaleAttrValueService skuSaleAttrValueService;

	@Override
	public PageVo queryPage(QueryCondition params) {
		IPage<SpuInfoEntity> page = this.page(
				new Query<SpuInfoEntity>().getPage(params),
				new QueryWrapper<SpuInfoEntity>()
		);

		return new PageVo(page);
	}

	@Override
	public PageVo querySpuInfo(QueryCondition condition, Long catId) {
		// 封装分页条件
		IPage<SpuInfoEntity> page = new Query<SpuInfoEntity>().getPage(condition);

		// 封装查询条件
		QueryWrapper<SpuInfoEntity> wrapper = new QueryWrapper<>();
		// 如果分类id不为0，要根据分类id查，否则查全部
		if (catId != 0) {
			wrapper.eq("catalog_id", catId);
		}
		// 如果用户输入了检索条件，根据检索条件查
		String key = condition.getKey();
		if (StringUtils.isNotBlank(key)) {
			wrapper.and(t -> t.like("spu_name", key).or().like("id", key));
		}

		return new PageVo(this.page(page, wrapper));
	}


	@Override
	public void saveSpuInfoVO(SpuInfoVO spuInfoVO) {
		/// 1.保存spu相关
		// 1.1. 保存spu基本信息 spu_info
		saveSpuInfo(spuInfoVO);

		// 1.2. 保存spu的描述信息 spu_info_desc
		saveSpuDesc(spuInfoVO);

		// 1.3. 保存spu的规格参数信息
		saveBaseAttrs(spuInfoVO);

		//保存sku相关信息及营销信息
		saveSkuInfoWithSaleInfo(spuInfoVO);

	}


	/**
	 * 保存sku相关信息及营销信息
	 *
	 * @param spuInfoVO
	 */
	private void saveSkuInfoWithSaleInfo(SpuInfoVO spuInfoVO) {
		List<SkuInfoVO> skuInfoVOs = spuInfoVO.getSkus();
		if (CollectionUtils.isEmpty(skuInfoVOs)) {
			return;
		}
		skuInfoVOs.forEach(skuInfoVO -> {
			// 2.1. 保存sku基本信息
			SkuInfoEntity skuInfoEntity = new SkuInfoEntity();
			BeanUtils.copyProperties(skuInfoVO, skuInfoEntity);
			// 品牌和分类的id需要从spuInfo中获取
			skuInfoEntity.setBrandId(spuInfoVO.getBrandId());
			skuInfoEntity.setCatalogId(spuInfoVO.getCatalogId());
			// 获取随机的uuid作为sku的编码
			skuInfoEntity.setSkuCode(UUID.randomUUID().toString().substring(0, 10).toUpperCase());
			// 获取图片列表
			List<String> images = skuInfoVO.getImages();
			// 如果图片列表不为null，则设置默认图片
			if (!CollectionUtils.isEmpty(images)) {
				// 设置第一张图片作为默认图片
				skuInfoEntity.setSkuDefaultImg(skuInfoEntity.getSkuDefaultImg() == null ? images.get(0) : skuInfoEntity.getSkuDefaultImg());
			}
			skuInfoEntity.setSpuId(spuInfoVO.getId());
			this.skuInfoDao.insert(skuInfoEntity);
			// 获取skuId
			Long skuId = skuInfoEntity.getSkuId();

			// 2.2. 保存sku图片信息
			if (!CollectionUtils.isEmpty(images)) {
				String defaultImage = images.get(0);
				List<SkuImagesEntity> skuImageses = images.stream().map(image -> {
					SkuImagesEntity skuImagesEntity = new SkuImagesEntity();
					skuImagesEntity.setDefaultImg(StringUtils.equals(defaultImage, image) ? 1 : 0);
					skuImagesEntity.setSkuId(skuId);
					skuImagesEntity.setImgSort(0);
					skuImagesEntity.setImgUrl(image);
					return skuImagesEntity;
				}).collect(Collectors.toList());
				this.skuImagesService.saveBatch(skuImageses);
			}

			// 2.3. 保存sku的规格参数（销售属性）
			List<SkuSaleAttrValueEntity> saleAttrs = skuInfoVO.getSaleAttrs();
			saleAttrs.forEach(saleAttr -> {
				// 设置属性名，需要根据id查询AttrEntity
				saleAttr.setAttrName(this.attrDao.selectById(saleAttr.getAttrId()).getAttrName());
				saleAttr.setAttrSort(0);
				saleAttr.setSkuId(skuId);
			});
			this.skuSaleAttrValueService.saveBatch(saleAttrs);

			// 3. 保存营销相关信息，需要远程调用gmall-sms
			SkuSaleVO skuSaleVO = new SkuSaleVO();
			BeanUtils.copyProperties(skuInfoVO, skuSaleVO);
			skuSaleVO.setSkuId(skuId);
			this.skuSaleFeign.saveSkuSaleInfo(skuSaleVO);
		});
	}

	/**
	 * 保存spu基本属性信息
	 *
	 * @param spuInfoVO
	 */
	private void saveBaseAttrs(SpuInfoVO spuInfoVO) {
		List<ProductAttrValueVO> baseAttrs = spuInfoVO.getBaseAttrs();
		if (!CollectionUtils.isEmpty(baseAttrs)) {
			List<ProductAttrValueEntity> productAttrValueEntities = baseAttrs.stream().map(productAttrValueVO -> {
				productAttrValueVO.setSpuId(spuInfoVO.getId());
				productAttrValueVO.setAttrSort(0);
				productAttrValueVO.setQuickShow(0);
				return productAttrValueVO;
			}).collect(Collectors.toList());
			this.productAttrValueService.saveBatch(productAttrValueEntities);
		}
	}

	/**
	 * 保存spu描述信息（图片）
	 *
	 * @param spuInfoVO
	 */
	private void saveSpuDesc(SpuInfoVO spuInfoVO) {
		SpuInfoDescEntity spuInfoDescEntity = new SpuInfoDescEntity();
		// 注意：spu_info_desc表的主键是spu_id,需要在实体类中配置该主键不是自增主键
		spuInfoDescEntity.setSpuId(spuInfoVO.getId());
		// 把商品的图片描述，保存到spu详情中，图片地址以逗号进行分割
		spuInfoDescEntity.setDecript(StringUtils.join(spuInfoVO.getSpuImages(), ","));
		this.spuInfoDescDao.insert(spuInfoDescEntity);
	}

	/**
	 * 保存spu基本信息
	 *
	 * @param spuInfoVO
	 */
	private void saveSpuInfo(SpuInfoVO spuInfoVO) {
		spuInfoVO.setPublishStatus(1); // 默认是已上架
		spuInfoVO.setCreateTime(new Date());
		spuInfoVO.setUodateTime(spuInfoVO.getCreateTime()); // 新增时，更新时间和创建时间一致
		this.save(spuInfoVO);
	}

}
