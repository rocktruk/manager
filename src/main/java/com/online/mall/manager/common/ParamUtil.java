package com.online.mall.manager.common;

import java.util.Map;

public class ParamUtil {

	
	public static String validatorColumn(Map<String,Object> req,String[] columns){
		for(int i=0;i<columns.length;i++) {
			if(req.get(columns[i])==null) {
				return RespConstantsUtil.INSTANCE.getDictVal(IConstants.RESPCODE_COLS_VALIDFAIL);
			}
		}
		return RespConstantsUtil.INSTANCE.getDictVal(IConstants.RESPCODE_SUC);
	}
	
}
