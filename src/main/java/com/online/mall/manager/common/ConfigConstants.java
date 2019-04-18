package com.online.mall.manager.common;

public class ConfigConstants {

	//商品分类
	public static final String GOODS_MENU_NOPARENT = "goods.menu.noparent";
	//订单状态
	public static final String ORDER_STATUS_SUC = "order.status.payed";
	
	public static final String ORDER_STATUS_WAITPAY = "order.status.waitpay";

	public static final String ORDER_STATUS_NOTPAY = "order.status.notpay";
	
	public static final String ORDER_STATUS_FAILED = "order.status.failed";
	
	public static final String ORDER_STATUS_REFUND = "order.status.refund";
	
	public static final String ORDER_STATUS_CANCEL = "order.status.cancel";
	
	//交易状态
	public static final String TRX_STATUS_SUC = "trx.status.suc";
	
	public static final String TRX_STATUS_WAITPAY = "trx.status.waitpay";
	
	public static final String TRX_STATUS_CLOSE = "trx.status.close";
	
	public static final String TRX_STATUS_REVERSE = "trx.status.reverse";
	
	public static final String TRX_STATUS_REFUND = "trx.status.refund";
	
	public static final String TRX_STATUS_INITIAL = "trx.status.initial";
	
	public static final String TRX_STATUS_FAIL = "trx.status.fail";
	
	//待发货
	public static final String DELIVER_STATUS_WAITSEND = "deliver.status.waitsend";
	//待收货
	public static final String DELIVER_STATUS_WAITCOLLECT = "deliver.status.waitcollect";
	//已收货
	public static final String DELIVER_STATUS_COLLECTED = "deliver.status.collected";
	
	public static final String GOODS_KIND = "goodsMenu";
	
	//商品列表展示方式
	public static final String GOODSLS_SORT = "goodsls.sort";
	
	public static final String GOODSLS_SORT_PRICE = "goodsls.sort.price";
	
	public static final String GOODSLS_SORT_ALL = "goodsls.sort.all";
	
	public static final String GOODSLS_SORT_SALES = "goodsls.sort.sales";
	
	//升序降序
	public static final String GOODSLS_SORT_PRICE_ASC = "goodsls.sort.price.asc";
	
	public static final String GOODSLS_SORT_PRICE_DESC = "goodsls.sort.price.desc";
	//商品下架
	public static final String GOODS_STATUS_SOLTOUT = "goods.status.soltout";
	//商品在售
	public static final String GOODS_STATUS_STAYON = "goods.status.stayon";
	//商品待售
	public static final String GOODS_STATUS_TOSALE = "goods.status.tosale";
	
	public static final String PUSHTYPE_FORMDATA = "pushtype.formdata";
	
	public static final String PUSHTYPE_FORMURLENCODED = "pushtype.formurlencoded";
	
	//科匠订单类型
	public static final String ORDRTYPE_NORMAL = "ordrtype.normal";
	
	public static final String ORDRTYPE_CYCLE = "ordrtype.cycle";
	
	public static final String ORDRTYPE_RECYCLE = "ordrtype.recycle";
	
	public static final String ORDRTYPE_PREPAY = "ordrtype.prepay";
	
	public static final String ORDRTYPE_DSCT = "ordrtype.dsct";
	
	public static final String ORDRTYPE_DSCTTIMES = "ordrtype.dscttimes";
	
	//收货地址是否默认
	public static final String RECV_ADDR_DFT = "recv.addr.dft";
	
	public static final String RECV_ADDR_UNDFT = "recv.addr.undft";
	
	//交易码
	public static final String TRXCODE_CONSUME = "trxcode.consume";
	
	public static final String TRXCODE_REFUND = "trxcode.refund";
	
	//商品图标上传标志
	public static final String GOODS_IMG = "goodsimg";
	//商品轮播图片上传标志
	public static final String GOODS_BANNER_IMG = "bannerImg";
	//推荐商品轮播图片上传标志
	public static final String GOODS_RCMND_IMG = "rcmndimg";
	
	public static final String RCMND_BANNER = "rcmnd.banner";
	
	public static final String RCMEN_GOODSLS = "rcmen.goodsls";
	
}


