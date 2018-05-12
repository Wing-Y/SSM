package cn.appsys.dao.user;

import org.apache.ibatis.annotations.Param;
import cn.appsys.pojo.BackendUser;
import cn.appsys.pojo.DevUser;

public interface UserMapper {

	/**
	 * ͨ��DevUser��ѯ�û���Ϣ
	 * @param userCode
	 * @return
	 * @throws Exception
	 */
	public DevUser getDevUserByUserCode(@Param("devCode")String devCode) throws Exception;
	
	/**
	 * ͨ��userCode��ѯ�û���Ϣ
	 * @param userCode
	 * @return
	 * @throws Exception
	 */
	public BackendUser getBackendUserByUserCode(@Param("userCode")String userCode) throws Exception;
}
