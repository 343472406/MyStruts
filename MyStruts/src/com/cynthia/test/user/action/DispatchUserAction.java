package com.cynthia.test.user.action;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.cynthia.suit.core.basic.DispatchAction;
import com.cynthia.test.user.bean.User;
import com.cynthia.suit.core.basic.ActionForm;

public class DispatchUserAction extends DispatchAction {
	public DispatchUserAction() {
		super();
	}
	
	public String login(HttpServletRequest request, HttpServletResponse response, ActionForm form) {
		System.out.println("已进入DispatchUserAction，执行login。");
		System.out.println(form);
		return "success";
	}

	@Override
	protected String unspecifica(HttpServletRequest request, HttpServletResponse response, ActionForm form) {
		System.out.println("已进入DispatchUserAction，执行unsepcifica，没有给出method。");
		System.out.println(form);
		return "error";
	}
	
}
