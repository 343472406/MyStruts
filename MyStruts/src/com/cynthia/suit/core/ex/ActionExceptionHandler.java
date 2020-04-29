package com.cynthia.suit.core.ex;

public class ActionExceptionHandler {

	public void handlerException(Exception e) {
		String stackTrace = e.getStackTrace().length > 0 ? e.getStackTrace()[0].toString() : "";
		String errorMsg = "系统未捕获的异常handlerException：error:" + e.toString() + "\n" + "stackTrace:" + stackTrace;
		System.out.println(errorMsg);
		e.printStackTrace();
	}
}
