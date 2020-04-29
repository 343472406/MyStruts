package com.cynthia.suit.common.filter;

import java.io.IOException;
import java.util.Map;
import java.util.Map.Entry;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;

import com.cynthia.suit.core.ex.ActionException;
import com.cynthia.suit.core.init.bean.InitMapping;

/**
 * 处理乱码
 * 
 * 判断请求地址是否符合mapping
 *     符合调用ActionServlet
 *     不符合调用error
 * @author 0000
 *
 */
@WebFilter("*.do")
public class ActionFilter implements Filter {

	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
		// TODO 拦截器初始化
		Filter.super.init(filterConfig);
		System.out.println("Filter init");
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		// TODO 执行拦截器
		System.out.println("已进入Filter");
		
		/* 设置编码，防止乱码 */
		response.setContentType("text/html;charset=utf-8");
		request.setCharacterEncoding("UTF-8");
		
		/* 条件都符合才放行 */
		if(checkUri(request, response)) {
			/* 放行 */
			chain.doFilter(request, response);
		} else {
			throw new ActionException("请求不符合规则。");
		}

	}

	@Override
	public void destroy() {
		// TODO 销毁拦截器
		Filter.super.destroy();
		System.out.println("Filter destory");
	}
	
	/**
	 * 检查URI是否符合struts-config.xml中配置的
	 * @param request
	 * @param response
	 * @return boolean		返回是否符合
	 */
	public boolean checkUri(ServletRequest request, ServletResponse response) {
		/* 获取内存中 配置文件中定义的action */
		@SuppressWarnings("unchecked")
		Map<String, InitMapping> mapping = (Map<String, InitMapping>)request.getServletContext().getAttribute("mapping");

		/* 获取请求的action名 */
		String actionName = ((HttpServletRequest) request).getServletPath().substring(0, ((HttpServletRequest) request).getServletPath().lastIndexOf("."));
		
		/* 零配置版创建Action对象 */
		Integer i = 0;
		for (Entry<String, InitMapping> entry : mapping.entrySet()) {
			String key = entry.getKey();

			String rule = key.replace("*", "(.*?)");
			boolean result = actionName.matches(rule);
			/* 如果符合配置的uri格式 */ 
			if (result) {
				return true;
			}
			i++;
		}
		return false;
	}

}
