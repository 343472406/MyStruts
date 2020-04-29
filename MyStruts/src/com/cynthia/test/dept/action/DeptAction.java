package com.cynthia.test.dept.action;

import com.cynthia.suit.core.basic.Action;
import com.cynthia.suit.core.basic.ActionForm;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


public class DeptAction extends Action {

	public DeptAction() {
		super();
	}

	@Override
	public String execute(HttpServletRequest request, HttpServletResponse response, ActionForm form) throws Exception {
		System.out.println("已进入：DeptAction");
		return "success";
	}

}
