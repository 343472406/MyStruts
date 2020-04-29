package com.cynthia.suit.core.basic;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 规定子类必须重写 execute 方法，方便调用。
 * @author 0000
 *
 */
public abstract class Action {

	/**
	 * 提供给反射，进行实例化
	 */
	public Action() {
		super();
	}

	/* 提供给使用者，实际执行操作用的 */
	public abstract String execute(HttpServletRequest request, HttpServletResponse response, ActionForm actionForm) throws Exception;

}
