package com.mooc.house.biz.service;

import java.util.concurrent.TimeUnit;

import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.RemovalListener;
import com.google.common.cache.RemovalNotification;
import com.mooc.house.biz.mapper.UserMapper;
import com.mooc.house.common.model.User;


@Service
public class MailService {
	
	//最大空间100 过期时间15分钟，如果过期要把数据库中记录删除掉
		private final Cache<String,String> registerCache = CacheBuilder.newBuilder()
				.maximumSize(100).expireAfterAccess(15, TimeUnit.MINUTES)
				.removalListener(new RemovalListener<String,String>() {
					@Override
					public void onRemoval(RemovalNotification<String,String> notification) {
						//userMapper.delete(notification.getValue());
					}
			
		}).build();
		
	@Value("${domain.name}")
	private String domain;
	
	@Autowired
	private UserMapper userMapper;
	
	@Autowired
	private JavaMailSender mailSender;
	
	@Value("${spring.mail.username}")
	private String from ;
	
	public void sendMail(String string, String url, String email) {
		SimpleMailMessage message = new SimpleMailMessage();
		message.setFrom(from);
		message.setTo(email);
		message.setText(url);
		mailSender.send(message);
	}
	
	/**
	 * 1缓存key-email
	 * 2借助spring mail发送邮件
	 * 3借助异步框架进行异步操作
	 * 4调用方和异步调用的方法不能在同一个类
	 * @param email
	 */
	@Async
	public void registerNotify(String email) {
		//生成10位长度字符串
		String randomKey = RandomStringUtils.randomAlphabetic(10);
		//将email与key绑定
		registerCache.put(randomKey, email);
		//创建链接
		String url = "http://"+domain+"/accounts/verify?key="+randomKey;
		sendMail("房产平台激活邮件",url,email);
	}
	
	public boolean enable(String key) {
	    String email = registerCache.getIfPresent(key);
	    if (StringUtils.isBlank(email)) {
	      return false;
	    }
	    User updateUser = new User();
	    updateUser.setEmail(email);
	    updateUser.setEnable(1);
	    userMapper.update(updateUser);
	    registerCache.invalidate(key);
	    return true;
	  }
	
}
