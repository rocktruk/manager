package com.online.mall.manager.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
	 * datatable分页查询
	 * @param goods
	 * @param sort
	 * @param index
	 * @param num
	 * @return
	 */
	public Map<String,Object> searchGoodsForDataTable(GoodsWithoutDetail goods,Sort sort,int index,int num){
		Map<String,Object> map = new HashMap<String, Object>();
		List<GoodsWithoutDetail> ls = findAllGoodsWithMenuAndPage(goods, sort, index, num);
		long total = noDetailRepos.count();
		map.put("recordsTotal", total);
		map.put("recordsFiltered", total);
		map.put("draw", index+1);
		map.put("data", ls);
		return map;
	}
	
	/**
	 * 关联查询商品信息
	 * @param goods
	 * @param sort
	 * @param index
	 * @param num
	 * @return
	 */
	public List<GoodsWithoutDetail> findAllGoodsWithMenuAndPage(GoodsWithoutDetail goods,Sort sort,int index,int num){
		if(goods != null) {
			goods = new GoodsWithoutDetail();
		}
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
		List<GoodsWithoutDetail> goodsls = noDetailRepos.findAll(example,page).getContent();
		return goodsls;
	}
	
	
	
	@Transactional
	public void insertGoods(Goods goods)
	{
		goodRepository.save(goods);
	}
	
	
	
	
	
	
	
}
