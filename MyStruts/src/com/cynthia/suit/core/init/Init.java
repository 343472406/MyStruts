package com.cynthia.suit.core.init;

import com.cynthia.suit.core.init.bean.InitForm;
import com.cynthia.suit.core.init.bean.InitForward;
import com.cynthia.suit.core.init.bean.InitParam;
import com.cynthia.suit.support.convert.Convert;
import com.cynthia.suit.core.init.bean.InitMapping;

import org.dom4j.Attribute;
import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import javax.servlet.ServletContext;
import java.io.File;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * 初始化web.xml
 * 获取	<init-param>
 * 			<param-name>config</param-name>
 * 			<param-value>/WEB-INF/struts-config.xml</param-value>
 * 		</init-param>
 * 		记录initParam到内存
 * 
 * 初始化struts-config.xml
 * 获取	<action name="{1}Form" path="\/*\/*Action"
 * 			scope="request" type="com.cynthia.test.{0}.action.{1}Action" >
 * 			<forward name="success" path="/WEB-INF/success.jsp" />
 * 		</action>
 * 		记录mapping到内存
 * 获取	globalForward
 * 		记录forward到内存
 * @author 0000
 *
 */
public class Init {
	/**
	 * dom4j读取xml文件
	 * 将xml进行遍历，把有用数据提取出来
	 * @param path
	 * @param servletContext 
	 * @throws Exception
	 */
	public void init(String path, ServletContext servletContext) throws Exception {
		/* 1.创建Reader对象 */
		SAXReader reader = new SAXReader();
		/* 2.加载xml */
		Document document = reader.read(new File(path));
		/* 3.获取根节点 */
		Element bookStore = document.getRootElement();
		Iterator<Element> it = bookStore.elementIterator();
		while (it.hasNext()) {
			Element element = it.next();
//			System.out.println("=============开始遍历" + element.getName() + "==========");
			Iterator<Element> childNode = element.elementIterator();
			while (childNode.hasNext()) {
				Element child = childNode.next();
				/* 当是web.xml/servlet时 */
				if ("servlet".equals(element.getName())) {
					if ("init-param".equals(child.getName())) {
						initConfigs(child, servletContext);
					}
				}
				/* 当是strutsConfig.xml/for-beans */
				else if ("form-beans".equals(element.getName())) {
					if ("form-bean".equals(child.getName())) {
						initForm(child, servletContext);
					}
				}
				/* 当是strutsConfig.xml/global-forwards */
				else if ("global-forwards".equals(element.getName())) {
					if ("forward".equals(child.getName())) {
						initForward(child, servletContext);
					}
				}
				/* 当是strutsConfig.xml/action-mappings时 */
				else if ("action-mappings".equals(element.getName())) {
					if ("action".equals(child.getName())) {
						initMapping(child, servletContext);
					}
				}
			}
		}
	}
	
	/**
	 * 初始化web.xml/servlet/init-param
	 * @param param		获取到的init-param标签
	 * @param servletContext
	 */
	private void initConfigs(Element param, ServletContext servletContext) {
		Iterator<Element> childNode = param.elementIterator();
		InitParam ip = new InitParam();

		while (childNode.hasNext()) {
			Element child = childNode.next();
			if ("param-name".equals(child.getName())) {
				ip.setParamName(child.getStringValue());
			} else if ("param-value".equals(child.getName())) {
				ip.setParamValue(child.getStringValue());
			}
		}
		
		/* 储存到内存的initParam中 */
		@SuppressWarnings("unchecked")
		List<InitParam> initParams = (List<InitParam>) servletContext.getAttribute("initParam");
		if (initParams == null) {
			initParams = new ArrayList<InitParam>();
		}
		initParams.add(ip);
		servletContext.setAttribute("initParam", initParams);
	}


	/**
	 * 初始化strutsConfig.xml/form-beans/form-bean
	 * @param action
	 * @param servletContext
	 */
	private void initForm(Element form, ServletContext servletContext) {
		InitForm fo = (InitForm) setTemplate(InitForm.class, form);
		
		/* 储存到内存的form中 */
		@SuppressWarnings("unchecked")
		Map<String, String> forms = (Map<String, String>) servletContext.getAttribute("form");
		if (forms == null) {
			forms = new HashMap<String, String>();
		}
		forms.put(fo.getName(), fo.getType());
		servletContext.setAttribute("form", forms);
	}
	
	/**
	 * 初始化strutsConfig.xml/global-forwards/forward
	 * @param forward
	 * @param servletContext
	 */
	private void initForward(Element forward, ServletContext servletContext) {
		InitForward fo = (InitForward) setTemplate(InitForward.class, forward);
		
		/* 储存到内存的forward中 */
		@SuppressWarnings("unchecked")
		Map<String, InitForward> forwards = (Map<String, InitForward>) servletContext.getAttribute("forward");
		if (forwards == null) {
			forwards = new HashMap<String, InitForward>();
		}
		forwards.put(fo.getName(), fo);
		servletContext.setAttribute("forward", forwards);
	}

	/**
	 * 初始化strutsConfig.xml/action-mappings/action 
	 * 储存name、path、type、forward 到mapping中
	 * 
	 * @param action 获取到的<action>标签
	 * @param servletContext 
	 */
	private void initMapping(Element action, ServletContext servletContext) {
		InitMapping ma = (InitMapping) setTemplate(InitMapping.class, action);
		
		Map<String, InitForward> temp = new HashMap<String, InitForward>();
		
		/* 配置局部的forward */
		Iterator<Element> forwards = action.elementIterator();
		while (forwards.hasNext()) {
			Element forward = forwards.next();
			InitForward newForward = new InitForward(forward.attributeValue("name"), forward.attributeValue("path"), false);
			temp.put(forward.attributeValue("name"), newForward);
		}
		ma.setForward(temp);
		
		/* 储存到内存的mapping中 */
		@SuppressWarnings("unchecked")
		Map<String, InitMapping> mappings = (Map<String, InitMapping>) servletContext.getAttribute("mapping");
		if (mappings == null) {
			mappings = new HashMap<String, InitMapping>();
		}
		mappings.put(action.attributeValue("path"), ma);
		servletContext.setAttribute("mapping", mappings);
	}
	
	/**
	 * 为各种模版赋值
	 * 将属性赋值，标签不行。
	 */
	private Object setTemplate(Class<?> clz, Element element) {
		Object obj = null;
		Field[] fields = clz.getDeclaredFields();
		Convert convert = new Convert();
		try {
			/* 实例化模版，赋值 */
			obj = clz.newInstance();
			for (Attribute attr : element.attributes()) {
				for (Field field : fields) {
					if(field.getName().equals(attr.getName())) {
						field.setAccessible(true);
						field.set(obj, convert.typeConvert(field, attr.getValue()));
						field.setAccessible(false);
						break;
					}
				}
			}
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
		return obj;
	}

}
