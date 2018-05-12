package cn.appsys.controller.backend;

import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import cn.appsys.tools.Constants;

@Controller
@RequestMapping(value="/backend/sys")
public class BackendMainController {

	//��¼�ɹ���ת��ҳ��֤
	@RequestMapping(value="/main")
	public String main(HttpSession session){
		if(session.getAttribute(Constants.USER_SESSION) == null){
			return "redirect:/backend/login";
		}
		return "backend/main";
	}
}
