package com.mooc.house.biz.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.alibaba.druid.filter.Filter;
import com.alibaba.druid.filter.stat.StatFilter;
import com.alibaba.druid.pool.DruidDataSource;
import com.alibaba.druid.support.http.StatViewServlet;
import com.google.common.collect.Lists;

@Configuration
public class DruidConfig {
	//将bean方法里的返回对象，与外部的配置文件绑定
	@ConfigurationProperties(prefix="spring.druid")
	@Bean(initMethod="init",destroyMethod="close")
	public DruidDataSource dataSource() {
		DruidDataSource druidDataSource = new DruidDataSource();
		druidDataSource.setProxyFilters(Lists.newArrayList(statFilter()));
		return druidDataSource;
	}
	@Bean
	public Filter statFilter() {
		StatFilter statFilter =  new StatFilter();
		statFilter.setSlowSqlMillis(1);
		statFilter.setLogSlowSql(true);
		//是否将日志合并
		statFilter.setMergeSql(true);
		return statFilter;
	}
	//添加监控
	@Bean
	public ServletRegistrationBean servletRegistrationBean() {
		return new ServletRegistrationBean(new StatViewServlet(),"/druid/*");
		
	}
}
