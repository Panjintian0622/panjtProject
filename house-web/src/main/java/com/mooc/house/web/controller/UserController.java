package com.mooc.house.web.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.mooc.house.biz.service.UserService;
import com.mooc.house.common.model.User;
import com.mooc.house.common.result.ResultMsg;

@RestController
public class UserController {
	@Autowired
	private UserService userService;
	 /**
	   * 注册提交:1.注册验证 2 发送邮件 3验证失败重定向到注册页面 注册页获取:根据account对象为依据判断是否注册页获取请求
	   * 
	   * @param account
	   * @param modelMap
	   * @return
	   */
	  @RequestMapping("accounts/register")
	  public String accountsRegister(User account, ModelMap modelMap) {
	    if (account == null || account.getName() == null) {
	     // modelMap.put("agencyList",  agencyService.getAllAgency());
	      return "/user/accounts/register";
	    }
	    // 用户验证
	    ResultMsg resultMsg = UserHelper.validate(account);
	    if (resultMsg.isSuccess() && userService.addAccount(account)) {
	      modelMap.put("email", account.getEmail());
	      return "/user/accounts/registerSubmit";
	    } else {
	      return "redirect:/accounts/register?" + resultMsg.asUrlParams();
	    }
	  }

}
