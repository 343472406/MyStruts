package com.cynthia.suit.core.servlet;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.cynthia.suit.core.ex.ActionException;
import com.cynthia.suit.core.ex.ActionExceptionHandler;
import com.cynthia.suit.common.util.PathUtils;
import com.cynthia.suit.core.basic.Action;
import com.cynthia.suit.core.init.bean.InitParam;
import com.cynthia.suit.core.init.bean.InitForward;
import com.cynthia.suit.core.init.bean.InitMapping;
import com.cynthia.suit.core.basic.ActionForm;
import com.cynthia.suit.core.init.Init;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

/**
 * ActionServlet的作用： 调用Init 拦截请求 如果是未曾实例化，实例化请求的Action 将参数传递给Form处理 调用请求的Action
 * 接收Action返回的跳转 在mapping或者forward中寻找跳转目标，进行跳转 出现异常 捕获，调用ex 跳转error
 * 
 * @author 0000
 *
 */
@WebServlet(value = "/ActionServlet")
public class ActionServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	/*
	 * 一些全局变量
	 * web.xml配置：		(List<InitParam>) getServletContext().getAttribute("initParam")
	 * 未实例化Action配置：	(Map<String, InitMapping>) servletContext.getAttribute("mapping")
	 * 已实例化Action配置：	(Map<String, Action>) getServletContext().getAttribute("actions")
	 * Form配置：			(Map<String, String>) servletContext.getAttribute("form")
	 * Forward配置：		(Map<String, InitForward>) servletContext.getAttribute("forward")
	 */
	
	@Override
	public void init(ServletConfig config) throws ServletException {
		super.init(config);
		// TODO 从配置文件读取内容，并储存到内存
		Init initUtil = new Init();
		try {
			ServletContext servletContext = getServletContext();
			
			/*
			 * 获取xml文件在服务器上的绝对路径
			 * D:\Develop\software\apache-tomcat-9.0.31\webapps\MyStruts\WEB-INF\web.xml
			 */
			String path = getServletContext().getRealPath("WEB-INF/web.xml");

			/* 初始化web.xml 记录initParam 找到其他配置文件 */
			initUtil.init(path, servletContext);
			System.out.println("初始化：initParam:	" + servletContext.getAttribute("initParam"));

			/* 遍历init-config给的配置文件 记录mapping 找到Action的配置 */
			@SuppressWarnings("unchecked")
			List<InitParam> initParams = (List<InitParam>) getServletContext().getAttribute("initParam");
			for (int i = 0; i < initParams.size(); i++) {
				path = getServletContext().getRealPath(initParams.get(i).getParamValue());
				initUtil.init(path, servletContext);
			}
			printContextAttribute();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/* 如果放任doGet默认调用super.doGet()的话，会报错405此URL不支持Http方法GET */
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		doPost(req, resp);
//		req.getRequestURL();	// http://localhost:8080/MyStruts/user/UserAction.do
//		req.getRequestURI();	// /MyStruts/user/UserAction.do
//		req.getServletPath();	// /user/UserAction.do
//		req.getContextPath();	// /MyStruts
//		req.getQueryString();	// name=Marry&pwd=789
//		req.getMethod();		// GET
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		System.out.println("已进入ActionServlet");

		/* 获取内存中 已经实例化的action */
		@SuppressWarnings("unchecked")
		Map<String, Action> actions = (Map<String, Action>) getServletContext().getAttribute("actions");
		if (actions == null) {
			actions = new HashMap<String, Action>();
		}
		/* 获取当前跳转链接，去掉末尾的.do */
		String actionName = req.getServletPath().substring(0, req.getServletPath().lastIndexOf("."));

		try {
			/* 如果请求的Action还没有创建实例，则创建实例 */
			if (actions.get(actionName) == null) {
				instantiateAction(req, resp, actions, actionName);
			}

			/* 实例化指定form */
			ActionForm form = instantiateForm(req, resp);

			/* 调用指定action的execute方法 */
			String forwardName = actions.get(actionName).execute(req, resp, form);
			
			/* 根据action返回，调用跳转 */
			doForward(req, resp, actionName, forwardName);
			
		} catch (Exception e) {
			ActionExceptionHandler aeh = new ActionExceptionHandler();
			aeh.handlerException(e);
		}

	}

	@Override
	public void destroy() {
		// TODO 关闭所有资源，销毁ActionServlet
		super.destroy();
	}

	/**
	 * 实例化Action
	 * 
	 * @param req        servlet的request
	 * @param resp       servlet的response
	 * @param actions    内存中储存已实例化的action用的map
	 * @param actionName 需要实例化的action名
	 * @throws ClassNotFoundException 异常：找不到类
	 * @throws IllegalAccessException 异常：实例化调用构造方法
	 * @throws InstantiationException 异常：实例化
	 * @throws IOException            异常：IO
	 */
	private void instantiateAction(HttpServletRequest req, HttpServletResponse resp, Map<String, Action> actions,
			String actionName)
			throws ClassNotFoundException, InstantiationException, IllegalAccessException, IOException {
		Class<?> clazz = null;
		/* 获取内存中 配置文件中定义的action */
		@SuppressWarnings("unchecked")
		Map<String, InitMapping> mappings = (Map<String, InitMapping>) getServletContext().getAttribute("mapping");

		/* 零配置版创建Action对象 */
		Integer i = 0;
		for (Entry<String, InitMapping> entry : mappings.entrySet()) {
			String key = entry.getKey();
			InitMapping value = entry.getValue();

			String rule = key.replace("*", "(.*?)");
			boolean result = actionName.matches(rule);
			/* 如果符合配置的uri格式 */
			if (result) {
				/* 找出实际path和格式的不同之处，就是需要代替{0}的内容 */
				List<String> difference = PathUtils.difference(actionName, key);
				String fullName = PathUtils.replace(value.getType(), difference);

				clazz = Class.forName(fullName);

				/* 将实例化的action储存到内存中 */
				actions.put(actionName, (Action) clazz.newInstance());
				getServletContext().setAttribute("actions", actions);

				System.out.println("实例化:" + clazz);
				
				/* 新增action时，如果没有对应mapping则增加mapping */
				String formName = null;
				if(value.getName() != null) {
					formName = PathUtils.replace(value.getName(), difference);
				}
				if (mappings.get(actionName) == null) {
					InitMapping newMapping = new InitMapping(formName, actionName, fullName, value.getForward(),
							value.getScope());
					mappings.put(actionName, newMapping);
					req.getServletContext().setAttribute("mapping", mappings);
					System.out.println("新增mapping:" + mappings);
				}
				
				return;
			} else if (i == mappings.size() - 1) {
				/* filter中已经判断过了，应该不会进来 */
				resp.getWriter().write("地址不符合格式");
				resp.getWriter().write("actionName:" + actionName);
			}
			i++;
		}
	}

	/**
	 * 处理每个Form的初始化
	 * 
	 * @param req
	 * @param resp
	 * @return
	 * @throws ClassNotFoundException
	 * @throws IllegalAccessException 
	 * @throws InstantiationException 
	 */
	private ActionForm instantiateForm(HttpServletRequest req, HttpServletResponse resp) throws ClassNotFoundException, InstantiationException, IllegalAccessException {
		@SuppressWarnings("unchecked")
		Map<String, InitMapping> mappings = (Map<String, InitMapping>) req.getServletContext().getAttribute("mapping");
		@SuppressWarnings("unchecked")
		Map<String, String> forms = (Map<String, String>) req.getServletContext().getAttribute("form");
		
		/* 获取当前跳转链接，去掉末尾的.do */
		String actionName = req.getServletPath().substring(0, req.getServletPath().lastIndexOf("."));

		/* 实例化一个需要的form,没有的话实例化ActionForm */
		String formName = mappings.get(actionName).getName();
		ActionForm form = null;
		if(formName == null) {
			form = new ActionForm();
		} else if(forms.get(formName) == null) {
			throw new ActionException("警告：没有配置该表：" + formName);
		} else {
			form = ((ActionForm) Class.forName(forms.get(formName)).newInstance()).init(req);
			System.out.println("实例化：" + forms.get(formName));
		}
		return form;
	}
	
	/**
	 * 跳转forward
	 * 先从mapping中寻找forward	
	 * 没有再从全局的forward中寻找
	 * 
	 * @param req
	 * @param resp
	 * @param forwardName
	 * @throws ServletException
	 * @throws IOException
	 */
	private void doForward(HttpServletRequest req, HttpServletResponse resp, String actionName, String forwardName) throws ServletException, IOException {
		Map<String, InitForward> forwardList = new HashMap<String, InitForward>();
		@SuppressWarnings("unchecked")
		InitMapping mapping = ((Map<String, InitMapping>) getServletContext().getAttribute("mapping")).get(actionName);
		@SuppressWarnings("unchecked")
		Map<String, InitForward> forwards = (Map<String, InitForward>) getServletContext().getAttribute("forward");

		/* 从mapping中寻找forward */
		if(mapping.getForward() != null) {
			forwardList.putAll(mapping.getForward());
		} 
		/* 从全局forward中寻找forward */
		forwardList.putAll(forwards);
		System.out.println(forwardList);
		InitForward forward = forwardList.get(forwardName);
		if (forward != null) {
			if (false == forward.getRedirect()) {
				req.getRequestDispatcher(forward.getPath()).forward(req, resp);
			} else if (true == forward.getRedirect()) {
				resp.sendRedirect(forward.getPath());
			}
		}
	}
	
	/**
	 * 打印几个全局属性
	 */
	private void printContextAttribute() {
		System.out.println("初始化：form:	" + getServletContext().getAttribute("form"));
		System.out.println("初始化：forward:	" + getServletContext().getAttribute("forward"));
		System.out.println("初始化：mapping:	" + getServletContext().getAttribute("mapping"));
	}
	
}
