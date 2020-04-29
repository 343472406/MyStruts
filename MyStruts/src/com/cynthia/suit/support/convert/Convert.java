package com.cynthia.suit.support.convert;

import java.lang.reflect.Field;
import java.text.ParseException;
import java.text.SimpleDateFormat;

public class Convert {

	/**
	 * 由给出类型转换当前String为对应类型
	 * @param field		需要转换的字段
	 * @param value		需要转换的值
	 * @return			返回转换后对应类型的值
	 */
	public Object typeConvert(Field field, String value) {
		if (field.getType().getName().equals("int") || field.getType().getName().equals("java.lang.Integer")) {
			/* int类型 */
			return Integer.parseInt(value);
		} else if (field.getType().getName().equals("java.lang.String")) {
			/* String类型 */
			return value;
		} else if (field.getType().getName().equals("java.sql.Date")) {
			/* 时间类型 */
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			try {
				return sdf.parse(value);
			} catch (ParseException e) {
				e.printStackTrace();
			}
		} else if(field.getType().getName().equals("boolean")) {
			return Boolean.valueOf(value);
		} else {
			System.out.println("数据类型未匹配");
		}
		return null;
	}
	
}
