package com.cynthia.springMybatis.interceptor;

import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import com.cynthia.springMybatis.pojo.User;

public class CheckLoginIntercpetor implements HandlerInterceptor {
	/*
	 * 在控制器方法执行之前进行拦截的方法
	 * 
	 * 方法返回 boolean
	 * 
	 * true 放行 false 不放行 跳转其他页面
	 * 
	 * 登录检查拦截思路
	 * 
	 * 从Session获取共享登录信息User对象
	 * 
	 * 有：说明已经登录，放行 return true; 没有：说明没有登录 return false;同时跳转到登录页面去
	 * 
	 */
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		// 1.获取Session
		HttpSession session = request.getSession();
		// 2.获取共享的User对象
		User user = (User) session.getAttribute("USER_IN_SESSION");
		System.out.println("拦截器的User ：" + user);
		if (user == null) {
			// 重定向跳转到登录页面
			response.sendRedirect(request.getContextPath() + "/login.jsp");
			return false;
		}

		return true;
	}

	/*
	 * 在控制器方法执行之后执行 做一些收尾工作，记录日志等
	 */
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
			ModelAndView modelAndView) throws Exception {
		// 1.获取Session
		HttpSession session = request.getSession();
		// 2.获取共享的User对象
		User user = (User) session.getAttribute("USER_IN_SESSION");
		String localName = request.getLocalName();
		System.out.println(localName);
		String addr = request.getRemoteAddr();
		String uri = request.getRequestURI();

		System.out.println(user.getName() + ":在 " + new Date().toLocaleString() + ",做了 ：" + uri + "： 操作");

		System.out.println(addr);
	}

	/*
	 * 视图渲染数据之后执行，最终收尾工作 释放资源等等
	 */
	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
			throws Exception {
	}
}
