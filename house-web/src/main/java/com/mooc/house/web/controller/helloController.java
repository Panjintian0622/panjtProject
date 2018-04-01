package com.mooc.house.web.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import com.mooc.house.biz.service.UserService;
import com.mooc.house.common.model.User;

@Controller
public class helloController {
	
	@Autowired
	private UserService userService;
	@RequestMapping("hello")
	public String hello(ModelMap model) {
		List<User> users = userService.getUsers();
		User one = users.get(0);
		/*if(one != null ){
			throw new IllegalArgumentException();
		}*/
		model.put("user",one);
		return "hello";
	}
	
	@RequestMapping("index")
	public String index() {
		return "homepage/index";
	}
}
