package com.online.mall.manager.control;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.online.mall.manager.common.IConstants;
import com.online.mall.manager.common.RespConstantsUtil;
import com.online.mall.manager.common.SessionUtil;
import com.online.mall.manager.common.SignatureUtil;
import com.online.mall.manager.entity.MerAdmin;
import com.online.mall.manager.service.MerAdminService;

@Controller
public class UserControl {

	private static final Logger log = LoggerFactory.getLogger(UserControl.class);
	
	@Autowired
	private MerAdminService merService;
	
	@RequestMapping("/")
	public String index() {
		return "redirect:login";
	}
	
	@RequestMapping("login")
	public String login(HttpServletRequest request) {
		request.setAttribute("pubkey", SignatureUtil.INTANCE.getPublicKey());
		return "login";
	}
	
	@RequestMapping("logout")
	public String logout(HttpSession session) {
		SessionUtil.setAttribute(session, SessionUtil.USER, null);
		return "redirect:login";
	}
	
	
	@RequestMapping("authToken")
	@ResponseBody
	public Map<String,Object> login(HttpServletRequest request,@RequestParam(value="acctNo") String acctNo,@RequestParam(value="pwd") String pwd) {
		Map<String,Object> result = new HashMap<String, Object>();
		try {
			String password = pwd.replaceAll("%2B", "+");
			pwd = SignatureUtil.INTANCE.dencryptByPrivateKey(password);
			Optional<MerAdmin> mer = merService.findMerByAcctNo(acctNo);
			if(mer.isPresent()) {
				if(mer.get().getPwd().equals(SignatureUtil.INTANCE.md5(acctNo+pwd))) {
					SessionUtil.setAttribute(request.getSession(), SessionUtil.USER, mer.get());
					result.put(IConstants.RESP_CODE, RespConstantsUtil.INSTANCE.getDictVal(IConstants.RESPCODE_SUC));
					result.put(IConstants.RESP_MSG, RespConstantsUtil.INSTANCE.getDictVal(IConstants.RESPMSG_SUC));
					return result;
				}
			}
			result.put(IConstants.RESP_CODE, RespConstantsUtil.INSTANCE.getDictVal(IConstants.RESPCODE_LOGIN_FAIL));
			result.put(IConstants.RESP_MSG, RespConstantsUtil.INSTANCE.getDictVal(IConstants.RESPMSG_LOGIN_FAIL));
		}catch(Exception e) {
			log.error(e.getMessage(),e);
			result.put(IConstants.RESP_CODE, RespConstantsUtil.INSTANCE.getDictVal(IConstants.RESPCODE_SYSERR));
			result.put(IConstants.RESP_MSG, RespConstantsUtil.INSTANCE.getDictVal(IConstants.RESPMSG_SYSERR));
		}
		return result;
	}
	
	
	
}
