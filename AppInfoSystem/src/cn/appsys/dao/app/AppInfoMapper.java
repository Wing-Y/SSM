package cn.appsys.dao.app;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import cn.appsys.pojo.AppCategory;
import cn.appsys.pojo.AppInfo;
import cn.appsys.pojo.DataDictionary;

public interface AppInfoMapper {

	/**
	 * ��������ѯApp��Ϣ�б�
	 * @param appinfo
	 * @return
	 */
	public List<AppInfo> getAppInfoList(@Param("softwareName")String softwareName,
			@Param("status")Integer status,
			@Param("flatformId")Integer flatformId,
			@Param("categoryLevel1")Integer categoryLevel1,
			@Param("categoryLevel2")Integer categoryLevel2,
			@Param("categoryLevel3")Integer categoryLevel3,
			@Param("from")Integer from,@Param("pageSize")Integer pageSize) throws Exception;
	
	/**
	 * ��������ѯApp��Ϣ����
	 * @param appinfo
	 * @return
	 */
	public int getAppInfoCount(@Param("softwareName")String softwareName,
			@Param("status")Integer status,
			@Param("flatformId")Integer flatformId,
			@Param("categoryLevel1")Integer categoryLevel1,
			@Param("categoryLevel2")Integer categoryLevel2,
			@Param("categoryLevel3")Integer categoryLevel3) throws Exception;
	
	/**
	 * �������Ͳ�ѯ����_�ʵ� 
	 * @return
	 */
	public List<DataDictionary> getDatadictionarylist(@Param("typeCode")String typeCode) throws Exception;
	
	/**
	 * ��ѯ�����б�
	 * @return
	 */
	public List<AppCategory> getAppCategoryList(@Param("parentId")Integer parentId) throws Exception;
	
	/**
	 * ͨ��APKName��ѯ��¼��
	 * @param APKName
	 * @return
	 */
	public int getAppInfoCountByAPKName(@Param("APKName")String APKName) throws Exception;

	/**
	 * ����APP��Ϣ
	 * @param appInfo
	 * @return
	 * @throws Exception
	 */
	public int addAppInfo(AppInfo appInfo) throws Exception;
	
	/**
	 * ͨ��id��ѯAPP��Ϣ
	 * @param id
	 * @return
	 * @throws Exception
	 */
	public AppInfo getAppInfoById(@Param("id")Integer id) throws Exception;
	
	/**
	 * ͨ��id�޸�Path
	 * @param id
	 * @return
	 * @throws Exception
	 */
	public int updatePathById(@Param("id")Integer id) throws Exception;
	
	/**
	 * ����APP��Ϣ
	 * @param appInfo
	 * @return
	 * @throws Exception
	 */
	public int updateAppInfo(AppInfo appInfo) throws Exception;
	

	/**
	 * �޸�APP���°汾��Ϣ
	 * @param versionId
	 * @param id
	 * @return
	 * @throws Exception
	 */
	public int updateAppInfoOfVersionIdById(@Param("versionId")Integer versionId,@Param("id")Integer id) throws Exception;
	
	/**
	 * ����IDɾ��APP��Ϣ 
	 * @param id
	 * @return
	 * @throws Exception
	 */
	public int deleteAppInfoById(@Param("id")Integer id) throws Exception;
	
	/**
	 * ����ID�޸�APP״̬
	 * @param status
	 * @param id
	 * @return
	 * @throws Exception
	 */
	public int updateAppInfoOfStatusById(@Param("status")Integer status,@Param("id")Integer id) throws Exception;
}
