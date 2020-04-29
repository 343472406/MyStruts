package com.cynthia.demo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class ComponentHelloController {

	/*
	 * @RequestMapping	请求映射注解
	 * 
	 * 值就是浏览器访问此方法的资源名称：	/method1
	 */
	 @RequestMapping("/method1") 
	public ModelAndView method1() {
		ModelAndView mv = new ModelAndView();
		mv.addObject("username", "小明");
		mv.setViewName("/WEB-INF/view/hello.jsp");
		return mv;
	}

}
