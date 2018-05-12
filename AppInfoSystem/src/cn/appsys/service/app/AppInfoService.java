package cn.appsys.service.app;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import cn.appsys.pojo.AppCategory;
import cn.appsys.pojo.AppInfo;
import cn.appsys.pojo.DataDictionary;

public interface AppInfoService {
	
	/**
	 * ��������ѯApp��Ϣ�б�
	 * 
	 * @param appinfo
	 * @return
	 */
	public List<AppInfo> getAppInfoList(String softwareName, Integer status,
			Integer flatformId, Integer categoryLevel1, Integer categoryLevel2,
			Integer categoryLevel3, Integer currentPageNo, Integer pageSize);

	/**
	 * ��������ѯApp��Ϣ����
	 * 
	 * @param appinfo
	 * @return
	 */
	public int getAppInfoCount(String softwareName, Integer status,
			Integer flatformId, Integer categoryLevel1, Integer categoryLevel2,
			Integer categoryLevel3);
	
	/**
	 * �������Ͳ�ѯ����_�ʵ� 
	 * @return
	 */
	public List<DataDictionary> getDatadictionarylist(String typeCode);
	
	/**
	 * ��ѯ�����б�
	 * @return
	 */
	public List<AppCategory> getAppCategoryList(Integer pid);
	
	/**
	 * ͨ��APKName��ѯ��¼��
	 * @param APKName
	 * @return
	 */
	public boolean getApkexistByAPKName(@Param("APKName")String APKName);
	
	/**
	 * ����APP��Ϣ
	 * @param appInfo
	 * @return
	 * @throws Exception
	 */
	public boolean addAppInfo(AppInfo appInfo);
	
	/**
	 * ͨ��id��ѯAPP��Ϣ
	 * @param id
	 * @return
	 * @throws Exception
	 */
	public AppInfo getAppInfoById(Integer id);
	
	/**
	 * ͨ��id�޸�Path
	 * @param id
	 * @return
	 * @throws Exception
	 */
	public boolean updatePathById(Integer id);
	
	/**
	 * ����APP��Ϣ
	 * @param appInfo
	 * @return
	 * @throws Exception
	 */
	public boolean updateAppInfo(AppInfo appInfo);
	
	/**
	 * �޸�APP���°汾��Ϣ
	 * @param versionId
	 * @param id
	 * @return
	 * @throws Exception
	 */
	public boolean updateAppInfoOfVersionIdById(Integer versionId,Integer id);
	
	/**
	 * ����IDɾ��APP��Ϣ 
	 * @param id
	 * @return
	 * @throws Exception
	 */
	public boolean deleteAppInfoById(Integer id);
	
	
	/**
	 * ����ID�޸�APP״̬
	 * @param status
	 * @param id
	 * @return
	 * @throws Exception
	 */
	public boolean updateAppInfoOfStatusById(Integer status,Integer id);
}
