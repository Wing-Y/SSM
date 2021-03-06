package cn.appsys.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;
import cn.appsys.pojo.DevUser;
import cn.appsys.tools.Constants;

public class PreInterceptor extends HandlerInterceptorAdapter{

	private Logger logger = Logger.getLogger(SysInterceptor.class);
	public boolean preHandle(HttpServletRequest request,HttpServletResponse response,Object handler) throws Exception{
		logger.debug("=================================��������PreInterceptor preHandle!");
		HttpSession session = request.getSession();
		DevUser user = (DevUser)session.getAttribute(Constants.DEV_USER_SESSION);
		if(null == user){
			response.sendRedirect(request.getContextPath() + "/403.jsp");
			return false;
		}
		return true;
	}
}
