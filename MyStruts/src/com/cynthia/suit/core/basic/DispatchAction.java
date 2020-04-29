package com.cynthia.suit.core.basic;

import java.lang.reflect.Method;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 在execute方法中，获取参数method，根据参数执行指定的方法。
 * 如果没有该参数，执行 unspecifica 方法。
 * @author 0000
 *
 */
public class DispatchAction extends Action {

	/**
	 * 提供给反射，进行实例化
	 */
	public DispatchAction() {
		super();
	}
	
	/**
	 * 如果有给到参数method，则执行给出的方法名。
	 * 若没有给出，执行unspecifica方法。
	 * @return	返回调用param
	 */
	@Override
	public String execute(HttpServletRequest request, HttpServletResponse response, ActionForm form) throws Exception {
		/* 获取method参数 */
		String methodName = request.getParameter("method");
		if (methodName == null) {
			unspecifica(request, response, form);
			return "error";
		} else {
			/* 获取当前跳转链接，去掉末尾的.do */
			String actionName = request.getServletPath().substring(0, request.getServletPath().lastIndexOf("."));
			@SuppressWarnings("unchecked")
			DispatchAction da = (DispatchAction) ((Map<String, Action>) request.getServletContext()
					.getAttribute("actions")).get(actionName);

			/* 获取并执行method给出的方法名，传参也必须为HttpServletRequest，HttpServletResponse */
			Method method = da.getClass().getMethod(methodName, HttpServletRequest.class, HttpServletResponse.class, ActionForm.class);
			method.invoke(da, request, response, form);
		}
		return "success";
	}

	/**
	 * 没有method参数时的默认方法，子类可重写
	 * @param request
	 * @param response
	 * @return	返回对应param的值
	 */
	protected String unspecifica(HttpServletRequest request, HttpServletResponse response, ActionForm form) {
		System.out.println("未给出默认方法");
		return "error";
	}
}
