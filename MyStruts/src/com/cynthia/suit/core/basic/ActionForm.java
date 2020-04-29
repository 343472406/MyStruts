package com.cynthia.suit.core.basic;

import java.lang.reflect.Field;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import com.cynthia.suit.support.convert.Convert;

/**
 * 提供空构造
 * 将所有数据储存起来,并提供获取方法
 * 将参数传入子类
 * 
 * @author 0000
 *
 */
public class ActionForm {
	
	protected Map<String, String[]> map;

	public ActionForm() {
		super();
	}
	
	/**
	 * 提供map参数的获取方法
	 * @param tagName	要获取的参数名
	 * @return
	 */
	public Object get(String tagName) {
		return this.map.get(tagName)[0];
	}
	
	public Map<String, String[]> getMap() {
		return map;
	}

	public void setMap(Map<String, String[]> map) {
		this.map = map;
	}

	/* 将请求的参数对应到form类中 */
	public ActionForm init(HttpServletRequest req) {
		
		
		/* 为actionForm赋值 */
		ActionForm actionForm = null;
		try {
			Class<?> cls = Class.forName("com.cynthia.test.user.bean.User");
			/* 实例化模版 */
			actionForm = (ActionForm) cls.newInstance();
			
			/* 将map储存到form中 */
			Map<String, String[]> map = req.getParameterMap();
			actionForm.setMap(map);
			
			/* 反射赋值，将map中和bean中对应数据赋值到对象form中 */
			Set<String> names = map.keySet();
			Field[] fields = cls.getDeclaredFields();
			Convert convert = new Convert();
			for (Field field : fields) {
				String fieldName = field.getName();
				for (String name : names) {
					if (fieldName.equals(name)) {
						field.setAccessible(true);
						field.set(actionForm, convert.typeConvert(field, req.getParameter(name)));
						field.setAccessible(false);
						break;
					}
				}
			}
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		}
		return actionForm;
	}

}
