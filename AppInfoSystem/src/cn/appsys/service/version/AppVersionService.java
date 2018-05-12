package cn.appsys.service.version;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import cn.appsys.pojo.AppVersion;

public interface AppVersionService {
	
	/**
	 * �����汾��Ϣ
	 * @param appVersion
	 * @return
	 * @throws Exception
	 */
	public boolean addAppVersion(AppVersion appVersion);
	
	/**
	 * ����APPid��ѯ-APP�汾��Ϣ 
	 * @param appVersion
	 * @return
	 * @throws Exception
	 */
	public List<AppVersion> getAppVersionListByAppId(Integer id);
	
	/**
	 * ��ѯ���°汾������ID
	 * @return
	 * @throws Exception
	 */
	 public int getNewId();
	 
	 /**
	 * ����id��ѯ�汾��Ϣ
	 * @param id
	 * @return
	 * @throws Exception
	 */
	public AppVersion getAppVersionById(Integer id);
	

	/**
	 * ͨ��id�޸�Path
	 * @param id
	 * @return
	 * @throws Exception
	 */
	public boolean updateAppVersionOfPathById(Integer id);
	

	/**
	 * �޸İ汾��Ϣ
	 * @param appVersion
	 * @return
	 * @throws Exception
	 */
	public boolean updateAppVersion(AppVersion appVersion);
	
	/**
	 * ����APPIDɾ���汾��Ϣ
	 * @param id
	 * @return
	 * @throws Exception
	 */
	public boolean deleteAppVersionByAId(Integer aid);
}