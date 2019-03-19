package com.online.mall.manager.service;

import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.online.mall.manager.entity.Goods;
import com.online.mall.manager.entity.GoodsWithoutDetail;
import com.online.mall.manager.repository.GoodsRepository;
import com.online.mall.manager.repository.GoodsWithoutDetailRepository;

@Service
public class GoodsService {
	
	
	private static final Logger log = LoggerFactory.getLogger(GoodsService.class);

	@Autowired
	private GoodsRepository goodRepository;
	
	@Autowired
	private GoodsWithoutDetailRepository noDetailRepos;
	
	
	public List<GoodsWithoutDetail> findGoodsByMenu(int menuId)
	{
		List<GoodsWithoutDetail> ls = noDetailRepos.selectGoodsByGoodsMenuId(menuId);
		return ls;
	}
	
	/**
	 * 
	 * @param menuId 商品菜单id
	 * @param sort 排序
	 * @param index 从0开始的页面索引
	 * @param num 每次查询的记录数
	 * @return
	 */
	public List<GoodsWithoutDetail> findGoodsByMenuWithSort(int menuId,Sort sort,int index,int num)
	{
		GoodsWithoutDetail goods = new GoodsWithoutDetail();
		goods.setGoodsMenuId(menuId);
		ExampleMatcher matcher = ExampleMatcher.matching().withIgnorePaths("inventory","totalSales","monthSales","carriage");
		Example<GoodsWithoutDetail> example = Example.of(goods,matcher);
		PageRequest page = null;
		if(sort == null)
		{
			page = PageRequest.of(index, num);
		}else
		{
			page = PageRequest.of(index, num, sort);
		}
		return noDetailRepos.findAll(example,page).getContent();
	}
	
	
	@Transactional
	public boolean insertGoods(Goods goods)
	{
		int n = goodRepository.insertGoods(goods.getId(), goods.getPrice(), goods.getBrand(), goods.getDetail(), goods.getInventory(), goods.getStatus(), goods.getImgPath(), goods.getGoodsMenuId(), goods.getSpecification(), goods.getTitle(), goods.getMonthSales(), goods.getTotalSales());
		if(n == 1) {
			return true;
		}else
		{
			return false;
		}
	}
	
	/**
	 * 根据商品ID查询商品信息
	 * @param goodsId
	 * @return
	 */
	public Optional<Goods> getProduct(String goodsId)
	{
		return goodRepository.findById(goodsId);
	}
	
	
	
	
	
}
