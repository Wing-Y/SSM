package cn.appsys.controller.developer;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import cn.appsys.pojo.DevUser;
import cn.appsys.service.user.UserService;
import cn.appsys.tools.Constants;

@Controller
@RequestMapping(value="/dev")
public class DevLoginController {

	private final Logger logger = Logger.getLogger(DevLoginController.class);
	@Resource
	private UserService devUserService;
	
	//���뿪���ߵ�¼ҳ��
	@RequestMapping(value="/login")
	public String login(){
		logger.debug("login");
		return "devlogin";
	}
	
	//�����ߵ�¼�ύ����
	@RequestMapping(value="/dologin",method=RequestMethod.POST)
	public String doLogin(@RequestParam String devCode,@RequestParam String devPassword,
			HttpServletRequest request,HttpSession session){
		logger.debug("dologin devCode : " + devCode + " and devPassword : " + devPassword);
		DevUser devUser = devUserService.getDevUserByUserCode(devCode);
		if(null != devUser){
			if(devUser.getDevPassword().equals(devPassword)){
				session.setAttribute(Constants.DEV_USER_SESSION,devUser);
				return "redirect:/dev/flatform/main";
			}else{
				request.setAttribute("error", "���벻��ȷ��");
			}
		}else{
			request.setAttribute("error", "�û������ڣ�");
		}
		return "devlogin";
	}
	
	//�������˺��˳�
	@RequestMapping(value="/logout")
	public String logout(HttpSession session){
		session.removeAttribute(Constants.DEV_USER_SESSION);
		return "devlogin";
	}
}
