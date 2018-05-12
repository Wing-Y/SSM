package cn.appsys.dao.version;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import cn.appsys.pojo.AppVersion;

public interface AppVersionMapper {

	/**
	 * �����汾��Ϣ
	 * @param appVersion
	 * @return
	 * @throws Exception
	 */
	public int addAppVersion(AppVersion appVersion) throws Exception;
	
	/**
	 * ����APPid��ѯ-APP�汾��Ϣ 
	 * @param appVersion
	 * @return
	 * @throws Exception
	 */
	public List<AppVersion> getAppVersionListByAppId(@Param("aid")Integer id) throws Exception;
	
	/**
	 * ��ѯ���°汾������ID
	 * @return
	 * @throws Exception
	 */
	public int getNewId() throws Exception;
	
	/**
	 * ����id��ѯ�汾��Ϣ
	 * @param id
	 * @return
	 * @throws Exception
	 */
	public AppVersion getAppVersionById(@Param("id")Integer id) throws Exception;

	/**
	 * ͨ��id�޸�Path
	 * @param id
	 * @return
	 * @throws Exception
	 */
	public int updateAppVersionOfPathById(@Param("id")Integer id) throws Exception;
	
	/**
	 * �޸İ汾��Ϣ
	 * @param appVersion
	 * @return
	 * @throws Exception
	 */
	public int updateAppVersion(AppVersion appVersion) throws Exception;
	
	/**
	 * ����APPIDɾ���汾��Ϣ
	 * @param id
	 * @return
	 * @throws Exception
	 */
	public int deleteAppVersionByAId(@Param("aid")Integer aid) throws Exception;
}
