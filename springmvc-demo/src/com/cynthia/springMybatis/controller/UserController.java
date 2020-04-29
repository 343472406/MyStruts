package com.cynthia.springMybatis.controller;

import java.util.List;

import javax.servlet.http.HttpSession;

import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.cynthia.springMybatis.pojo.User;
import com.cynthia.springMybatis.service.UserService;

/*
 * 
 * @Scope
 * 	singleton 单例-默认
 *  prototype 多例，使用一次创建一个对象
 *  request 一次请求创建一个对象
 *  session 一次会话创建一个对象
 *  
 *  早期的Struts2表现层框架一定要 是 request，或者protytype
 *  因为 Struts2 使用的是成员变量接受的参数
 *  如果对象控制器对象是单例，对应的接受参数的成员变量在内存中也只有一份，如果并发访问，会出现线程安全问题
 *  
 *  如果控制器中有成员变量可能需要并发修改的情况，一定要设置成 request，或者protytype
 *  
 *  SpringMVC使用局部方法参数接受请求参数，不会存在线程安全问题，默认单例即可
 *  
 */

@Controller
@RequestMapping("/user")
public class UserController {

	@Autowired
	private UserService userService;

	@RequestMapping("/list")
	public String list(Model m) {

		List<User> users = userService.selectList();
		m.addAttribute("users", users);

		return "userList";
	}

	@RequestMapping("/delete")
	public String delete(Integer userId) {

		userService.deleteByPrimaryKey(userId);

		// 删除成功以后重定向跳转到列表
		return "redirect:/user/list.do";
	}

	@RequestMapping("/login")
	public String login(String name, String password, Model m, HttpSession session) {

		User user = userService.selectNameAndPassword(name, password);
		// 如果User对象不等于空，说明用户存在
		if (user == null) {
			m.addAttribute("errorMsg", "亲，账号密码错误！");
			return "forward:/login.jsp";
		}

		// 将数据共享到Session中
		session.setAttribute("USER_IN_SESSION", user);

		// 登录成功以后重定向跳转到列表
		return "redirect:/user/list.do";
	}
}
