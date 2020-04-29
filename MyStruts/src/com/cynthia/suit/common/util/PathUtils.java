package com.cynthia.suit.common.util;

import java.util.ArrayList;
import java.util.List;

public class PathUtils {

	/**
	 * 比较两个字符串之间的区别
	 * 
	 * @param str1 形如/dept/UserAction
	 * @param str2 形如\/*\/*Action *代表一段字符串，取出这部分
	 * @return 返回*所代表的字符串
	 */
	public static List<String> difference(String str1, String str2) {
		List<String> result = new ArrayList<String>();
		String temp = "";
		for (int i = 0, j = 0; i < str1.length() && j < str2.length(); i++, j++) {
			if (str1.charAt(i) == str2.charAt(j)) {
				/* 如果相等且temp不为空（之前存的有不同的值），则作为一个整体加入到result中 */
				if (!"".equals(temp)) {
					result.add(temp);
					temp = "";
				}
			} else {
				/* 上一位是*的话，直到匹配到再下一位 */
				if (j > 0 && str2.charAt(j - 1) == '*') {
					j--;
				}
				/* 如果不相等，储存起来 */
				temp += str1.charAt(i);
			}
		}
		return result;
	}

	/**
	 * 用内容替代{0}的零配置
	 * 
	 * @param path 形如"com.cynthia.{0}.action.{1}Action"
	 * @param list 字符串集合，对应{0}、{1}等等
	 * @return 返回用集合内容替代后的路径
	 */
	public static String replace(String path, List<String> list) {
		for (int i = 0; i < list.size(); i++) {
			path = path.replace("{" + i + "}", list.get(i));
		}
		return path;
	}

}
