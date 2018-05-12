package cn.appsys.controller.backend;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import cn.appsys.pojo.BackendUser;
import cn.appsys.service.user.UserService;
import cn.appsys.tools.Constants;

@Controller
@RequestMapping(value="/backend")
public class BackendLoginController {

	private final Logger logger = Logger.getLogger(BackendLoginController.class);
	@Resource
	private UserService userService;
	
	//����ϵͳ��¼ҳ��
	@RequestMapping(value="/login")
	public String login(){
		logger.debug("login");
		return "backendlogin";
	}
	
	//ϵͳ��¼�ύ����
	@RequestMapping(value="/dologin",method=RequestMethod.POST)
	public String doLogin(@RequestParam String userCode,@RequestParam String userPassword,
			HttpServletRequest request,HttpSession session){
		logger.debug("dologin userCode : " + userCode + " and userPassword : " + userPassword);
		BackendUser backendUser = userService.getBackendUserByUserCode(userCode);
		if(null != backendUser){
			if(backendUser.getUserPassword().equals(userPassword)){
				session.setAttribute(Constants.USER_SESSION,backendUser);
				return "redirect:/backend/sys/main";
			}else{
				request.setAttribute("error", "���벻��ȷ��");
			}
		}else{
			request.setAttribute("error", "�û������ڣ�");
		}
		return "backendlogin";
	}
	
	//ϵͳ�˺��˳�
	@RequestMapping(value="/logout")
	public String logout(HttpSession session){
		session.removeAttribute(Constants.USER_SESSION);
		return "backendlogin";
	}
}
