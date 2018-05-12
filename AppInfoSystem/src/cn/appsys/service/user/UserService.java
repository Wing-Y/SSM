package cn.appsys.service.user;

import cn.appsys.pojo.BackendUser;
import cn.appsys.pojo.DevUser;

public interface UserService {

	/**
	 * ͨ��DevUser��ѯ�û���Ϣ
	 * 
	 * @param userCode
	 * @return
	 * @throws Exception
	 */
	public DevUser getDevUserByUserCode(String devCode);
	
	/**
	 * ͨ��userCode��ѯ�û���Ϣ
	 * @param userCode
	 * @return
	 * @throws Exception
	 */
	public BackendUser getBackendUserByUserCode(String userCode);
}
