package com.cynthia.struts.action;

import com.cynthia.struts.bean.InitParam;
import com.cynthia.struts.bean.MappingAction;
import com.cynthia.struts.template.Action;
import org.dom4j.Attribute;
import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.Map.Entry;

//@WebServlet(value = "/ActionServlet")
public class ActionServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/* 储存init-config */
	protected List<InitParam> initParam = new ArrayList();
	/* 储存mapping */
	protected Map<String, MappingAction> mapping = new HashMap<String, MappingAction>();
	/* 储存forward */
	protected Map<String, Object[]> forward = null;
	/* 储存Action */
	protected Map<String, Action> actions = new HashMap<String, Action>();

	public ActionServlet() {
		super();
	}

	/* 初始化 */
	@Override
	public void init() {//ServletConfig config
		System.out.println("init");

		try {
			/* tomcat初始化时是null，第一次调用servlet时能获取到"/WEB-INF/struts-config.xml" */
			/* System.out.println("getInitParameter:" + this.getInitParameter("config")); */

			/*
			 * 获取xml文件在服务器上的绝对路径
			 * D:\Develop\software\apache-tomcat-9.0.31\webapps\MyStruts\WEB-INF\web.xml
			 */
			 String path = getServletContext().getRealPath("WEB-INF/web.xml"); 

			/*
			 * this.getClass().getClassLoader().getResource("");
			 * this.getClass().getClassLoader().getResourceAsStream(""); "d:/a.jar!/"
			 */
			/* 遍历web.xml 记录initParam 找到其他配置文件 */
			
			init(path);

			/* 遍历init-config 记录mapping 找到Action的配置 */
			for (int i = 0; i < this.initParam.size(); i++) {
				path = getServletContext().getRealPath(this.initParam.get(i).getParamValue());
				init(path);
			}

			/* 创建Action对象 */
			/*
			 * Class clazz = null; for(Entry<String, MappingAction> entry :
			 * this.mapping.entrySet()){ String key = entry.getKey(); MappingAction value =
			 * entry.getValue();
			 * 
			 * clazz = Class.forName(value.getType()); actions.put(key,
			 * (Action)clazz.newInstance()); System.out.println("Instance:" + clazz); }
			 */

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		response.setContentType("text/html;charset=utf-8");

//		request.getRequestURL();// http://localhost:8080/MyStruts/ActionServlet.do
//		request.getRequestURI();// /MyStruts/ActionServlet.do
//		request.getServletPath();// /ActionServlet.do
//		request.getContextPath();// /MyStruts
//		request.getQueryString();// action=login&name=Marry&pwd=789
//		request.getMethod();// GET

		System.out.println("已进入ActionServlet");

		System.out.println("initParam:" + this.initParam);
		System.out.println("mapping:" + this.mapping);
		System.out.println("forward:" + this.forward);
		System.out.println("actions:" + this.actions);
		System.out.println("---------------------------");

		/* 获取当前跳转链接，去掉末尾的.do */
		String actionName = request.getServletPath().substring(0, request.getServletPath().lastIndexOf("."));

		try {
			/* 配置占位符 */
			/* 如果action还没有创建实例 */
			if (actions.get(actionName) == null) {
				/* 创建Action对象 */
				Class clazz = null;

				/*
				 * for (Entry<String, MappingAction> entry : this.mapping.entrySet()) { String
				 * key = entry.getKey(); MappingAction value = entry.getValue();
				 * 
				 * clazz = Class.forName(value.getType()); actions.put(key, (Action)
				 * clazz.newInstance()); System.out.println("Instance:" + clazz); }
				 */

				/* 零配置版 */
				for (Entry<String, MappingAction> entry : this.mapping.entrySet()) {
					String key = entry.getKey();
					MappingAction value = entry.getValue();

					String rule = key.replace("*", "(.*?)");
					boolean result = actionName.matches(rule);
					/* 如果符合格式 */ 
					if (result) {
						/* 找出实际path和格式的不同之处，就是需要代替{0}的内容 */
						List<String> difference = this.difference(actionName, key);
						String packName = this.replace(value.getType(), difference);
						
						clazz = Class.forName(packName);
						actions.put(actionName, (Action) clazz.newInstance());
						System.out.println("Instance:" + clazz);
						
						actions.get(actionName).execute(request, response);
					} else {
						response.getWriter().write("地址不符合格式");
						response.getWriter().write("actionName:" + actionName);
					}

				}

			} else {
				actions.get(actionName).execute(request, response);
			}

		} catch (

		Exception e) {
			e.printStackTrace();
		}

	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doGet(request, response);
	}

	private void init(String path) throws Exception {
		/* String path = contextPath.getRealPath("WEB-INF/web.xml"); */

		/* dom4j读取xml文件 */

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
			List<Attribute> bookAttrs = element.attributes();
			for (Attribute attr : bookAttrs) {
//				System.out.println("属性名：" + attr.getName() + "-----属性值：" + attr.getValue());
			}
			Iterator<Element> childNode = element.elementIterator();
			while (childNode.hasNext()) {
				Element child = childNode.next();
//				System.out.println("节点名：" + child.getName() + "-----节点值：" + child.getStringValue());

				/* 当是action-mapping中的action时 */
				if ("init-param".equals(child.getName())) {
					initConfigs(child);
				}

				if ("action".equals(child.getName())) {
					initAction(child);
				}
			}
//			System.out.println("=============结束遍历" + element.getName() + "==========");
		}
	}

	/**
	 * 初始化web.xml servlet init-param
	 * 
	 * @param action 获取到的<action>标签
	 */
	private void initConfigs(Element param) {
		Iterator<Element> childNode = param.elementIterator();
		String paramName;
		String paramValue;
		InitParam ip = new InitParam();

		while (childNode.hasNext()) {
			Element child = childNode.next();
			if ("param-name".equals(child.getName())) {
				ip.setParamName(child.getStringValue());
			} else if ("param-value".equals(child.getName())) {
				ip.setParamValue(child.getStringValue());
			}
		}
		/* 添加一条新的init-param */
		this.initParam.add(ip);
		System.out.println("initParam:" + this.initParam);
	}

	/**
	 * 初始化struts-config action-mappings action 储存name、path、type、forward
	 * 
	 * @param action 获取到的<action>标签
	 */
	private void initAction(Element action) {

		MappingAction ma = new MappingAction();
		ma.setName(action.attributeValue("name"));
		ma.setPath(action.attributeValue("path"));
		ma.setType(action.attributeValue("type"));

		Map<String, String> temp = new HashMap<String, String>();

		Iterator<Element> forwards = action.elementIterator();
		while (forwards.hasNext()) {
			Element forward = forwards.next();
			temp.put(forward.attributeValue("name"), forward.attributeValue("path"));
		}
		ma.setForward(temp);
		this.mapping.put(action.attributeValue("path"), ma);
		System.out.println("initAction---mapping:" + this.mapping);
	}

	/**
	 * 比较两个字符串之间的区别
	 * 
	 * @param str1 形如/Dept/UserAction
	 * @param str2 形如\/*\/*Action *代表一段字符串，取出这部分
	 * @return 返回*所代表的字符串
	 */
	public List<String> difference(String str1, String str2) {
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
	 * @return 返回用集合内容替代过的路径
	 */
	public String replace(String path, List<String> list) {
		for (int i = 0; i < list.size(); i++) {
			path = path.replace("{" + i + "}", list.get(i));
		}
		return path;
	}
}
