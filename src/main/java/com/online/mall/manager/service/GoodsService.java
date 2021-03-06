package com.online.mall.manager.service;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.StringJoiner;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.online.mall.manager.common.ConfigConstants;
import com.online.mall.manager.common.IConstants;
import com.online.mall.manager.common.RespConstantsUtil;
import com.online.mall.manager.entity.Goods;
import com.online.mall.manager.entity.GoodsWithoutDetail;
import com.online.mall.manager.repository.GoodsRepository;
import com.online.mall.manager.repository.GoodsWithoutDetailRepository;

@Service
public class GoodsService extends AbstractMallService{
	
	
	private static final Logger log = LoggerFactory.getLogger(GoodsService.class);

	@Autowired
	private GoodsRepository goodRepository;
	
	@Autowired
	private GoodsWithoutDetailRepository noDetailRepos;
	
	public static final Map<Integer,String> orderColumn;
	static {
		orderColumn = new HashMap<Integer, String>();
		orderColumn.put(1, "title");
		orderColumn.put(2, "brand");
		orderColumn.put(3, "price");
		orderColumn.put(4, "carriage");
		orderColumn.put(10, "monthSales");
		orderColumn.put(11, "totalSales");
		orderColumn.put(12, "status");
	}
	
	
	public String orderGoods(int orderSeq) {
		return orderColumn.get(orderSeq);
	}
	
	/**
	 * 查询所有已上架商品的id,title
	 * @return
	 */
	public List<GoodsWithoutDetail> findAllGoodsPutAway(){
		return noDetailRepos.findAllGoodsPutAway();
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
		map.put("nextstart", index+1);
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
		if(goods == null) {
			goods = new GoodsWithoutDetail();
		}
		ExampleMatcher matcher = ExampleMatcher.matching().withIgnorePaths("inventory","totalSales","monthSales","carriage");
		if(goods.getTitle()!=null)	{
			matcher.withMatcher("title", ExampleMatcher.GenericPropertyMatchers.contains());
		}
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
	
	
	/**
	 * 商品图片上传，写入磁盘，并保存路径到数据库
	 * @param img
	 * @param id
	 * @param type 图片类型 goodsimg-商品图片，bannerImg-轮播图
	 * @return
	 */
	@Transactional
	public Map<String,Object> writeGoodsUploadFile(List<MultipartFile> imgs,String id,String type){
		Map<String,Object> result = new HashMap<String, Object>();
		try {
			StringJoiner imgpath = new StringJoiner(",");
			for(MultipartFile img : imgs) {
				imgpath.add(writeFile(img,type+File.separator+id));
			}
			//更新商品图片
			if(ConfigConstants.GOODS_IMG.equals(type)) {
				goodRepository.updateGoodsImgWithId(id, imgpath.toString());
			}else {
				goodRepository.updateGoodsBannersWithId(id, imgpath.toString());
			}
			result.put(IConstants.RESP_CODE, RespConstantsUtil.INSTANCE.getDictVal(IConstants.RESPCODE_SUC));
			result.put(IConstants.RESP_MSG, RespConstantsUtil.INSTANCE.getDictVal(IConstants.RESPMSG_SUC));
		} catch (IOException e) {
			log.error(e.getMessage(),e);
			result.put(IConstants.RESP_CODE, RespConstantsUtil.INSTANCE.getDictVal(IConstants.RESPCODE_SYSERR));
			result.put(IConstants.RESP_MSG, RespConstantsUtil.INSTANCE.getDictVal(IConstants.RESPMSG_SYSERR));
		}
		return result;
	}
	
	
	@Transactional
	public void saveGoods(Goods goods)
	{
		goodRepository.save(goods);
	}
	
	@Transactional
	public void deleteGoods(List<String> ids) {
		List<Goods> gs = new ArrayList<Goods>();
		ids.stream().map(s -> {
			Goods g = new Goods();
			g.setId(s);
			gs.add(g);
			return s;
		}).collect(Collectors.toList());
		goodRepository.deleteInBatch(gs);
		
	}
	
	@Transactional
	public void updateGoodsStatus(String id,String status) {
		goodRepository.updateGoodsSetStatusWithId(id, status);
	}
	
	@Transactional
	public void updateBatchGoodsStatus(String id,String status) {
		log.debug("update goods status:"+status+"|ids:"+id);
		goodRepository.updateGoodsSetStatusByIdIn(status,Arrays.asList(id.split((","))));
	}
}
