package com.cynthia.suit.core.ex;

public class ActionException extends RuntimeException {

	private static final long serialVersionUID = 1L;
	
	public ActionException() {
		super();
	}
	
	public ActionException(String msg) {
		System.out.println(msg);
		new RuntimeException(msg);
	}

	public ActionException(Exception e) {
		new RuntimeException(e);
//		System.out.println("getLocalizedMessage:" + e.getLocalizedMessage());//com.cynthia.test.dept.action.aaaAction
//		System.out.println("getMessage:" + e.getMessage());//com.cynthia.test.dept.action.aaaAction
//		System.out.println("getCause:" + e.getCause());//null
//		System.out.println("getClass:" + e.getClass());//class java.lang.ClassNotFoundException
	}

	public void showInfo() {
		System.out.println("ActionException: " + super.getMessage());
	}
	
}
