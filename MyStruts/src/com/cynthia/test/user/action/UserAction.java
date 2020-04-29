package com.cynthia.test.user.action;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.cynthia.suit.core.basic.Action;
import com.cynthia.suit.core.basic.ActionForm;

public class UserAction extends Action {
	public UserAction() {
		super();
	}

	@Override
	public String execute(HttpServletRequest request, HttpServletResponse response, ActionForm form) throws Exception {
		System.out.println("已进入：UserAction");
		return "success";
	}
	
	public String login(HttpServletRequest request, HttpServletResponse response, ActionForm form) {
		System.out.println("已执行：login");
		return "success";
	}

}
