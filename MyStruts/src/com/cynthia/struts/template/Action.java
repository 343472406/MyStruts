package com.cynthia.struts.template;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public abstract class Action extends HttpServlet {
	private static final long serialVersionUID = 1L;

	public Action() {
		super();
	}

	/**
	 * Servlet的get、post方法调用封装的execute方法
	 * 继承Action的类只需要重写execute即可
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		try {
			execute(request, response);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doGet(request, response);
	}

	public abstract void execute(HttpServletRequest request, HttpServletResponse response) throws Exception;

}
