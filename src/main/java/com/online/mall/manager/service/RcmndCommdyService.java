package com.online.mall.manager.service;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.StringJoiner;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.online.mall.manager.common.ConfigConstants;
import com.online.mall.manager.common.IConstants;
import com.online.mall.manager.common.RespConstantsUtil;
import com.online.mall.manager.entity.GoodsWithoutDetail;
import com.online.mall.manager.entity.RecommendCommodities;
import com.online.mall.manager.repository.RcmndCommdyRepository;

@Service
public class RcmndCommdyService extends AbstractMallService {

	private  static final Logger log = LoggerFactory.getLogger(RcmndCommdyService.class);
	
	@Autowired
	private RcmndCommdyRepository rcmndRepository;
	
	
	public List<String> hotSale(){
		return rcmndRepository.findAllHotSale();
	}
	
	
	public List<String> getRcmndCommdy(){
		return rcmndRepository.findAllRcmnd();
	}
	
	/**
	 * 查询所有首页轮播热销商品
	 * @return
	 */
	public List<Object[]> getAllHotRcmndCommdities(){
		return rcmndRepository.findAllComboIdWithHotSale();
	}
	
	/**
	 * 设置推荐商品
	 * @param rcmmdCommdy
	 * @param type
	 */
	@Transactional
	public void addRcmnd(List<RecommendCommodities> rcmndCommdy,List<String> goods,List<String> goodsIds) {
		List<RecommendCommodities> saveRcmnd = rcmndCommdy.stream().filter(h -> !goods.contains(h.getGoods().getId())).collect(Collectors.toList());
		List<String> delRcmnd = goods.stream().filter(g -> !goodsIds.contains(g)).collect(Collectors.toList());
		if(delRcmnd.size()>0) {
			rcmndRepository.delRecommendCommoditiesByGoodsId(delRcmnd);
		}
		if(!saveRcmnd.isEmpty()) {
			save(saveRcmnd);
		}
	}
	
	@Transactional
	public void delRcmndWhichSaleOut(List<String> goodsLs) {
		rcmndRepository.delRecommendCommoditiesByGoodsId(goodsLs);
	}
	
	
	public void save(List<RecommendCommodities> rcmmdCommdy) {
		rcmndRepository.saveAll(rcmmdCommdy);
	}
	
	/**
	 * 上传推荐商品轮播图
	 * @param imgs
	 * @param id
	 * @param type
	 * @return
	 */
	@Transactional
	public Map<String,Object> writeRcmndUploadFile(List<MultipartFile> imgs,String id,String type){
		Map<String,Object> result = new HashMap<String, Object>();
		try {
			for(MultipartFile img : imgs) {
				String path = writeFile(img,type+File.separator+id);
				rcmndRepository.updateRecommendCommoditiesSetRcmndImgById(path,id);
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
	
}
